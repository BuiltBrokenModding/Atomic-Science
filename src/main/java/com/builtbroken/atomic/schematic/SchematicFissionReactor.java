package com.builtbroken.atomic.schematic;

import com.builtbroken.atomic.Atomic;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.world.schematic.Schematic;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

public class SchematicFissionReactor extends Schematic
{
    @Override
    public String getName()
    {
        return "schematic.fissionReactor.name";
    }

    @Override
    public HashMap<Pos, Pair<Block, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Pos, Pair<Block, Integer>> returnMap = new HashMap();

        if (size <= 1)
        {
            int r = 2;

            for (int x = -r; x <= r; x++)
            {
                for (int z = -r; z <= r; z++)
                {
                    Pos targetPosition = new Pos(x, 0, z);
                    returnMap.put(targetPosition, new Pair(Blocks.water, 0));
                }
            }

            r -= 1;

            /** Create turbines and control rods */
            for (int x = -r; x <= r; x++)
            {
                for (int z = -r; z <= r; z++)
                {
                    Pos targetPosition = new Pos(x, 1, z);
                    returnMap.put(targetPosition, new Pair(Atomic.blockElectricTurbine, 0));

                    if (!((x == -r || x == r) && (z == -r || z == r)) && new Pos(x, 0, z).magnitude() <= 1)
                    {
                        returnMap.put(new Pos(x, -1, z), new Pair(Atomic.blockControlRod, 0));
                        returnMap.put(new Pos(x, -2, z), new Pair(Blocks.piston, 1));
                    }
                }
            }

            returnMap.put(new Pos(0, -1, 0), new Pair(Atomic.blockThermometer, 0));
            // TODO: IF Siren is a Tile, don't do this. Redstone can't hold it.
            returnMap.put(new Pos(0, -3, 0), new Pair(Atomic.blockSiren, 0));
            returnMap.put(new Pos(0, -2, 0), new Pair(Blocks.redstone_wire, 0));
            returnMap.put(new Pos(), new Pair(Atomic.blockReactorCell, 0));
        }
        else
        {
            int r = 2;

            for (int y = 0; y < size; y++)
            {
                for (int x = -r; x <= r; x++)
                {
                    for (int z = -r; z <= r; z++)
                    {
                        Pos targetPosition = new Pos(x, y, z);
                        Pos leveledPosition = new Pos(0, y, 0);

                        if (y < size - 1)
                        {
                            if (targetPosition.distance(leveledPosition) == 2)
                            {
                                returnMap.put(targetPosition, new Pair(Atomic.blockControlRod, 0));

                                /** Place piston base to push control rods in. */
                                int rotationMetadata = 0;
                                Pos offset = new Pos(x, 0, z).normalize();

                                for (ForgeDirection checkDir : ForgeDirection.VALID_DIRECTIONS)
                                {
                                    if (offset.xi() == checkDir.offsetX && offset.yi() == checkDir.offsetY && offset.zi() == checkDir.offsetZ)
                                    {
                                        rotationMetadata = checkDir.getOpposite().ordinal();
                                        break;
                                    }
                                }

                                returnMap.put(targetPosition.add(offset), new Pair(Blocks.sticky_piston, rotationMetadata));
                            }
                            else if (x == -r || x == r || z == -r || z == r)
                            {
                                returnMap.put(targetPosition, new Pair(Blocks.glass, 0));

                            }
                            else if (x == 0 && z == 0)
                            {
                                returnMap.put(targetPosition, new Pair(Atomic.blockReactorCell, 0));
                            }
                            else
                            {
                                returnMap.put(targetPosition, new Pair(Blocks.flowing_water, 0));
                            }
                        }
                        else if (targetPosition.distance(leveledPosition) < 2)
                        {
                            returnMap.put(targetPosition, new Pair(Atomic.blockElectricTurbine, 0));
                        }
                    }
                }
            }
        }

        return returnMap;
    }
}
