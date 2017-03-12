package resonantinduction.atomic.base;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import resonant.lib.render.EnumColor;
import resonant.lib.utility.LanguageUtility;
import resonantinduction.atomic.Atomic;

public class ItemCell extends Item
{
    public ItemCell(int itemID)
    {
        super(itemID);
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
                list.add(LanguageUtility.getLocal("tooltip.noShift").replace("%0", EnumColor.AQUA.toString()).replace("%1", EnumColor.GREY.toString()));
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
