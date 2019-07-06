package com.builtbroken.atomic.content.machines.sensors.thermal;

import com.builtbroken.atomic.content.machines.sensors.thermal.gui.ContainerThermalRedstone;
import com.builtbroken.atomic.content.machines.sensors.thermal.gui.GuiThermalRedstone;
import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import com.builtbroken.atomic.lib.MetaEnum;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.map.MapHandler;
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
 *
 * Created by Dark(DarkGuardsman, Robert) on 10/23/2018.
 */
public class TileEntityThermalRedstone extends TileEntityPrefab implements IGuiTile
{
    public static final String NBT_MIN_HEAT = "minHeat";
    public static final String NBT_MAX_HEAT = "maxHeat";

    public static final int TRIGGER_SET_PACKET_ID = 1;
    public static final int GET_HEAT_PACKET_ID = 2;

    public int minHeatTrigger = 1000;
    public int maxHeatTrigger = 1000;

    public int clientHeatValue = -1;

    //TODO set owner of machine

    @Override
    public void onLoad()
    {
        updateRedstoneState(world.getBlockState(getPos()), MapHandler.THERMAL_MAP.getStoredHeat(world, getPos()));
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, IPacket type)
    {
        if (!super.read(buf, id, player, type))
        {
            if (isServer())
            {
                if (id == TRIGGER_SET_PACKET_ID)
                {
                    minHeatTrigger = buf.readInt();
                    maxHeatTrigger = buf.readInt();

                    //TODO send to event queue to prevent packet spawn updating block
                    int heat = MapHandler.THERMAL_MAP.getStoredHeat(world, getPos());
                    updateRedstoneState(world.getBlockState(getPos()), heat);
                    sendGuiPacket();

                    markDirty();

                    return true;
                }
                else if(id == GET_HEAT_PACKET_ID)
                {
                    int heat = MapHandler.THERMAL_MAP.getStoredHeat(world, getPos());
                    PacketSystem.INSTANCE.sendToPlayer(new PacketTile("heat_send", GET_HEAT_PACKET_ID, this).addData(heat), player);
                    return true;
                }
            }
            else if(isClient() && id == GET_HEAT_PACKET_ID)
            {
                clientHeatValue = buf.readInt();
                return true;
            }
            return false;
        }
        return true;
    }

    public void requestHeatValue()
    {
        PacketSystem.INSTANCE.sendToServer(new PacketTile("heat_get", GET_HEAT_PACKET_ID, this));
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
            return (int) Math.min(15, Math.max(0, Math.floor(getHeatScale(heatValue) * 15)));
        }
        return 0;
    }

    public float getHeatScale(int heatValue)
    {
        if (heatValue > minHeatTrigger)
        {
            return Math.max(0, Math.min(1, (heatValue - minHeatTrigger) / (float) (maxHeatTrigger - minHeatTrigger)));
        }
        return 0;
    }

    public void updateRedstoneState(IBlockState blockState, int heatValue)
    {
        int redstone = getExpectedRedstoneValue(heatValue);
        int currentRedstone = BlockThermalRedstone.getRedstoneValue(blockState);
        if (redstone != currentRedstone)
        {
            world.setBlockState(getPos(), blockState.withProperty(BlockThermalRedstone.REDSTONE_PROPERTY, MetaEnum.get(redstone)));
        }
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
