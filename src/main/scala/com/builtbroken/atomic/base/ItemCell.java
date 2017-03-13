package com.builtbroken.atomic.base;

import com.builtbroken.atomic.Atomic;
import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemCell extends Item
{
    public ItemCell()
    {
        setContainerItem(Atomic.itemCell);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List list, boolean par4)
    {
        String tooltip = LanguageUtility.getLocal(getUnlocalizedName(itemStack) + ".tooltip");

        if (tooltip != null && tooltip.length() > 0)
        {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                list.add(LanguageUtility.getLocal("tooltip.noShift").replace("%0", Colors.AQUA.toString()).replace("%1", Colors.GREY.toString()));
            }
            else
            {
                list.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        String localized = LanguageUtility.getLocal(getUnlocalizedName() + "." + itemstack.getItemDamage() + ".name");
        if (localized != null && !localized.isEmpty())
        {
            return getUnlocalizedName() + "." + itemstack.getItemDamage();
        }
        return getUnlocalizedName();
    }
}
