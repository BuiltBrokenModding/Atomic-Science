package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.api.radiation.IRadioactiveItem;
import com.builtbroken.atomic.content.prefab.ItemPrefab;
import net.minecraft.item.ItemStack;

import java.util.function.IntSupplier;

/**
 * Radioactive item
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
public class ItemRadioactive extends ItemPrefab implements IRadioactiveItem
{
    public final IntSupplier radioactiveMaterialValue;

    public ItemRadioactive(String key, String name, IntSupplier radioactiveMaterialValue)
    {
        super(key, name);
        this.radioactiveMaterialValue = radioactiveMaterialValue;
    }

    @Override
    public int getRadioactiveMaterial(ItemStack stack)
    {
        return radioactiveMaterialValue.getAsInt() * stack.getCount();
    }
}
