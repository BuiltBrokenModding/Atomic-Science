package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.radiation.IRadioactiveItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Radioactive item
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
public class ItemRadioactive extends Item implements IRadioactiveItem
{
    public final int radioactiveMaterialValue;

    public ItemRadioactive(String name, int radioactiveMaterialValue)
    {
        this.radioactiveMaterialValue = radioactiveMaterialValue;
        this.setTranslationKey(AtomicScience.PREFIX + name);
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    public int getRadioactiveMaterial(ItemStack stack)
    {
        return radioactiveMaterialValue * stack.getCount();
    }
}
