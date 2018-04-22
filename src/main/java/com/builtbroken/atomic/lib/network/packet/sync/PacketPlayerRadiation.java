package com.builtbroken.atomic.lib.network.packet.sync;

import com.builtbroken.atomic.ClientProxy;
import com.builtbroken.atomic.lib.network.IPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Syncs radiation data for the player
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2018.
 */
public class PacketPlayerRadiation implements IPacket<PacketPlayerRadiation>
{
    public float rads;
    public float rads_area;

    public PacketPlayerRadiation()
    {
        //Empty for default packet handling
    }

    public PacketPlayerRadiation(float rads, float rads_area)
    {
        this.rads = rads;
        this.rads_area = rads_area;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeFloat(rads);
        buffer.writeFloat(rads_area);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        rads = buffer.readFloat();
        rads_area = buffer.readFloat();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer player)
    {
        ClientProxy.RAD_EXPOSURE = rads_area;
        ClientProxy.RAD_PLAYER = rads;
    }
}
