package com.builtbroken.atomic.content.machines.steam.generator;

import com.builtbroken.atomic.config.ConfigPower;
import com.builtbroken.atomic.content.machines.steam.TileEntitySteamInput;
import com.builtbroken.atomic.lib.power.PowerSystem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Simple tile to convert steam flow rate into power
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class TileEntitySteamGenerator extends TileEntitySteamInput
{
    public float _clientTurbineRotation = 0;
    public float _clientPrevRotation = 0;

    @Override
    public void firstTick()
    {
        super.firstTick();
    }

    @Override
    protected void update(int ticks)
    {
        super.update(ticks);
        if (isClient())
        {
            _clientTurbineRotation += getRotationSpeed();
            if(_clientTurbineRotation > 360)
            {
                _clientTurbineRotation -= 360f;
                _clientPrevRotation -= 360f;
            }
        }
        PowerSystem.outputPower(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UP, getPowerToOutput(), true);
    }

    public float within180(float rotation)
    {
        if (rotation > 360)
        {
            rotation -= 360;
        }
        return rotation;
    }

    @SideOnly(Side.CLIENT)
    public float rotate(float delta)
    {
        _clientPrevRotation = _clientPrevRotation + (_clientTurbineRotation - _clientPrevRotation) * delta;
        return _clientPrevRotation;
    }

    public float getRotationSpeed()
    {
        return (getSteamGeneration() / 1000f) * 10;
    }

    public int getPowerToOutput()
    {
        return getSteamGeneration() * ConfigPower.STEAM_TO_UE_POWER;
    }

    //-------------------------------------------------
    //-----Data handling ------------------------------
    //-------------------------------------------------

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
    }
}
