package resonantinduction.atomic.fission;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import resonant.api.IReactor;
import resonant.api.IReactorComponent;
import resonantinduction.atomic.Atomic;
import resonantinduction.core.Settings;
import universalelectricity.api.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Fissile fuel rod */
public class ItemFissileFuel extends ItemRadioactive implements IReactorComponent
{
    public static final int DECAY = 2500;

    /** Temperature at which the fuel rod will begin to re-enrich itself. */
    public static final int BREEDING_TEMP = 1200;

    /** The energy in one KG of uranium is: 72PJ, 100TJ in one cell of uranium. */
    public static final long ENERGY = 100000000000L;

    /** Approximately 20,000,000J per tick. 400 MW. */
    public static final long ENERGY_PER_TICK = ENERGY / 50000;

    public ItemFissileFuel(int itemID)
    {
        super(itemID);
        this.setMaxStackSize(1);
        this.setMaxDamage(DECAY);
        this.setNoRepair();
    }

    @Override
    public void onReact(ItemStack itemStack, IReactor reactor)
    {
        TileEntity tileEntity = (TileEntity) reactor;
        World worldObj = tileEntity.worldObj;
        int reactors = 0;

        for (int i = 0; i < 6; i++)
        {
            Vector3 checkPos = new Vector3(tileEntity).translate(ForgeDirection.getOrientation(i));
            TileEntity tile = checkPos.getTileEntity(worldObj);

            // Check that the other reactors not only exist but also are running.
            if (tile instanceof IReactor && ((IReactor) tile).getTemperature() > BREEDING_TEMP)
            {
                reactors++;
            }
        }

        // Only three reactor cells are required to begin the uranium breeding process instead of four.
        if (reactors >= 3)
        {
            // Begin the process of re-enriching the uranium rod but not consistently.
            if (worldObj.rand.nextInt(1000) <= 100 && reactor.getTemperature() > BREEDING_TEMP)
            {
                // Cells can regain a random amount of health per tick.
                int healAmt = worldObj.rand.nextInt(5);
                itemStack.setItemDamage(Math.max(itemStack.getItemDamage() - healAmt, 0));
                // System.out.println("[Atomic Science] [Reactor Cell] Breeding " + String.valueOf(healAmt) + " back into fissle rod. " + String.valueOf(itemStack.getItemDamage()) + " / " + String.valueOf(itemStack.getMaxDamage()));
            }
        }
        else
        {
            reactor.heat(ENERGY_PER_TICK);

            if (reactor.world().getWorldTime() % 20 == 0)
            {
                itemStack.setItemDamage(Math.min(itemStack.getItemDamage() + 1, itemStack.getMaxDamage()));
            }

            // Create toxic waste.
            if (Settings.allowToxicWaste && worldObj.rand.nextFloat() > 0.5)
            {
                FluidStack fluid = Atomic.FLUIDSTACK_TOXIC_WASTE.copy();
                fluid.amount = 1;
                reactor.fill(ForgeDirection.UNKNOWN, fluid, true);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, getMaxDamage() - 1));
    }

}
