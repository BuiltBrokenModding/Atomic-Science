package com.builtbroken.atomic.lib.gui.tip;

import net.minecraftforge.fluids.IFluidTank;

import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/30/2018.
 */
public class ToolTipTank extends ToolTip
{
    public final IFluidTank tank;

    public ToolTipTank(Rectangle area, String string, IFluidTank tank)
    {
        super(area, string, true);
        this.tank = tank;
    }

    @Override
    public String getString()
    {
        if (tank.getFluid() != null)
        {
            String string = super.getString();
            string = string.replace("-vol-", "" + tank.getFluidAmount());
            string = string.replace("-cap-", "" + tank.getCapacity());
            string = string.replace("-fluid-", "" + tank.getFluid().getLocalizedName());
            return string;
        }
        else
        {
            return translate(string + ".empty");
        }
    }
}
