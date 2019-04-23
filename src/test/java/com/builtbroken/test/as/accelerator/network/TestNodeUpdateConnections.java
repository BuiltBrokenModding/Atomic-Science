package com.builtbroken.test.as.accelerator.network;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.test.as.accelerator.ATubeTestCommon;
import com.builtbroken.test.as.providers.EnumFacingSideArgumentsProvider;
import com.builtbroken.test.as.world.FakeWorldAccess;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-22.
 */
public class TestNodeUpdateConnections extends ATubeTestCommon
{

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void front(EnumFacing facing)
    {
        //Create tube
        AcceleratorNode node = newNode(BLOCK_POS_ZERO, facing, TubeConnectionType.NORMAL);

        //Create test world
        FakeWorldAccess worldAccess = new FakeWorldAccess();
        createTube(worldAccess, facing, facing);

        //Map connections
        node.updateConnections(worldAccess);

        //Test
        Assertions.assertNotNull(node.getNodes()[facing.ordinal()]);
        Assertions.assertNull(node.getNodes()[facing.getOpposite().ordinal()]);
        Assertions.assertNull(node.getNodes()[facing.rotateY().ordinal()]);
        Assertions.assertNull(node.getNodes()[facing.rotateY().getOpposite().ordinal()]);
    }

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void back(EnumFacing facing)
    {
        //Create tube
        AcceleratorNode node = newNode(BLOCK_POS_ZERO, facing, TubeConnectionType.NORMAL);

        //Create test world
        FakeWorldAccess worldAccess = new FakeWorldAccess();
        createTube(worldAccess, facing.getOpposite(), facing);

        //Map connections
        node.updateConnections(worldAccess);

        //Test
        Assertions.assertNotNull(node.getNodes()[facing.getOpposite().ordinal()]);
        Assertions.assertNull(node.getNodes()[facing.ordinal()]);
        Assertions.assertNull(node.getNodes()[facing.rotateY().ordinal()]);
        Assertions.assertNull(node.getNodes()[facing.rotateY().getOpposite().ordinal()]);
    }

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void frontAndBack(EnumFacing facing)
    {
        //Create tube
        AcceleratorNode node = newNode(BLOCK_POS_ZERO, facing, TubeConnectionType.NORMAL);

        //Create test world
        FakeWorldAccess worldAccess = new FakeWorldAccess();
        createTube(worldAccess, facing.getOpposite(), facing);
        createTube(worldAccess, facing, facing);

        //Map connections
        node.updateConnections(worldAccess);

        //Test
        Assertions.assertNotNull(node.getNodes()[facing.getOpposite().ordinal()]);
        Assertions.assertNotNull(node.getNodes()[facing.ordinal()]);
        Assertions.assertNull(node.getNodes()[facing.rotateY().ordinal()]);
        Assertions.assertNull(node.getNodes()[facing.rotateY().getOpposite().ordinal()]);
    }

    public void createTube(FakeWorldAccess worldAccess, EnumFacing offset, EnumFacing facing)
    {
        worldAccess.addTile(BLOCK_POS_ZERO.offset(offset), newTube(facing, BLOCK_POS_ZERO.offset(offset)));

    }

}
