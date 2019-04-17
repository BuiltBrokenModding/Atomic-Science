package com.builtbroken.test.as.accelerator.connections;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.tube.TileEntityAcceleratorTube;
import com.sun.source.tree.AssertTree;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class ConnectionCommon
{

    public static final BlockPos BLOCK_POS_ZERO = new BlockPos(0, 0, 0);

    /**
     * Called to create a new tube node
     * <p>
     * Will validate starting conditions to ensure test is valid
     *
     * @param facing
     * @param connectionType
     * @return
     */
    public static AcceleratorNode newNode(EnumFacing facing, TubeConnectionType connectionType)
    {
        return newNode(BLOCK_POS_ZERO, facing, connectionType);
    }

    /**
     * Called to create a new tube node
     * <p>
     * Will validate starting conditions to ensure test is valid
     *
     * @param pos
     * @param facing
     * @param connectionType
     * @return
     */
    public static AcceleratorNode newNode(BlockPos pos, EnumFacing facing, TubeConnectionType connectionType)
    {
        //Create
        AcceleratorNode node = new AcceleratorNode(pos, facing, connectionType);

        //Test init so we can fail early if something goes wrong
        Assertions.assertEquals(pos, node.getPos());
        Assertions.assertNull(node.getNetwork());
        Assertions.assertEquals(0, node.turnIndex);

        //Match we are the right facing and type
        Assertions.assertEquals(facing, node.getDirection());
        Assertions.assertEquals(connectionType, node.getConnectionType());

        //Should start with an array of 6 sides all empty
        Assertions.assertEquals(6, node.getNodes().length);
        for (EnumFacing side : EnumFacing.values())
        {
            Assertions.assertNull(node.getNodes()[side.ordinal()]);
        }

        return node;
    }

    public static ATestTube newTube()
    {
        final ATestTube tube = new ATestTube();
        tube.setPos(BLOCK_POS_ZERO);

        Assertions.assertEquals(0, tube.xi());
        Assertions.assertEquals(0, tube.yi());
        Assertions.assertEquals(0, tube.zi());

        Assertions.assertNull(tube.world());

        Assertions.assertNotNull(tube.acceleratorNode);

        return tube;
    }
}
