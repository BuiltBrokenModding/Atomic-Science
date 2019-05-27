package com.builtbroken.atomic.network.packet.client;

import com.builtbroken.atomic.CommonProxy;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.network.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/14/2019.
 */
public class PacketAcceleratorParticleSync implements IPacket
{
    public UUID ID;
    public int dim;
    public float cx, cy, cz;
    public float speed;

    public ItemStack renderItem = ItemStack.EMPTY;

    public PacketAcceleratorParticleSync()
    {

    }

    public PacketAcceleratorParticleSync(AcceleratorParticle particle)
    {
        ID = particle.unique_id;
        dim = particle.dim();
        cx = particle.xf();
        cy = particle.yf();
        cz = particle.zf();
        speed = particle.getVelocity();
        renderItem = particle.getItem();
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeLong(ID.getMostSignificantBits());
        buffer.writeLong(ID.getLeastSignificantBits());
        buffer.writeInt(dim);
        buffer.writeFloat(cx);
        buffer.writeFloat(cy);
        buffer.writeFloat(cz);
        buffer.writeFloat(speed);
        ByteBufUtils.writeItemStack(buffer, renderItem);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        ID = new UUID(buffer.readLong(), buffer.readLong());
        dim = buffer.readInt();
        cx = buffer.readFloat();
        cy = buffer.readFloat();
        cz = buffer.readFloat();
        speed = buffer.readFloat();
        renderItem = ByteBufUtils.readItemStack(buffer);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClientSide(EntityPlayer player)
    {
        CommonProxy.NEW_PARTICLE_PACKETS.add(this);
    }
}
