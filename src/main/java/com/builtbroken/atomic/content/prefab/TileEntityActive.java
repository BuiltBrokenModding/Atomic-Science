package com.builtbroken.atomic.content.prefab;

import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.lib.gui.IPlayerUsing;
import com.builtbroken.atomic.lib.timer.ITickTimer;
import com.builtbroken.atomic.lib.transform.IPosWorld;
import com.builtbroken.atomic.network.IPacketIDReceiver;
import net.minecraft.util.ITickable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public abstract class TileEntityActive extends TileEntityPrefab implements IPacketIDReceiver, IPosWorld, IPlayerUsing, ITickable
{
    private int _ticks = 0;
    private boolean _syncClientNextTick = true;

    protected final List<ITickTimer> tickServer = new ArrayList();
    protected final List<ITickTimer> tickClient = new ArrayList();

    //-----------------------------------------------
    //--------- Update methods ----------------------
    //-----------------------------------------------

    public final void update()
    {
        //Init tick
        if (_ticks == 0)
        {
            firstTick(world.isRemote);
        }

        //Tick timers
        if (isServer())
        {
            tickServer.forEach(timer -> timer.tick(this, _ticks));
        }
        else
        {
            tickClient.forEach(timer -> timer.tick(this, _ticks));
        }

        //Do tick
        update(_ticks, world.isRemote);

        //Increase tick
        _ticks++;
        if (_ticks + 1 == Integer.MAX_VALUE)
        {
            _ticks = 1;
        }

        //GUI packets
        if (isServer() && _ticks % 3 == 0 && this instanceof IGuiTile)
        {
            sendGuiPacket();
        }

        //Sync
        if (_syncClientNextTick)
        {
            _syncClientNextTick = false;
            sendDescPacket();
        }
    }

    public int getTicks()
    {
        return _ticks;
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
    public void syncClientNextTick()
    {
        _syncClientNextTick = true;
    }
}
