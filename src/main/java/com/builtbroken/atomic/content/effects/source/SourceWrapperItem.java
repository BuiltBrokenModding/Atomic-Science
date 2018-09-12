package com.builtbroken.atomic.content.effects.source;

import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public class SourceWrapperItem implements IIndirectEffectSource
{
    Entity holdOfTheItem;
    ItemStack item;
    int slot;

    public SourceWrapperItem(Entity entity, ItemStack itemstack, int slot)
    {
        this.holdOfTheItem = entity;
        this.item = itemstack;
        this.slot = slot;
    }

    @Override
    public World world()
    {
        return holdOfTheItem.world;
    }

    @Override
    public double z()
    {
        return holdOfTheItem.posZ;
    }

    @Override
    public double x()
    {
        return holdOfTheItem.posX;
    }

    @Override
    public double y()
    {
        return holdOfTheItem.posY;
    }
}
