package com.builtbroken.atomic.schematic;

import com.builtbroken.atomic.Atomic;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.world.schematic.Schematic;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

public class SchematicAccelerator extends Schematic
{
    @Override
    public String getName()
    {
        return "schematic.accelerator.name";
    }

    @Override
    public HashMap<Pos, Pair<Block, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Pos, Pair<Block, Integer>> returnMap = new HashMap();

        int r = size;

        for (int x = -r; x < r; x++)
        {
            for (int z = -r; z < r; z++)
            {
                for (int y = -1; y <= 1; y++)
                {
                    if (x == -r || x == r - 1 || z == -r || z == r - 1)
                    {
                        returnMap.put(new Pos(x, y, z), new Pair(Atomic.blockElectromagnet, 0));
                    }
                }
            }
        }

        r = size - 2;

        for (int x = -r; x < r; x++)
        {
            for (int z = -r; z < r; z++)
            {
                for (int y = -1; y <= 1; y++)
                {
                    if (x == -r || x == r - 1 || z == -r || z == r - 1)
                    {
                        returnMap.put(new Pos(x, y, z), new Pair(Atomic.blockElectromagnet, 0));
                    }
                }
            }
        }

        r = size - 1;

        for (int x = -r; x < r; x++)
        {
            for (int z = -r; z < r; z++)
            {
                for (int y = -1; y <= 1; y++)
                {
                    if (x == -r || x == r - 1 || z == -r || z == r - 1)
                    {
                        if (y == -1 || y == 1)
                        {
                            returnMap.put(new Pos(x, y, z), new Pair(Atomic.blockElectromagnet, 1));
                        }
                        else if (y == 0)
                        {
                            returnMap.put(new Pos(x, y, z), new Pair(0, 0));
                        }
                    }
                }
            }
        }

        return returnMap;
    }
}
