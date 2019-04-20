package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;

/**
 * Set of common helper methods for testing accelerator tubes
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class ATubeTestCommon
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
        AcceleratorNode node = new AcceleratorNode().setData(pos, facing, connectionType);

        validateNodeInit(node, pos, facing, connectionType);

        return node;
    }

    public static void validateNodeInit(AcceleratorNode node, BlockPos pos, EnumFacing facing, TubeConnectionType connectionType)
    {
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
    }

    /**
     * Creates a new tube at the position facing the direction given
     * <p>
     * Does some basic validation to ensure the tube setup with the right data.
     *
     * @param facing - direction to face
     * @param pos    - position to exist
     * @return new tube
     */
    public static ATestTube newTube(EnumFacing facing, BlockPos pos)
    {
        return newTube(facing, pos, null);
    }

    /**
     * Creates a new tube at the position facing the direction given
     * <p>
     * Does some basic validation to ensure the tube setup with the right data.
     *
     * @param facing - direction to face
     * @param pos    - position to exist
     * @return new tube
     */
    public static ATestTube newTube(EnumFacing facing, BlockPos pos, TubeConnectionType type)
    {
        //Init
        final ATestTube tube = new ATestTube();

        //Check that we have a node
        Assertions.assertNotNull(tube.getNode());

        //Set node data
        tube.getNode().setData(pos, facing, type != null ? type : TubeConnectionType.NORMAL);

        //Do normal validations for nodes
        validateNodeInit(tube.getNode(), pos, facing, type != null ? type : TubeConnectionType.NORMAL);

        //Set tube data
        if (type != null)
        {
            tube.setConnectionType(type);
        }
        tube.setDirection(facing);
        tube.setPos(pos);

        //Test position
        Assertions.assertEquals(pos.getX(), tube.xi());
        Assertions.assertEquals(pos.getY(), tube.yi());
        Assertions.assertEquals(pos.getZ(), tube.zi());

        //Test for null world, if not null we need to update tests
        Assertions.assertNull(tube.world());

        return tube;
    }

    /**
     * Helper to add a new tube to the center tube for testing on the side and facing a direction
     *
     * @param center     - tube to act as both a center and the target to add
     * @param sideOffset - side to move the tube towards and place in the tile map
     * @param facing     - direction for the tube to face
     */
    public static void addTube(ATestTube center, EnumFacing sideOffset, EnumFacing facing)
    {
        center.setTiles(sideOffset, newTube(center.getPos(), sideOffset, facing));
    }

    /**
     * Helper to add a new tube to the center tube for testing on the side and facing a direction
     *
     * @param center     - tube to act as both a center and the target to add
     * @param sideOffset - side to move the tube towards and place in the tile map
     * @param facing     - direction for the tube to face
     */
    public static void addTube(ATestTube center, EnumFacing sideOffset, EnumFacing facing, TubeConnectionType type)
    {
        center.setTiles(sideOffset, newTube(center.getPos(), sideOffset, facing, type));
    }

    /**
     * Called to make a new tube on the side with a facing and position
     *
     * @param pos        - position to create the tube
     * @param sideOffset - side to place the tube from the position, acts as an offset for pos
     * @param facing     - direction to face the tube
     * @return new tube
     */
    public static ATestTube newTube(BlockPos pos, EnumFacing sideOffset, EnumFacing facing)
    {
        return newTube(facing, pos.offset(sideOffset));
    }

    /**
     * Called to make a new tube on the side with a facing and position
     *
     * @param pos        - position to create the tube
     * @param sideOffset - side to place the tube from the position, acts as an offset for pos
     * @param facing     - direction to face the tube
     * @return new tube
     */
    public static ATestTube newTube(BlockPos pos, EnumFacing sideOffset, EnumFacing facing, TubeConnectionType type)
    {
        return newTube(facing, pos.offset(sideOffset), type);
    }
}
