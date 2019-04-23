package com.builtbroken.atomic.content.items.cell;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

/**
 * Fluid cell that has negative results if power fails
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public class ItemPoweredCell extends ItemFluidCell
{
    public HashMap<Fluid, OnUpdate> fluidUpdateFunction = new HashMap();

    public ItemPoweredCell()
    {
        super(100);
        this.setCreativeTab(AtomicScience.creativeTab);
        this.setTranslationKey(AtomicScience.PREFIX + "cell.powered");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> lines, ITooltipFlag flagIn)
    {
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack != null)
        {
            lines.add("Power: " + "100%");
            lines.add("Fluid: " + fluidStack.getLocalizedName());
            lines.add("Amount: " + fluidStack.amount);
        }
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
