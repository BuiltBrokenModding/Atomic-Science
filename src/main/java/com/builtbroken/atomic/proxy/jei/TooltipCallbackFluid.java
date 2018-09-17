package com.builtbroken.atomic.proxy.jei;

import mezz.jei.api.gui.ITooltipCallback;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/17/2018.
 */
public class TooltipCallbackFluid implements ITooltipCallback<FluidStack>
{
    @Override
    public void onTooltip(int slotIndex, boolean input, FluidStack ingredient, List<String> tooltip)
    {
        tooltip.add(ingredient.amount + " mB"); 
    }
}
