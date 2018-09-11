package com.builtbroken.atomic.content.items.cell;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;

/**
 * Fluid cell that has negative results if power fails
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public class ItemPoweredCell extends ItemFluidCell
{
    public HashMap<Fluid, OnUpdate> fluidUpdateFunction = new HashMap();

    public ItemPoweredCell()
    {
        super(100);
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    //TODO add power (IC2, RF, UE)
    //TODO decrease power per tick (ensure there is a good few hours of power)
    //TODO track last tick stores (use to estimate power loss over time, ensure user has some time to move item)

    @Override
    public void onUpdate(ItemStack container, World world, Entity entity, int slot, boolean p_77663_5_)
    {
        FluidStack fluidStack = getFluid(container);
        if (fluidStack != null)
        {
            OnUpdate function = fluidUpdateFunction.get(fluidStack.getFluid());
            if (function != null)
            {
                function.onUpdate(container, world, entity, slot, p_77663_5_);
            }
        }
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem)
    {
        ItemStack container = entityItem.getItem();
        FluidStack fluidStack = getFluid(container);
        if (fluidStack != null)
        {
            OnUpdate function = fluidUpdateFunction.get(fluidStack.getFluid());
            if (function != null)
            {
                function.onEntityItemUpdate(container, entityItem);
            }
        }
        return false;
    }

    public static interface OnUpdate
    {
        default void onUpdate(ItemStack container, World world, Entity entity, int slot, boolean p_77663_5_)
        {
        }

        default void onEntityItemUpdate(ItemStack container, EntityItem entityItem)
        {
        }
    }
}
