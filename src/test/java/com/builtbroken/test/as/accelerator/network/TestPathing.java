package com.builtbroken.test.as.accelerator.network;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNetwork;
import com.builtbroken.test.as.accelerator.ATubeTestCommon;
import com.builtbroken.test.as.world.FakeWorldAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-30.
 */
public class TestPathing extends ATubeTestCommon
{
    @Test
    public void testSimple()
    {
        FakeWorldAccess worldAccess = new FakeWorldAccess();
        createTube(worldAccess, BlockPos.ORIGIN, EnumFacing.NORTH, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.north(1), EnumFacing.NORTH, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.north(2), EnumFacing.NORTH, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.south(1), EnumFacing.NORTH, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.south(2), EnumFacing.NORTH, TubeConnectionType.NORMAL);

        AcceleratorNetwork network = new AcceleratorNetwork(0);
        network.init(worldAccess, BlockPos.ORIGIN);

        Assertions.assertEquals(5, network.getNodes().size());
    }


    @Test
    public void testBend()
    {
        FakeWorldAccess worldAccess = new FakeWorldAccess();
        createTube(worldAccess, BlockPos.ORIGIN, EnumFacing.NORTH, TubeConnectionType.CORNER_LEFT);
        createTube(worldAccess, BlockPos.ORIGIN.west(1), EnumFacing.EAST, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.west(2), EnumFacing.EAST, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.north(1), EnumFacing.NORTH, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.north(2), EnumFacing.NORTH, TubeConnectionType.NORMAL);

        AcceleratorNetwork network = new AcceleratorNetwork(0);
        network.init(worldAccess, BlockPos.ORIGIN);

        Assertions.assertEquals(5, network.getNodes().size());
    }

    @Test
    public void testBox()
    {
        FakeWorldAccess worldAccess = new FakeWorldAccess();

        //   /\
        //   /\
        createTube(worldAccess, BlockPos.ORIGIN.north(1), EnumFacing.NORTH, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.north(2), EnumFacing.NORTH, TubeConnectionType.NORMAL);

        //   /\
        //   /\
        //   C
        createTube(worldAccess, BlockPos.ORIGIN, EnumFacing.NORTH, TubeConnectionType.CORNER_LEFT);

        // >>C
        createTube(worldAccess, BlockPos.ORIGIN.west(1), EnumFacing.EAST, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.west(2), EnumFacing.EAST, TubeConnectionType.NORMAL);

        // C>>C
        createTube(worldAccess, BlockPos.ORIGIN.west(3), EnumFacing.EAST, TubeConnectionType.CORNER_LEFT);

        // \/
        // \/
        // C>>C
        createTube(worldAccess, BlockPos.ORIGIN.west(3).north(1), EnumFacing.SOUTH, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.west(3).north(2), EnumFacing.SOUTH, TubeConnectionType.NORMAL);

        // C
        // \/
        // \/
        createTube(worldAccess, BlockPos.ORIGIN.west(3).north(3), EnumFacing.SOUTH, TubeConnectionType.CORNER_LEFT);

        // C<<
        // \/
        // \/
        createTube(worldAccess, BlockPos.ORIGIN.west(1).north(3), EnumFacing.WEST, TubeConnectionType.NORMAL);
        createTube(worldAccess, BlockPos.ORIGIN.west(2).north(3), EnumFacing.WEST, TubeConnectionType.NORMAL);

        // C<<C
        // \/
        // \/
        createTube(worldAccess, BlockPos.ORIGIN.north(3), EnumFacing.WEST, TubeConnectionType.CORNER_LEFT);


        AcceleratorNetwork network = new AcceleratorNetwork(0);
        network.init(worldAccess, BlockPos.ORIGIN);

        Assertions.assertEquals(12, network.getNodes().size());
    }

    public void createTube(FakeWorldAccess worldAccess, BlockPos pos, EnumFacing facing, TubeConnectionType type)
    {
        worldAccess.addTile(pos, newTube(facing, pos, type));
    }
}
