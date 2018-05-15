package com.builtbroken.atomic.content.steam;

import com.builtbroken.atomic.content.tiles.TileEntityMachine;
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
    boolean checkSteam = true;
    int steam = 0;
    DataPos topMostBlock = DataPos.get(0, 0, 0);

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

    protected void pathDown()
    {
        steam = 0;
        topMostBlock = null;
        topMostBlock.y = -1;

        int y = yCoord - 1;
        Block block;
        do
        {
            block = worldObj.getBlock(xCoord, y, zCoord);
            if (block == Blocks.water || block == Blocks.flowing_water)
            {
                if (topMostBlock.y == -1)
                {
                    topMostBlock.x = xCoord;
                    topMostBlock.y = y;
                    topMostBlock.z = zCoord;
                }
                steam += ThermalHandler.getVaporRate(worldObj, xCoord, yCoord, zCoord);
            }
            else if (!block.isAir(worldObj, xCoord, y, zCoord)) //TODO ignore blocks with small colliders
            {
                break;
            }
        }
        while (y > 0);
    }
}
