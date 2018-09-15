package com.builtbroken.atomic.proxy.eu;

import com.builtbroken.atomic.config.mods.ConfigIC2;
import com.builtbroken.atomic.lib.power.PowerHandler;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class PowerHandlerEU extends PowerHandler
{
    public static final PowerHandlerEU INSTANCE = new PowerHandlerEU();

    @Override
    @Optional.Method(modid = "ic2")
    public boolean canHandle(ItemStack stack)
    {
        return ConfigIC2.ENABLE_IC2 && stack.getItem() instanceof IElectricItem;
    }

    @Override
    @Optional.Method(modid = "ic2")
    public boolean canHandle(EnumFacing side, TileEntity tile)
    {
        return ConfigIC2.ENABLE_IC2 && tile instanceof IEnergySink && ((IEnergySink) tile).acceptsEnergyFrom(null, side);
    }


    @Override
    @Optional.Method(modid = "ic2")
    public int addPower(EnumFacing side, TileEntity tile, int powerInFE, boolean doAction)
    {
        if (canHandle(side, tile))
        {
            //Get requested powerInFE of the target machine
            double demand_eu = ((IEnergySink) tile).getDemandedEnergy();
            int demand_fe = (int) Math.floor(demand_eu * ConfigIC2.FE_PER_EU);

            //If simulating return demand
            if(!doAction)
            {
                return demand_fe;
            }

            //Check how much powerInFE we can remove
            int inject_fe = Math.min(demand_fe, powerInFE);
            if (inject_fe > 0)
            {
                //Convert to IC2 powerInFE
                double inject_eu = inject_fe / ConfigIC2.FE_PER_EU;

                //Inject energy
                double remain_eu = ((IEnergySink) tile).injectEnergy(side, inject_eu, 1);

                //Remove energy from storage
                inject_eu -= remain_eu;

                //Convert back to FE
                return (int) Math.ceil(inject_eu * ConfigIC2.FE_PER_EU);
            }
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = "ic2")
    public int addPower(ItemStack stack, int insert_fe, boolean doAction)
    {
        if (canHandle(stack))
        {
            int tier = ((IElectricItem) stack.getItem()).getTier(stack);

            //Convert to IC2 power
            double insert_eu = insert_fe / ConfigIC2.FE_PER_EU;

            //Give energy
            double taken_eu = ElectricItem.manager.charge(stack, insert_eu, tier, false, !doAction);

            //Convert to FE
            return (int) Math.ceil(taken_eu * ConfigIC2.FE_PER_EU);
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = "ic2")
    public int removePower(ItemStack stack, int remove_fe, boolean doAction)
    {
        if (canHandle(stack))
        {
            int tier = ((IElectricItem) stack.getItem()).getTier(stack);

            //Calculate max remove_eu from battery
            double remove_eu = remove_fe / ConfigIC2.FE_PER_EU;
            remove_eu = ElectricItem.manager.discharge(stack, remove_eu, tier, false, true, !doAction);

            //Convert to FE
            return (int) Math.ceil(remove_eu * ConfigIC2.FE_PER_EU);
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = "ic2")
    public void onTileValidate(TileEntity tile)
    {
        if (ConfigIC2.ENABLE_IC2 && tile instanceof IEnergyTile && !tile.getWorld().isRemote)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) tile));
        }
    }

    @Override
    @Optional.Method(modid = "ic2")
    public void onTileInvalidate(TileEntity tile)
    {
        if (ConfigIC2.ENABLE_IC2 && tile instanceof IEnergyTile && !tile.getWorld().isRemote)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) tile));
        }
    }
}
