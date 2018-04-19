package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Radioactive item
 * <p>
 * Purpose: Prefab & Generic item for radioactive objects
 * Features:
 * * Generate radiation in the environment
 * * Radiate entities
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
public class ItemRadioactive extends Item
{
    public ItemRadioactive(String name, String texture)
    {
        this.setUnlocalizedName(AtomicScience.PREFIX + name);
        this.setTextureName(AtomicScience.PREFIX + texture);
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity entity, int par4, boolean par5)
    {
        if (entity instanceof EntityLivingBase)
        {
            //TODO apply radiation each tick
        }
    }
}
