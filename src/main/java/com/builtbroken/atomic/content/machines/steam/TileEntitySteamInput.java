package com.builtbroken.atomic.content.machines.steam;

import com.builtbroken.atomic.content.prefab.TileEntityActive;
import com.builtbroken.atomic.lib.vapor.VaporHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2018.
 */
public class TileEntitySteamInput extends TileEntityActive
{

    protected boolean checkSteam = true;
    protected int steam = 0;
    protected BlockPos topMostBlock;

    @Override
    protected void update(int ticks, boolean isClient)
    {
        if (!isClient)
        {
            if (ticks % 20 == 0 || checkSteam)
            {
                checkSteam = false;
                pathDown();
            }
        }

        //TODO produce steam effect on top of water
    }

    public int getSteamGeneration()
    {
        return steam;
    }

    protected void pathDown()
    {
        steam = 0;
        topMostBlock = null;

        BlockPos pos = getPos().down();
        do
        {
            //Check if block can produce vapor
            if (VaporHandler.isSupportedVaporFluid(world, pos))
            {
                if (topMostBlock == null)
                {
                    topMostBlock = pos;
                }
                steam += VaporHandler.getVaporRate(world, pos);
            }
            //Stop if we hit a none-air block
            else if (!VaporHandler.canSteamPassThrough(world, pos))
            {
                break;
            }

            pos = pos.down();
        }
        while (pos.getY() > 0);
    }
}
