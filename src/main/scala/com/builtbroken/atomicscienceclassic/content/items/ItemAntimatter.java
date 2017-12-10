package com.builtbroken.atomicscienceclassic.content.items;

import com.builtbroken.atomicscienceclassic.Atomic;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;

import java.util.List;

/* Antimatter Cell */
public class ItemAntimatter extends ItemCell
{
    @SideOnly(Side.CLIENT)
    private IIcon iconGram;

    public ItemAntimatter()
    {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "") + "_milligram");
        this.iconGram = iconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "") + "_gram");
    }

    @Override
    public IIcon getIconFromDamage(int metadata)
    {
        if (metadata >= 1)
        {
            return this.iconGram;
        }
        else
        {
            return this.itemIcon;
        }
    }

    @Override
    public void getSubItems(Item id, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(id, 1, 0));
        par3List.add(new ItemStack(id, 1, 1));
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return 160;
    }

    @SubscribeEvent
    public void onItemExpires(ItemExpireEvent evt)
    {
        if (evt.entityItem != null)
        {
            ItemStack itemStack = evt.entityItem.getEntityItem();

            if (itemStack != null)
            {
                if (itemStack.getItem() == this)
                {
                    evt.entityItem.worldObj.playSoundEffect(evt.entityItem.posX, evt.entityItem.posY, evt.entityItem.posZ, Atomic.PREFIX + "antimatter", 3f, 1f - evt.entityItem.worldObj.rand.nextFloat() * 0.3f);

                    if (!evt.entityItem.worldObj.isRemote)
                    {
                        //TODO trigger ICBM antimatter explosion
                    }
                }
            }
        }
    }
}
