package com.builtbroken.atomic.content.machines;

import com.builtbroken.atomic.lib.network.IPacket;
import com.builtbroken.atomic.lib.network.IPacketIDReceiver;
import com.builtbroken.atomic.lib.network.netty.PacketSystem;
import com.builtbroken.atomic.lib.network.packet.PacketTile;
import com.builtbroken.atomic.lib.transform.IPosWorld;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public abstract class TileEntityMachine extends TileEntity implements IPacketIDReceiver, IPosWorld
{
    public static final int DESC_PACKET_ID = -1;

    private int _ticks = 0;
    private boolean _syncClientNextTick = true;

    //-----------------------------------------------
    //--------- Update methods ----------------------
    //-----------------------------------------------

    public final void updateEntity()
    {
        if (_ticks == 0)
        {
            firstTick();
        }
        update(_ticks);
        _ticks++;
        if (_ticks + 1 == Integer.MAX_VALUE)
        {
            _ticks = 1;
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
    protected void firstTick()
    {

    }

    /**
     * Called each update loop
     *
     * @param ticks - number of ticks of the world (20 ticks a second)
     */
    protected void update(int ticks)
    {

    }

    //-----------------------------------------------
    //--------- Network -----------------------------
    //-----------------------------------------------


    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, IPacket type)
    {
        if (isClient() && id == DESC_PACKET_ID)
        {
            readDescPacket(buf, player);
            return true;
        }
        return false;
    }

    /**
     * Will send a description packet to the client next tick
     */
    protected void syncClientNextTick()
    {
        _syncClientNextTick = true;
    }

    /**
     * Sends the description packet to the client
     */
    protected void sendDescPacket()
    {
        PacketTile packetTile = new PacketTile("reactor_desc", DESC_PACKET_ID, this);

        //Collect data
        List<Object> list = new ArrayList();
        writeDescPacket(list, null); //TODO get list of players, then send
        packetTile.addData(list);

        PacketSystem.INSTANCE.sendToAllAround(packetTile, this);
    }

    /**
     * Called to write data to the packet
     * <p>
     * This works by building a list of objects to write. These objects
     * will be written when the packet is encoded. This helps reduce issues
     * trying to encode a bytebuf into a bytebuf.
     *
     * @param dataList - list of objects to write
     * @param player   - player to send the packet, can be null
     */
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {

    }

    /**
     * Called to read the packet
     *
     * @param buf    - raw data
     * @param player - player reading the data
     */
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {

    }

    //-----------------------------------------------
    //--------- Helpers -----------------------------
    //-----------------------------------------------

    protected boolean isServer()
    {
        return worldObj != null && !worldObj.isRemote;
    }

    protected boolean isClient()
    {
        return worldObj != null && worldObj.isRemote;
    }

    //-----------------------------------------------
    //--------- Position ----------------------------
    //-----------------------------------------------

    @Override
    public World world()
    {
        return getWorldObj();
    }

    @Override
    public double z()
    {
        return zCoord + 0.5;
    }

    @Override
    public double x()
    {
        return xCoord + 0.5;
    }

    @Override
    public double y()
    {
        return yCoord + 0.5;
    }

    @Override
    public int zi()
    {
        return zCoord;
    }

    @Override
    public int xi()
    {
        return xCoord;
    }

    @Override
    public int yi()
    {
        return yCoord;
    }
}
