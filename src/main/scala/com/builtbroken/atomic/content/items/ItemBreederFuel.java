package com.builtbroken.atomic.content.items;

import net.minecraft.item.ItemStack;
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
        // Breeder fuel rods have half the normal energy potential of pure uranium.
        reactor.heat(ItemFissileFuel.ENERGY_PER_TICK / 2);

        if (reactor.world().getWorldTime() % 20 == 0)
        {
            itemStack.setItemDamage(Math.min(itemStack.getItemDamage() + 1, itemStack.getMaxDamage()));
        }
    }
}
