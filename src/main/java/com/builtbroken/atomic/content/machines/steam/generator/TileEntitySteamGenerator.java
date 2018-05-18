package com.builtbroken.atomic.content.machines.steam.generator;

import com.builtbroken.atomic.content.machines.steam.TileEntitySteamInput;
import com.builtbroken.atomic.lib.power.PowerSystem;
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
    @Override
    public void firstTick()
    {
        super.firstTick();
    }

    @Override
    protected void update(int ticks)
    {
        super.update(ticks);
        PowerSystem.outputPower(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UP, getPowerToOutput(), true);
    }

    public int getPowerToOutput()
    {
        return 0;
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
