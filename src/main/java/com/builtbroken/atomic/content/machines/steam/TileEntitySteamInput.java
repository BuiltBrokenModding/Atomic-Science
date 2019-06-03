package com.builtbroken.atomic.content.machines.steam;

import com.builtbroken.atomic.content.prefab.TileEntityActive;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
        IBlockState blockState;
        Block block;
        do
        {
            //Get block
            blockState = getState(pos);
            block = blockState.getBlock();

            //Check if block can produce vapor
            if (ThermalHandler.isSupportedVaporFluid(world, pos))
            {
                if (topMostBlock == null)
                {
                    topMostBlock = pos;
                }
                int vaporRate = ThermalHandler.getVaporRate(world, pos);
                steam += vaporRate;
            }
            //Stop if we hit a none-air block
            else if (!ThermalHandler.canSteamPassThrough(world, pos)) //TODO ignore blocks with small colliders
            {
                break;
            }

            pos = pos.down();
        }
        while (pos.getY() > 0);
    }
}
