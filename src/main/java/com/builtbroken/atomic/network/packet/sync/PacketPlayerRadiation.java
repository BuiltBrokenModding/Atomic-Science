package com.builtbroken.atomic.network.packet.sync;

import com.builtbroken.atomic.client.ClientProxy;
import com.builtbroken.atomic.network.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Syncs radiation data for the player
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2018.
 */
public class PacketPlayerRadiation implements IPacket
{
    public float rads;
    public float rads_area;
    public int rad_remove_timer;

    public PacketPlayerRadiation()
    {
        //Empty for default packet handling
    }

    public PacketPlayerRadiation(float rads, float rads_area, int rad_remove_timer)
    {
        this.rads = rads;
        this.rads_area = rads_area;
        this.rad_remove_timer = rad_remove_timer;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeFloat(rads);
        buffer.writeFloat(rads_area);
        buffer.writeInt(rad_remove_timer);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        rads = buffer.readFloat();
        rads_area = buffer.readFloat();
        rad_remove_timer = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        ClientProxy.RAD_EXPOSURE = rads_area;
        ClientProxy.RAD_PLAYER = rads;
        ClientProxy.RAD_REMOVE_TIMER = rad_remove_timer;
    }
}
