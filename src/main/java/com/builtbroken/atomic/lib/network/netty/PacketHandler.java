package com.builtbroken.atomic.lib.network.netty;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.lib.network.IPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

/**
 * @author tgame14
 * @since 31/05/14
 */
@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<IPacket>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacket packet) throws Exception
    {
        try
        {
            INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();

            switch (FMLCommonHandler.instance().getEffectiveSide())
            {
                case CLIENT:
                    packet.handleClientSide();
                    break;
                case SERVER:
                    packet.handleServerSide(((NetHandlerPlayServer) netHandler).playerEntity);
                    break;
                default:
                    break;
            }
        }
        catch (Exception e)
        {
            AtomicScience.logger.error("Failed to handle packet " + packet, e);
        }
    }

}
