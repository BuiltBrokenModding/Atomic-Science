package com.builtbroken.atomicscienceclassic.schematic;

import com.builtbroken.atomicscienceclassic.Atomic;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.world.schematic.Schematic;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

public class SchematicFusionReactor extends Schematic
{
    @Override
    public String getName()
    {
        return "schematic.fusionReactor.name";
    }

    @Override
    public HashMap<Pos, Pair<Block, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Pos, Pair<Block, Integer>> returnMap = new HashMap();

        /** Fusion Torus */
        int radius = size + 2;

        for (int x = -radius; x <= radius; x++)
        {
            for (int z = -radius; z <= radius; z++)
            {
                for (int y = 0; y <= size; y++)
                {
                    Pos position = new Pos(x, y, z);
                    double magnitude = Math.sqrt(x * x + z * z);

                    if (!returnMap.containsKey(position))
                    {
                        returnMap.put(position, new Pair(0, 0));
                    }

                    if (magnitude <= radius)
                    {
                        if (y == 0 || y == size)
                        {
                            if (magnitude >= 1)
                            {
                                double yDeviation = (y == 0 ? size / 3 : -size / 3) + (y == 0 ? -1 : 1) * Math.sin(magnitude / radius * Math.PI) * size / 2d;
                                Pos newPos = position.add(0, yDeviation, 0);
                                returnMap.put(newPos.round(), new Pair(Atomic.blockElectromagnet, 1));
                            }
                        }
                        else if (magnitude > radius - 1)
                        {
                            returnMap.put(position, new Pair(Atomic.blockElectromagnet, 0));
                        }
                    }
                }
            }
        }
        /** Fusion Core */
        for (int y = 0; y < size; y++)
        {
            returnMap.put(new Pos(0, y, 0), new Pair(Atomic.blockReactorCell, 0));
            returnMap.put(new Pos(1, y, 0), new Pair(Atomic.blockElectromagnet, 0));
            returnMap.put(new Pos(0, y, 1), new Pair(Atomic.blockElectromagnet, 0));
            returnMap.put(new Pos(0, y, -1), new Pair(Atomic.blockElectromagnet, 0));
            returnMap.put(new Pos(-1, y, 0), new Pair(Atomic.blockElectromagnet, 0));
        }

        returnMap.put(new Pos(0, 0, 0), new Pair(Atomic.blockReactorCell, 0));

        return returnMap;
    }
}
