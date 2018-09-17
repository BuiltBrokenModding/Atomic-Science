package com.builtbroken.atomic.network.packet;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.lib.transform.vector.Location;
import com.builtbroken.atomic.network.IPacketIDReceiver;
import com.builtbroken.atomic.network.ex.PacketTileReadException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Packet type designed to be used with Tiles
 *
 * @author tgame14
 * @since 26/05/14
 */
public class PacketTile extends PacketBase
{
    public int dim;
    public BlockPos pos;
    public int id;

    public String name;

    public PacketTile()
    {
        //Needed for forge to construct the packet
    }

    /**
     * @param pos - location
     */
    public PacketTile(String name, int id, int dim, BlockPos pos)
    {
        this.dim = dim;
        this.name = name;
        this.id = id;
        this.pos = pos;
    }

    /**
     * @param tile - TileEntity to send this packet to, only used for location data
     */
    public PacketTile(String name, int id, TileEntity tile)
    {
        this(name, id, tile.getWorld().provider.getDimension(), tile.getPos());
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeBoolean(AtomicScience.runningAsDev);
        if (AtomicScience.runningAsDev)
        {
            ByteBufUtils.writeUTF8String(buffer, name);
        }
        buffer.writeInt(id);
        buffer.writeInt(dim);
        buffer.writeInt(pos.getX());
        buffer.writeInt(pos.getY());
        buffer.writeInt(pos.getZ());
        super.encodeInto(ctx, buffer);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        if (buffer.readBoolean())
        {
            name = ByteBufUtils.readUTF8String(buffer);
        }
        id = buffer.readInt();
        dim = buffer.readInt();
        pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        super.decodeInto(ctx, buffer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer player)
    {
        if (player != null)
        {
            handle(player);
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error("PacketTile#handleClientSide(null) - player was null for packet", new RuntimeException());
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        if (player != null)
        {
            handle(player);
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error("PacketTile#handleServerSide(null) - player was null for packet", new RuntimeException());
        }
    }

    /**
     * Called to handle a packet when it is received
     *
     * @param player - player who received the packet
     */
    public void handle(EntityPlayer player)
    {
        if (player.getEntityWorld() == null)
        {
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error("PacketTile#handle(" + player + ", " + name  + ") - world is null for player while handling packet. ", new RuntimeException());
            }
            return;
        }
        if (player.getEntityWorld().provider.getDimension() != dim)
        {
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error("PacketTile#handle(" + player + ", " + name  + ") - packet received on wrong dim. Current[" + player.getEntityWorld().provider.getDimension() + "] Expected[" + dim + "]");
            }
        }
        else if (player.getEntityWorld().isBlockLoaded(pos, false))
        {
            handle(player, player.getEntityWorld().getTileEntity(pos));
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error("PacketTile#handle(" + player + ", " + name + ") - block is not loaded for player while handling packet. ");
        }
    }

    /**
     * Called to handler a packet when it is received
     *
     * @param player - player who received the packet
     * @param tile   - tile who is receiving the packet
     */
    public void handle(EntityPlayer player, TileEntity tile)
    {
        //TODO add checksum or hash to verify the packet is sent to the correct tile
        final Location location = new Location(player.world, pos);
        if (tile == null)
        {
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error(new PacketTileReadException(location, "Null tile, PacketName: " + name));
            }
        }
        else if (tile.isInvalid())
        {
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error(new PacketTileReadException(location, "Invalidated tile, PacketName: " + name));
            }
        }
        else if (tile instanceof IPacketIDReceiver)
        {
            if (((IPacketIDReceiver) tile).shouldReadPacket(player, location, this))
            {
                try
                {
                    IPacketIDReceiver receiver = (IPacketIDReceiver) tile;
                    receiver.read(dataToRead, id, player, this);
                }
                catch (IndexOutOfBoundsException e)
                {
                    AtomicScience.logger.error(new PacketTileReadException(location, "Packet was read past it's size, PacketName: " + name));
                    AtomicScience.logger.error("Error: ", e);
                }
                catch (NullPointerException e)
                {
                    AtomicScience.logger.error(new PacketTileReadException(location, "Null pointer while reading data, PacketName: " + name, e));
                    AtomicScience.logger.error("Error: ", e);
                }
                catch (Exception e)
                {
                    AtomicScience.logger.error(new PacketTileReadException(location, "Failed to read packet, PacketName: " + name, e));
                    AtomicScience.logger.error("Error: ", e);
                }
            }
            else if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error("Error: " + tile + " rejected packet " + this + " due to invalid conditions, PacketName: " + name);
            }
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error(new PacketTileReadException(location, "Unsupported action for " + tile + ", PacketName: " + name));
        }
    }
}
