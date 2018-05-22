package com.builtbroken.atomic.content.items.cell;

import com.builtbroken.atomic.content.ASItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

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
    public void displayAllReleventItems(List list)
    {
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
        {
            ItemStack stack = new ItemStack(ASItems.itemFluidCell, 1, 0);
            ASItems.itemFluidCell.fill(stack, new FluidStack(fluid, ASItems.itemFluidCell.getCapacity(stack)), true);
            list.add(stack);
        }
    }

    @Override
    public Item getTabIconItem()
    {
        return ASItems.itemFluidCell;
    }
}
