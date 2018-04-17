package com.builtbroken.atomic.schematic;

import com.builtbroken.atomic.Atomic;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.world.schematic.Schematic;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

public class SchematicBreedingReactor extends Schematic
{
    @Override
    public String getName()
    {
        return "schematic.breedingReactor.name";
    }

    @Override
    public HashMap<Pos, Pair<Block, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Pos, Pair<Block, Integer>> returnMap = new HashMap();

        int r = Math.max(size, 2);

        for (int x = -r; x <= r; x++)
        {
            for (int z = -r; z <= r; z++)
            {
                returnMap.put(new Pos(x, 0, z), new Pair(Blocks.water, 0));
            }
        }

        r--;

        for (int x = -r; x <= r; x++)
        {
            for (int z = -r; z <= r; z++)
            {
                Pos targetPosition = new Pos(x, 1, z);

                if (new Pos(x, 0, z).magnitude() <= 2)
                {
                    if (!((x == -r || x == r) && (z == -r || z == r)))
                    {
                        returnMap.put(new Pos(x, 0, z), new Pair(Atomic.blockReactorCell, 0));
                        returnMap.put(new Pos(x, -1, z), new Pair(Atomic.blockThermometer, 0));
                        returnMap.put(new Pos(x, -3, z), new Pair(Atomic.blockSiren, 0));
                        returnMap.put(new Pos(x, -2, z), new Pair(Blocks.redstone_wire, 0));

                    }
                    else
                    {
                        returnMap.put(new Pos(x, -1, z), new Pair(Atomic.blockControlRod, 0));
                        returnMap.put(new Pos(x, -2, z), new Pair(Blocks.piston, 1));
                    }
                }
            }
        }

        returnMap.put(new Pos(0, -2, 0), new Pair(Blocks.stone, 0));
        returnMap.put(new Pos(0, -3, 0), new Pair(Blocks.stone, 0));
        returnMap.put(new Pos(), new Pair(Atomic.blockReactorCell, 0));
        return returnMap;
    }
}
