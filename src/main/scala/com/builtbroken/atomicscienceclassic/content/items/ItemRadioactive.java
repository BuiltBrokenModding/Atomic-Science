package com.builtbroken.atomicscienceclassic.content.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Radioactive Items */
public class ItemRadioactive extends Item
{
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity entity, int par4, boolean par5)
    {
        if (entity instanceof EntityLivingBase)
        {
            //resonant.lib.prefab.poison.PoisonRadiation;.INSTANCE.poisonEntity(new Pos(entity), (EntityLivingBase) entity, 1);
        }
    }

}
