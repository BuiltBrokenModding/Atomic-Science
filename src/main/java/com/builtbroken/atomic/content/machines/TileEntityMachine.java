package com.builtbroken.atomic.content.machines;

import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.lib.gui.IPlayerUsing;
import com.builtbroken.atomic.network.IPacket;
import com.builtbroken.atomic.network.IPacketIDReceiver;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.PacketTile;
import com.builtbroken.atomic.lib.transform.IPosWorld;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public abstract class TileEntityMachine extends TileEntity implements IPacketIDReceiver, IPosWorld, IPlayerUsing, ITickable
{
    public static final int DESC_PACKET_ID = -1;
    public static final int GUI_PACKET_ID = -2;

    private int _ticks = 0;
    private boolean _syncClientNextTick = true;

    private ArrayList<EntityPlayer> playersUsingGUI = new ArrayList();

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
            Iterator<EntityPlayer> it = playersUsingGUI.iterator();
            while (it.hasNext())
            {
                EntityPlayer player = it.next();
                if (player instanceof EntityPlayerMP && shouldSendGuiPacket((EntityPlayerMP) player))
                {
                    PacketTile packet = new PacketTile("gui", GUI_PACKET_ID, this);
                    List<Object> objects = new ArrayList();
                    writeGuiPacket(objects, player);
                    packet.addData(objects);
                    PacketSystem.INSTANCE.sendToPlayer(packet, (EntityPlayerMP) player);
                }
                else
                {
                    it.remove();
                }
            }
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

    //-----------------------------------------------
    //--------- Network -----------------------------
    //-----------------------------------------------


    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, IPacket type)
    {
        if (isClient())
        {
            if (id == DESC_PACKET_ID)
            {
                readDescPacket(buf, player);
                return true;
            }
            else if (id == GUI_PACKET_ID)
            {
                readGuiPacket(buf, player);
                return true;
            }
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
    protected void writeGuiPacket(List<Object> dataList, EntityPlayer player)
    {

    }

    /**
     * Called to read the packet
     *
     * @param buf    - raw data
     * @param player - player reading the data
     */
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {

    }

    //-----------------------------------------------
    //--------- Helpers -----------------------------
    //-----------------------------------------------

    protected boolean isServer()
    {
        return getWorld() != null && !getWorld().isRemote;
    }

    protected boolean isClient()
    {
        return getWorld() != null && getWorld().isRemote;
    }

    @Override
    public Collection<EntityPlayer> getPlayersUsingGui()
    {
        return playersUsingGUI;
    }

    /**
     * Checks if the player should continue to receive GUI packets
     *
     * @param playerMP - player
     * @return true if should continue to receive packets, false will remove the player from the list
     */
    protected boolean shouldSendGuiPacket(EntityPlayerMP playerMP)
    {
        return playerMP.isEntityAlive() && playerMP.openContainer != null;
    }

    //-----------------------------------------------
    //--------- Position ----------------------------
    //-----------------------------------------------

    @Override
    public World world()
    {
        return getWorld();
    }

    @Override
    public int dim()
    {
        return getWorld().provider.getDimension();
    }

    @Override
    public double z()
    {
        return zi() + 0.5;
    }

    @Override
    public double x()
    {
        return xi() + 0.5;
    }

    @Override
    public double y()
    {
        return yi() + 0.5;
    }

    @Override
    public int zi()
    {
        return pos.getZ();
    }

    @Override
    public int xi()
    {
        return pos.getX();
    }

    @Override
    public int yi()
    {
        return pos.getY();
    }
}
