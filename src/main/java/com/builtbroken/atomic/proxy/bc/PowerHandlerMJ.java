package com.builtbroken.atomic.proxy.bc;

import buildcraft.api.mj.IMjReceiver;
import buildcraft.api.mj.MjAPI;
import com.builtbroken.atomic.config.mods.ConfigMod;
import com.builtbroken.atomic.lib.power.PowerHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/20/2018.
 */
public class PowerHandlerMJ extends PowerHandler
{
    @Override
    public boolean canHandle(EnumFacing side, TileEntity tile)
    {
        return tile.hasCapability(MjAPI.CAP_RECEIVER, side);
    }

    @Override
    public int addPower(EnumFacing enumFacing, TileEntity target, int power, boolean doAction)
    {
        //Check that we support output for side
        if (ConfigMod.BUILDCRAFT.ENABLE_BUILDCRAFT)
        {
            //Check that target can receive energy
            if (target.hasCapability(MjAPI.CAP_RECEIVER, enumFacing))
            {
                IMjReceiver receiver = target.getCapability(MjAPI.CAP_RECEIVER, enumFacing);
                if (receiver != null && receiver.canReceive())
                {
                    long request = receiver.getPowerRequested();
                    if (request > 0)
                    {
                        //Convert power input to BC and get smallest value
                        long insert = (int) Math.floor(toBuildcraftEnergy(power));
                        insert = Math.min(insert, request);

                        //insert
                        long leftOver = receiver.receivePower(insert, !doAction);

                        //Get energy taken
                        long taken = insert - leftOver;

                        //Return converted value
                        return (int) Math.ceil(toForgeEnergy(taken));
                    }
                }
            }
        }
        return 0;
    }


    public static double toBuildcraftEnergy(int fe)
    {
        return (fe / ConfigMod.BUILDCRAFT.FE_PER_MJ) * MjAPI.MJ;
    }

    public static double toForgeEnergy(long mj)
    {
        return (mj / (double) MjAPI.MJ) * ConfigMod.BUILDCRAFT.FE_PER_MJ;
    }
}