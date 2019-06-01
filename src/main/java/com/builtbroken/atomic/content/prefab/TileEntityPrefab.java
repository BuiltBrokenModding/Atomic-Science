package com.builtbroken.atomic.content.prefab;

import com.builtbroken.atomic.lib.gui.IPlayerUsing;
import com.builtbroken.atomic.lib.transform.IPosWorld;
import com.builtbroken.atomic.network.IPacket;
import com.builtbroken.atomic.network.IPacketIDReceiver;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.PacketTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public abstract class TileEntityPrefab extends TileEntity implements IPacketIDReceiver, IPosWorld, IPlayerUsing
{

    public static final int DESC_PACKET_ID = -1;
    public static final int GUI_PACKET_ID = -2;

    private final ArrayList<EntityPlayer> playersUsingGUI = new ArrayList();

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
     * Sends the description packet to the client
     */
    protected void sendDescPacket()
    {
        if(isServer())
        {
            PacketTile packetTile = new PacketTile("reactor_desc", DESC_PACKET_ID, this);

            //Collect data
            List<Object> list = new ArrayList();
            writeDescPacket(list, null); //TODO get list of players, then send
            packetTile.addData(list);

            PacketSystem.INSTANCE.sendToAllAround(packetTile, this);
        }
    }

    protected void sendGuiPacket()
    {
        Iterator<EntityPlayer> it = getPlayersUsingGui().iterator();
        while (it.hasNext())
        {
            final EntityPlayer player = it.next();
            if (!sendGuiPacket(player))
            {
                it.remove();
            }
        }
    }

    public boolean sendGuiPacket(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP && shouldSendGuiPacket((EntityPlayerMP) player))
        {
            PacketTile packet = new PacketTile("gui", GUI_PACKET_ID, this);

            List<Object> objects = new ArrayList();
            writeGuiPacket(objects, player);
            packet.addData(objects);

            PacketSystem.INSTANCE.sendToPlayer(packet, (EntityPlayerMP) player);
            return true;
        }
        return false;
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

    public final boolean isServer()
    {
        return getWorld() != null && !getWorld().isRemote;
    }

    public final boolean isClient()
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
        if (getWorld() == null)
        {
            return 0;
        }
        else if (getWorld().provider == null)
        {
            return 0;
        }
        return getWorld().provider.getDimension();
    }

    public String worldName()
    {
        if (getWorld() == null)
        {
            return "-null-";
        }
        else if (getWorld().getWorldInfo() == null)
        {
            return "-null info-";
        }
        return getWorld().getWorldInfo().getWorldName();
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

    //-----------------------------------------------
    //---------- Helpers ----------------------------
    //-----------------------------------------------

    public TileEntity getTileEntity(BlockPos pos)
    {
        return world != null ? world.getTileEntity(pos) : null;
    }

    public TileEntity getTileEntityIfLoaded(BlockPos pos)
    {
        return world != null && world.isBlockLoaded(pos) ? getTileEntity(pos) : null;
    }

    public TileEntity getTileEntity(BlockPos pos, EnumFacing side)
    {
        return getTileEntity(pos.offset(side));
    }

    public TileEntity getTileEntity(EnumFacing side)
    {
        return getTileEntity(getPos().offset(side));
    }

    public <T> T getCapabilityOnSide(Capability<T> cap, EnumFacing side)
    {
        final TileEntity tileEntity = getTileEntity(side);
        if(tileEntity != null && tileEntity.hasCapability(cap, side.getOpposite()))
        {
            return tileEntity.getCapability(cap, side.getOpposite());
        }

        return null;
    }

    public IBlockState getState(BlockPos pos)
    {
        return world != null ? world.getBlockState(pos) : null;
    }

    public IBlockState getState()
    {
        return getState(getPos());
    }

    public IBlockState getState(BlockPos pos, EnumFacing side)
    {
        return getState(pos.offset(side));
    }

    public IBlockState getState(EnumFacing side)
    {
        return getState(getPos(), side);
    }

    /**
     * Gets the direction from the block state or other data
     * @return direction, or UP if not supported
     */
    public EnumFacing getDirection()
    {
        final IBlockState state = world.getBlockState(getPos());
        if(state.getPropertyKeys().contains(BlockMachine.ROTATION_PROP))
        {
            final EnumFacing facing = state.getValue(BlockMachine.ROTATION_PROP);
            if(facing != null)
            {
                return facing;
            }
        }
        return EnumFacing.UP;
    }

    public void setDirection(EnumFacing facing)
    {
        if(getDirection() != facing)
        {
            IBlockState state = world.getBlockState(getPos());
            if (state.getPropertyKeys().contains(BlockMachine.ROTATION_PROP))
            {
                state = state.withProperty(BlockMachine.ROTATION_PROP, facing);
                world.setBlockState(getPos(), state);
            }
        }
    }

    /**
     * Called when the direction changes
     * @param direction
     */
    public void onDirectionChanged(EnumFacing direction)
    {

    }

}
