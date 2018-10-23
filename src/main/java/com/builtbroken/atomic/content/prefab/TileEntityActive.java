package com.builtbroken.atomic.content.prefab;

import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.lib.gui.IPlayerUsing;
import com.builtbroken.atomic.lib.transform.IPosWorld;
import com.builtbroken.atomic.network.IPacketIDReceiver;
import net.minecraft.util.ITickable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public abstract class TileEntityActive extends TileEntityPrefab implements IPacketIDReceiver, IPosWorld, IPlayerUsing, ITickable
{
    private int _ticks = 0;
    private boolean _syncClientNextTick = true;

    //-----------------------------------------------
    //--------- Update methods ----------------------
    //-----------------------------------------------

    public final void update()
    {
        if (_ticks == 0)
        {
            firstTick(world.isRemote);
        }
        update(_ticks, world.isRemote);
        _ticks++;
        if (_ticks + 1 == Integer.MAX_VALUE)
        {
            _ticks = 1;
        }

        if (isServer() && _ticks % 3 == 0 && this instanceof IGuiTile)
        {
            sendGuiPacket();
        }

        if (_syncClientNextTick)
        {
            _syncClientNextTick = false;
            sendDescPacket();
        }
    }

    /**
     * Called on the very fist update loop call.
     * Only called once after the tile has been created
     */
    protected void firstTick(boolean isClient)
    {

    }

    /**
     * Called each update loop
     *
     * @param ticks - number of ticks of the world (20 ticks a second)
     */
    protected void update(int ticks, boolean isClient)
    {

    }

    /**
     * Will send a description packet to the client next tick
     */
    protected void syncClientNextTick()
    {
        _syncClientNextTick = true;
    }
}
