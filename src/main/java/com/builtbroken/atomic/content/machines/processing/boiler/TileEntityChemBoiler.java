package com.builtbroken.atomic.content.machines.processing.boiler;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.client.EffectRefs;
import com.builtbroken.atomic.config.content.ConfigContent;
import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.items.wrench.WrenchMode;
import com.builtbroken.atomic.content.machines.processing.ProcessorRecipeHandler;
import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;
import com.builtbroken.atomic.content.machines.processing.boiler.gui.ContainerChemBoiler;
import com.builtbroken.atomic.content.machines.processing.boiler.gui.GuiChemBoiler;
import com.builtbroken.atomic.content.recipes.ProcessingRecipeList;
import com.builtbroken.atomic.content.recipes.chem.RecipeChemBoiler;
import com.builtbroken.atomic.lib.SideSettings;
import com.builtbroken.atomic.lib.fluid.FluidSideWrapper;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.client.PacketSpawnParticle;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class TileEntityChemBoiler extends TileEntityProcessingMachine<IItemHandlerModifiable, TileEntityChemBoiler, RecipeChemBoiler> implements IGuiTile
{
    public static final int SLOT_FLUID_INPUT = 0;
    public static final int SLOT_ITEM_INPUT = 1;
    public static final int SLOT_ITEM_OUTPUT = 2;
    public static final int SLOT_BATTERY = 3;
    public static final int SLOT_WASTE_FLUID = 4;
    public static final int SLOT_HEX_FLUID = 5;
    public static final int INVENTORY_SIZE = 6;

    public static int PROCESSING_TIME = 100;

    private final FluidTank blueTank;
    private final FluidTank greenTank;
    private final FluidTank yellowTank;

    private final SideSettings blueTankSideSettings = new SideSettings(true); //TODO switch to map of all tanks
    private final SideSettings greenTankSideSettings = new SideSettings(false);
    private final SideSettings yellowTankSideSettings = new SideSettings(false);

    private final FluidSideWrapper[] fluidSideWrappers = new FluidSideWrapper[6];

    public TileEntityChemBoiler()
    {
        blueTank = new FluidTank(Fluid.BUCKET_VOLUME * 10);
        greenTank = new FluidTank(Fluid.BUCKET_VOLUME * 10);
        yellowTank = new FluidTank(Fluid.BUCKET_VOLUME * 10);

        for (EnumFacing side : EnumFacing.values())
        {
            fluidSideWrappers[side.ordinal()] = new FluidSideWrapper(side);
            fluidSideWrappers[side.ordinal()].add(blueTankSideSettings, blueTank, false);  //TODO switch to map of all tanks for easier access
            fluidSideWrappers[side.ordinal()].add(greenTankSideSettings, greenTank, true);
            fluidSideWrappers[side.ordinal()].add(yellowTankSideSettings, yellowTank, true);
        }
    }

    @Override
    public void update(int ticks)
    {
        super.update(ticks);
        if (isServer())
        {
            drainBattery(SLOT_BATTERY);
        }
    }

    @Override
    protected IItemHandlerModifiable createInventory()
    {
        return new InvChemBoiler(this);
    }

    @Override
    protected IItemHandlerModifiable createInternalInventory()
    {
        return new ItemStackHandler(inventorySize())
        {
            @Override
            public int getSlotLimit(int slot)
            {
                if (slot == SLOT_FLUID_INPUT || slot == SLOT_HEX_FLUID || slot == SLOT_WASTE_FLUID || slot == SLOT_BATTERY)
                {
                    return 1;
                }
                return super.getSlotLimit(slot);
            }
        };
    }

    @Override
    protected int inventorySize()
    {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (facing != null && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)  //TODO if facing is null return group container
        {
            return fluidSideWrappers[facing.ordinal()].hasTank();
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (facing != null && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return (T) fluidSideWrappers[facing.ordinal()];
        }
        return super.getCapability(capability, facing);
    }

    @Override
    protected void onProcessed()
    {
        if (isServer())
        {
            PacketSpawnParticle packetSpawnParticle = new PacketSpawnParticle(world.provider.getDimension(),
                    xi() + 0.5, yi() + 0.5, zi() + 0.5,
                    0, 0, 0,
                    EffectRefs.BOILER_COMPLETE);
            PacketSystem.INSTANCE.sendToAllAround(packetSpawnParticle, world, this, 30);
        }
    }

    @Override
    protected void doEffects(int ticks)
    {
        if (world.rand.nextFloat() > 0.3)
        {
            AtomicScience.sideProxy.spawnParticle(EffectRefs.BOILER_RUNNING, xi() + 0.5, yi() + 0.5, zi() + 0.5, 0, 0, 0);
        }
    }

    @Override
    protected void preProcess(int ticks)
    {
        fillTank(SLOT_FLUID_INPUT, getBlueTank());
    }

    @Override
    protected int getProcessingTime()
    {
        return PROCESSING_TIME;
    }

    @Override
    protected void postProcess(int ticks)
    {
        outputFluids(SLOT_WASTE_FLUID, getGreenTank());
        outputFluids(SLOT_HEX_FLUID, getYellowTank());
        outputFluidToTiles(getGreenTank(), f -> greenTankSideSettings.get(f));
        outputFluidToTiles(getYellowTank(), f -> yellowTankSideSettings.get(f));
    }

    @Override
    public ProcessingRecipeList getRecipeList()
    {
        return ProcessorRecipeHandler.INSTANCE.chemBoilerProcessingRecipe;
    }

    @Override
    protected void onSlotStackChanged(ItemStack prev, ItemStack stack, int slot)
    {
        if (isServer() && slot == SLOT_ITEM_INPUT)
        {
            checkRecipe();
        }
        super.onSlotStackChanged(prev, stack, slot);
    }

    @Override
    public void onWrench(WrenchMode type, WrenchColor color, EnumFacing side, EntityPlayer player)
    {
        if (type == WrenchMode.FLUID && side != null)
        {
            if (color == WrenchColor.GREEN)
            {
                greenTankSideSettings.toggle(side);
                player.sendMessage(new TextComponentString(greenTankSideSettings.get(side) ? "Green tank set to output on side" : "Green tank set to ignore side"));
            }
            else if (color == WrenchColor.YELLOW)
            {
                yellowTankSideSettings.toggle(side);
                player.sendMessage(new TextComponentString(yellowTankSideSettings.get(side) ? "Yellow tank set to output on side" : "Yellow tank set to ignore side"));
            }
            else if (color == WrenchColor.BLUE)
            {
                blueTankSideSettings.toggle(side);
                player.sendMessage(new TextComponentString(blueTankSideSettings.get(side) ? "Blue tank set to input on side" : "Blue tank set to ignore side"));
            }
        }
    }

    public FluidTank getBlueTank() //TODO convert to map
    {
        return blueTank;
    }

    public FluidTank getGreenTank() //TODO convert to map
    {
        return greenTank;
    }

    public FluidTank getYellowTank() //TODO convert to map
    {
        return yellowTank;
    }

    //-----------------------------------------------
    //--------Props ---------------------------------
    //-----------------------------------------------

    @Override
    public int getEnergyUsage()
    {
        return ConfigContent.POWER_USAGE.BOILER;
    }

    //-----------------------------------------------
    //--------GUI Handler ---------------------------
    //-----------------------------------------------

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player)
    {
        return new ContainerChemBoiler(player, this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiChemBoiler(player, this);
    }

    @Override
    protected void writeGuiPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeGuiPacket(dataList, player);
        dataList.add(getBlueTank());
        dataList.add(getGreenTank());
        dataList.add(getYellowTank());
    }

    @Override
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readGuiPacket(buf, player);
        getBlueTank().readFromNBT(ByteBufUtils.readTag(buf));
        getGreenTank().readFromNBT(ByteBufUtils.readTag(buf));
        getYellowTank().readFromNBT(ByteBufUtils.readTag(buf));
    }

    //-----------------------------------------------
    //--------Save/Load -----------------------------
    //-----------------------------------------------

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setTag("wasteTank", getGreenTank().writeToNBT(new NBTTagCompound()));
        nbt.setTag("inputTank", getBlueTank().writeToNBT(new NBTTagCompound()));
        nbt.setTag("hexTank", getYellowTank().writeToNBT(new NBTTagCompound()));

        nbt.setTag("wasteTankSides", greenTankSideSettings.save(new NBTTagCompound()));
        nbt.setTag("hexTankSides", yellowTankSideSettings.save(new NBTTagCompound()));
        nbt.setTag("waterTankSides", blueTankSideSettings.save(new NBTTagCompound()));

        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        getGreenTank().readFromNBT(nbt.getCompoundTag("wasteTank"));
        getBlueTank().readFromNBT(nbt.getCompoundTag("inputTank"));
        getYellowTank().readFromNBT(nbt.getCompoundTag("hexTank"));

        greenTankSideSettings.load(nbt.getCompoundTag("wasteTankSides"));
        yellowTankSideSettings.load(nbt.getCompoundTag("hexTankSides"));
        blueTankSideSettings.load(nbt.getCompoundTag("waterTankSides"));
    }
}
