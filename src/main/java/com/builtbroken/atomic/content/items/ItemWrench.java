package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.lib.LanguageUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

/**
 * Multi-tool for working with and configuring machines
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/29/2018.
 */
public class ItemWrench extends Item
{
    public ItemWrench()
    {
        this.setUnlocalizedName(AtomicScience.PREFIX + "wrench");
        this.setTextureName(AtomicScience.PREFIX + "wrench");
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit)
    {
        if (!world.isRemote)
        {
            if (player.isSneaking())
            {
                toggleMode(stack);
                String translation = LanguageUtility.getLocal(getUnlocalizedName(stack) + ".mode.set." + getMode(stack));
                player.addChatComponentMessage(new ChatComponentText(translation));
            }
            else
            {
                int mode = getMode(stack);
                if (mode == 0)
                {
                    //TODO rotate
                }
                //Red
                else if (mode == 1)
                {

                }
                //Green
                else if (mode == 2)
                {

                }
                //Blue
                else if (mode == 3)
                {

                }
            }
        }
        return true;
    }

    public void toggleMode(ItemStack stack)
    {
        int nextMode = getMode(stack) + 1;
        if (nextMode > getMaxMode(stack))
        {
            nextMode = 0;
        }
        setMode(stack, nextMode);
    }

    public int getMode(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            return stack.getTagCompound().getInteger("mode");
        }
        return 0;
    }

    public void setMode(ItemStack stack, int mode)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("mode", mode);
    }

    public int getMaxMode(ItemStack stack)
    {
        return 2;
    }
}
