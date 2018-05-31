package com.builtbroken.atomic.lib.gui.slot;

import com.builtbroken.atomic.lib.gui.GuiContainerBase;
import com.builtbroken.atomic.lib.gui.ISlotRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/30/2018.
 */
public class SlotFluid extends MachineSlot implements ISlotRender
{
    boolean doCheck;

    public SlotFluid(IInventory inventory, int index, int x, int y, boolean doCheck)
    {
        super(inventory, index, x, y);
        this.doCheck = doCheck;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        if (doCheck && stack != null)
        {
            return FluidContainerRegistry.isFilledContainer(stack) || stack.getItem() instanceof IFluidContainerItem;
        }
        return super.isItemValid(stack);
    }

    @Override
    protected void drawIcon(Gui gui, int x, int y)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(GuiContainerBase.GUI_COMPONENTS);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        gui.drawTexturedModalRect(x, y, 0, 18 * 2, 18, 18);
    }
}
