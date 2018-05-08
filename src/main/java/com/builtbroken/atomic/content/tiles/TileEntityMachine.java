package com.builtbroken.atomic.content.tiles;

import net.minecraft.tileentity.TileEntity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class TileEntityMachine extends TileEntity
{
    private int ticks = 0;

    public final void updateEntity()
    {
        if (ticks == 0)
        {
            firstTick();
        }
        update(ticks);
        ticks++;
        if (ticks + 1 == Integer.MAX_VALUE)
        {
            ticks = 1;
        }
    }

    protected void firstTick()
    {

    }

    protected void update(int ticks)
    {

    }

    protected boolean isServer()
    {
        return worldObj != null && !worldObj.isRemote;
    }

    protected boolean isClient()
    {
        return worldObj != null && worldObj.isRemote;
    }
}
