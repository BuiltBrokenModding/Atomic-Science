package com.builtbroken.atomic.content.machines.accelerator.graph;

import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2019.
 */
public class AcceleratorDebug
{
    public static void printNetwork(AcceleratorNetwork network)
    {
        final Set<AcceleratorNode> nodes = network.nodes;

        //Get grid min-max
        int minX = nodes.stream().min(Comparator.comparingInt(node -> node.getPos().getX())).get().getPos().getX();
        int minZ = nodes.stream().min(Comparator.comparingInt(node -> node.getPos().getZ())).get().getPos().getZ();

        int maxX = nodes.stream().max(Comparator.comparingInt(node -> node.getPos().getX())).get().getPos().getX();
        int maxZ = nodes.stream().max(Comparator.comparingInt(node -> node.getPos().getZ())).get().getPos().getZ();

        //Get grid size
        int sizeX = Math.abs(maxX - minX) + 10; //TODO fix math so we don't need +10
        int sizeY = Math.abs(maxZ - minZ) + 10;

        //Fix min-size
        if (sizeX == 0)
        {
            sizeX = 1;
        }
        if (sizeY == 0)
        {
            sizeY = 1;
        }

        //Debug size
        System.out.println(minX + ", " + minZ + " - " + maxX + ", " + maxZ + "  " + sizeX + "x" + sizeY);

        //Generate grid
        char[][] grid = new char[sizeX][sizeY];
        nodes.forEach(node -> {
            BlockPos blockPos = node.getPos();
            int x = blockPos.getX() - minX + 2;
            int z = blockPos.getZ() - minZ + 2;

            //Output position of connection
            System.out.println(blockPos);
            System.out.println(x + ", " + z + "  " + grid.length + "x" + grid[0].length);

            //Output connections
            Arrays.stream(node.getNodes()).forEach(n -> System.out.println("\t" + n.getNodes()));

            //Set char for number of connections 0-6
            int connections = node.getNodes().length;
            grid[x][z] = Character.forDigit(connections, 10);
        });

        //Output grid
        for (int x = 0; x < grid.length; x++)
        {
            for (int z = 0; z < grid[x].length; z++)
            {
                char c = grid[x][z];
                if (c == 0)
                {
                    c = ' ';
                }
                System.out.print(c);
            }
            System.out.println();
        }
    }
}
