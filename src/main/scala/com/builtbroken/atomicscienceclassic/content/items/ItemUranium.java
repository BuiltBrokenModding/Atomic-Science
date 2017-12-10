package com.builtbroken.atomicscienceclassic.content.items;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.List;

/** Uranium */
public class ItemUranium extends ItemRadioactive
{
    public ItemUranium()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
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
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list)
    {
        list.add(new ItemStack(par1, 1, 0));
        list.add(new ItemStack(par1, 1, 1));
    }
}
