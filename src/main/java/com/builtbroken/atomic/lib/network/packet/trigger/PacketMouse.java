package com.builtbroken.atomic.lib.network.packet.trigger;

import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.lib.network.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/29/2018.
 */
public class PacketMouse implements IPacket
{
    int slot;
    boolean ctrl;
    boolean forward;

    public PacketMouse()
    {
        //empty for packet system
    }

    public PacketMouse(int slot, boolean ctrl, boolean forward)
    {
        this.slot = slot;
        this.ctrl = ctrl;
        this.forward = forward;
    }


    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(slot);
        buffer.writeBoolean(forward);
        buffer.writeBoolean(ctrl);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        slot = buffer.readInt();
        forward = buffer.readBoolean();
        ctrl = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (stack.getItem() == ASItems.itemWrench) //TODO add interface when more than wrench use
        {
            ASItems.itemWrench.handleMouseWheelAction(stack, player, ctrl, forward);
        }
    }
}
