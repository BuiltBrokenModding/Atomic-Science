package com.builtbroken.atomic.content.items.cell;

import com.builtbroken.atomic.content.ASItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/21/2018.
 */
public class CreativeTabCells extends CreativeTabs
{
    public CreativeTabCells()
    {
        super("fluid.cells");
    }

    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> list)
    {
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
        {
            list.add(ASItems.itemFluidCell.getContainerForFluid(fluid));
        }
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ASItems.itemFluidCell);
    }
}
