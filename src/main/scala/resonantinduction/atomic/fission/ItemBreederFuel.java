package resonantinduction.atomic.fission;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.api.IReactor;
import resonant.api.IReactorComponent;

/** Breeder rods */
public class ItemBreederFuel extends ItemRadioactive implements IReactorComponent
{
    public ItemBreederFuel(int itemID)
    {
        super(itemID);
        this.setMaxDamage(ItemFissileFuel.DECAY);
        this.setMaxStackSize(1);
        this.setNoRepair();
    }

    @Override
    public void onReact(ItemStack itemStack, IReactor reactor)
    {
        TileEntity tileEntity = (TileEntity) reactor;
        World worldObj = tileEntity.worldObj;

        // Breeder fuel rods have half the normal energy potential of pure uranium.
        reactor.heat(ItemFissileFuel.ENERGY_PER_TICK / 2);

        if (reactor.world().getWorldTime() % 20 == 0)
        {
            itemStack.setItemDamage(Math.min(itemStack.getItemDamage() + 1, itemStack.getMaxDamage()));
        }
    }
}
