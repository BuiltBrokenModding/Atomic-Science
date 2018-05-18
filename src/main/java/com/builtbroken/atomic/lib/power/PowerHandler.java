package com.builtbroken.atomic.lib.power;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Simple wrapper for power systems
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public abstract class PowerHandler
{
    public abstract boolean canHandle(ForgeDirection side, TileEntity tile);

    public abstract int addPower(ForgeDirection side, Object object, int power, boolean doAction);

    public abstract int removePower(ForgeDirection side, Object object, int power, boolean doAction);
}
