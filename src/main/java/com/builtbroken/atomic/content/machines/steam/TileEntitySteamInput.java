package com.builtbroken.atomic.content.machines.steam;

import com.builtbroken.atomic.content.machines.TileEntityMachine;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.data.DataPos;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2018.
 */
public class TileEntitySteamInput extends TileEntityMachine
{
    protected boolean checkSteam = true;
    protected int steam = 0;
    protected final DataPos topMostBlock = DataPos.get(0, 0, 0);

    @Override
    protected void update(int ticks)
    {
        if (ticks % 20 == 0 || checkSteam)
        {
            checkSteam = false;
            pathDown();
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
        topMostBlock.y = -1;

        int y = yCoord - 1;
        Block block;
        do
        {
            //Get block
            block = worldObj.getBlock(xCoord, y, zCoord);

            //Check if block can produce vapor
            if (block == Blocks.water || block == Blocks.flowing_water)
            {
                if (topMostBlock.y == -1)
                {
                    topMostBlock.x = xCoord;
                    topMostBlock.y = y;
                    topMostBlock.z = zCoord;
                }
                steam += ThermalHandler.getVaporRate(worldObj, xCoord, y, zCoord);
            }
            //Stop if we hit a none-air block
            else if (!block.isAir(worldObj, xCoord, y, zCoord)) //TODO ignore blocks with small colliders
            {
                break;
            }
        }
        while (y-- > 0);
    }
}
