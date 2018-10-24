package com.builtbroken.atomic.content.machines.sensors.thermal;

import com.builtbroken.atomic.content.machines.sensors.thermal.gui.ContainerThermalRedstone;
import com.builtbroken.atomic.content.machines.sensors.thermal.gui.GuiThermalRedstone;
import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.network.IPacket;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.PacketTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/23/2018.
 */
public class TileEntityThermalRedstone extends TileEntityPrefab implements IGuiTile
{
    public static final String NBT_MIN_HEAT = "minHeat";
    public static final String NBT_MAX_HEAT = "maxHeat";

    public static final int TRIGGER_SET_PACKET_ID = 1;

    public int minHeatTrigger = 100;
    public int maxHeatTrigger = 100;

    //TODO set owner of machine

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, IPacket type)
    {
        if (!super.read(buf, id, player, type))
        {
            if (isServer() && id == TRIGGER_SET_PACKET_ID)
            {
                minHeatTrigger = buf.readInt();
                maxHeatTrigger = buf.readInt();
            }
            return false;
        }
        return true;
    }

    public void setTriggerClient(int min, int max)
    {
        this.minHeatTrigger = min;
        this.maxHeatTrigger = max;
        PacketSystem.INSTANCE.sendToServer(new PacketTile("trigger_set", TRIGGER_SET_PACKET_ID, this).addData(min, max));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        minHeatTrigger = compound.getInteger(NBT_MIN_HEAT);
        maxHeatTrigger = compound.getInteger(NBT_MAX_HEAT);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger(NBT_MIN_HEAT, minHeatTrigger);
        compound.setInteger(NBT_MAX_HEAT, maxHeatTrigger);
        return super.writeToNBT(compound);
    }

    @Override
    protected void writeGuiPacket(List<Object> dataList, EntityPlayer player)
    {
        dataList.add(minHeatTrigger);
        dataList.add(maxHeatTrigger);
    }

    @Override
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {
        minHeatTrigger = buf.readInt();
        maxHeatTrigger = buf.readInt();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    public int getExpectedRedstoneValue(int heatValue)
    {
        if (heatValue > minHeatTrigger)
        {
            float scale = (heatValue - minHeatTrigger) / (float) (maxHeatTrigger - minHeatTrigger);
            return (int) Math.min(15, Math.max(0, Math.floor(scale * 15)));
        }
        return 0;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player)
    {
        return new ContainerThermalRedstone(player, this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiThermalRedstone(player, this);
    }
}
