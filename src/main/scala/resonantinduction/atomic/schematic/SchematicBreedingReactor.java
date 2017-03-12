package resonantinduction.atomic.schematic;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;
import resonant.lib.schematic.Schematic;
import resonant.lib.type.Pair;
import resonantinduction.atomic.Atomic;
import universalelectricity.api.vector.Vector3;

public class SchematicBreedingReactor extends Schematic
{
    @Override
    public String getName()
    {
        return "schematic.breedingReactor.name";
    }

    @Override
    public HashMap<Vector3, Pair<Integer, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Vector3, Pair<Integer, Integer>> returnMap = new HashMap<Vector3, Pair<Integer, Integer>>();

        int r = Math.max(size, 2);

        for (int x = -r; x <= r; x++)
        {
            for (int z = -r; z <= r; z++)
            {
                returnMap.put(new Vector3(x, 0, z), new Pair(Block.waterStill.blockID, 0));
            }
        }

        r--;

        for (int x = -r; x <= r; x++)
        {
            for (int z = -r; z <= r; z++)
            {
                Vector3 targetPosition = new Vector3(x, 1, z);

                if (new Vector3(x, 0, z).getMagnitude() <= 2)
                {
                    if (!((x == -r || x == r) && (z == -r || z == r)))
                    {
                        returnMap.put(new Vector3(x, 0, z), new Pair(Atomic.blockReactorCell.blockID, 0));
                        returnMap.put(new Vector3(x, -1, z), new Pair(Atomic.blockThermometer.blockID, 0));
                        returnMap.put(new Vector3(x, -3, z), new Pair(Atomic.blockSiren.blockID, 0));
                        returnMap.put(new Vector3(x, -2, z), new Pair(Block.redstoneWire.blockID, 0));

                    }
                    else
                    {
                        returnMap.put(new Vector3(x, -1, z), new Pair(Atomic.blockControlRod.blockID, 0));
                        returnMap.put(new Vector3(x, -2, z), new Pair(Block.pistonStickyBase.blockID, 1));
                    }
                }
            }
        }

        returnMap.put(new Vector3(0, -2, 0), new Pair(Block.stone.blockID, 0));
        returnMap.put(new Vector3(0, -3, 0), new Pair(Block.stone.blockID, 0));
        returnMap.put(new Vector3(), new Pair(Atomic.blockReactorCell.blockID, 0));
        return returnMap;
    }
}
