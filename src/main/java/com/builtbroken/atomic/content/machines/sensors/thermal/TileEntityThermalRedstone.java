package com.builtbroken.atomic.content.machines.sensors.thermal;

import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/23/2018.
 */
public class TileEntityThermalRedstone extends TileEntityPrefab
{
    public static final String NBT_MIN_HEAT = "minHeat";
    public static final String NBT_MAX_HEAT = "maxHeat";

    public int minHeatTrigger = 100;
    public int maxHeatTrigger = 100;

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
}
