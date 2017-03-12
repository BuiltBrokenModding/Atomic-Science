package resonantinduction.atomic.schematic;

import java.util.HashMap;

import net.minecraftforge.common.ForgeDirection;
import resonant.lib.schematic.Schematic;
import resonant.lib.type.Pair;
import resonantinduction.atomic.Atomic;
import universalelectricity.api.vector.Vector3;

public class SchematicFusionReactor extends Schematic
{
    @Override
    public String getName()
    {
        return "schematic.fusionReactor.name";
    }

    @Override
    public HashMap<Vector3, Pair<Integer, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Vector3, Pair<Integer, Integer>> returnMap = new HashMap<Vector3, Pair<Integer, Integer>>();

        /** Fusion Torus */
        int radius = size + 2;

        for (int x = -radius; x <= radius; x++)
        {
            for (int z = -radius; z <= radius; z++)
            {
                for (int y = 0; y <= size; y++)
                {
                    Vector3 position = new Vector3(x, y, z);
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
                                Vector3 newPos = position.clone().translate(0, yDeviation, 0);
                                returnMap.put(newPos.round(), new Pair(Atomic.blockElectromagnet.blockID, 1));
                            }
                        }
                        else if (magnitude > radius - 1)
                        {
                            returnMap.put(position, new Pair(Atomic.blockElectromagnet.blockID, 0));
                        }
                    }
                }
            }
        }
        /** Fusion Core */
        for (int y = 0; y < size; y++)
        {
            returnMap.put(new Vector3(0, y, 0), new Pair(Atomic.blockReactorCell.blockID, 0));
            returnMap.put(new Vector3(1, y, 0), new Pair(Atomic.blockElectromagnet.blockID, 0));
            returnMap.put(new Vector3(0, y, 1), new Pair(Atomic.blockElectromagnet.blockID, 0));
            returnMap.put(new Vector3(0, y, -1), new Pair(Atomic.blockElectromagnet.blockID, 0));
            returnMap.put(new Vector3(-1, y, 0), new Pair(Atomic.blockElectromagnet.blockID, 0));
        }

        returnMap.put(new Vector3(0, 0, 0), new Pair(Atomic.blockReactorCell.blockID, 0));

        return returnMap;
    }
}
