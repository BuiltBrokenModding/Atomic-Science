package resonantinduction.atomic.schematic;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;
import resonant.lib.schematic.Schematic;
import resonant.lib.type.Pair;
import resonantinduction.atomic.Atomic;
import universalelectricity.api.vector.Vector3;

public class SchematicFissionReactor extends Schematic
{
    @Override
    public String getName()
    {
        return "schematic.fissionReactor.name";
    }

    @Override
    public HashMap<Vector3, Pair<Integer, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Vector3, Pair<Integer, Integer>> returnMap = new HashMap<Vector3, Pair<Integer, Integer>>();

        if (size <= 1)
        {
            int r = 2;

            for (int x = -r; x <= r; x++)
            {
                for (int z = -r; z <= r; z++)
                {
                    Vector3 targetPosition = new Vector3(x, 0, z);
                    returnMap.put(targetPosition, new Pair(Block.waterStill.blockID, 0));
                }
            }

            r -= 1;

            /** Create turbines and control rods */
            for (int x = -r; x <= r; x++)
            {
                for (int z = -r; z <= r; z++)
                {
                    Vector3 targetPosition = new Vector3(x, 1, z);
                    returnMap.put(targetPosition, new Pair(Atomic.blockElectricTurbine.blockID, 0));

                    if (!((x == -r || x == r) && (z == -r || z == r)) && new Vector3(x, 0, z).getMagnitude() <= 1)
                    {
                        returnMap.put(new Vector3(x, -1, z), new Pair(Atomic.blockControlRod.blockID, 0));
                        returnMap.put(new Vector3(x, -2, z), new Pair(Block.pistonStickyBase.blockID, 1));
                    }
                }
            }

            returnMap.put(new Vector3(0, -1, 0), new Pair(Atomic.blockThermometer.blockID, 0));
            // TODO: IF Siren is a Tile, don't do this. Redstone can't hold it.
            returnMap.put(new Vector3(0, -3, 0), new Pair(Atomic.blockSiren.blockID, 0));
            returnMap.put(new Vector3(0, -2, 0), new Pair(Block.redstoneWire.blockID, 0));
            returnMap.put(new Vector3(), new Pair(Atomic.blockReactorCell.blockID, 0));
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
                        Vector3 targetPosition = new Vector3(x, y, z);
                        Vector3 leveledPosition = new Vector3(0, y, 0);

                        if (y < size - 1)
                        {
                            if (targetPosition.distance(leveledPosition) == 2)
                            {
                                returnMap.put(targetPosition, new Pair(Atomic.blockControlRod.blockID, 0));

                                /** Place piston base to push control rods in. */
                                int rotationMetadata = 0;
                                Vector3 offset = new Vector3(x, 0, z).normalize();

                                for (ForgeDirection checkDir : ForgeDirection.VALID_DIRECTIONS)
                                {
                                    if (offset.x == checkDir.offsetX && offset.y == checkDir.offsetY && offset.z == checkDir.offsetZ)
                                    {
                                        rotationMetadata = checkDir.getOpposite().ordinal();
                                        break;
                                    }
                                }

                                returnMap.put(targetPosition.clone().translate(offset), new Pair(Block.pistonStickyBase.blockID, rotationMetadata));
                            }
                            else if (x == -r || x == r || z == -r || z == r)
                            {
                                returnMap.put(targetPosition, new Pair(Block.glass.blockID, 0));

                            }
                            else if (x == 0 && z == 0)
                            {
                                returnMap.put(targetPosition, new Pair(Atomic.blockReactorCell.blockID, 0));
                            }
                            else
                            {
                                returnMap.put(targetPosition, new Pair(Block.waterMoving.blockID, 0));
                            }
                        }
                        else if (targetPosition.distance(leveledPosition) < 2)
                        {
                            returnMap.put(targetPosition, new Pair(Atomic.blockElectricTurbine.blockID, 0));
                        }
                    }
                }
            }
        }

        return returnMap;
    }
}
