package com.builtbroken.test.as.accelerator.data;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import net.minecraft.util.EnumFacing;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-22.
 */
public class TestTubeSide
{

    @Test
    public void testInvertBack()
    {
        Assertions.assertEquals(TubeSide.FRONT, TubeSide.BACK.getOpposite());
    }

    @Test
    public void testInvertFront()
    {
        Assertions.assertEquals(TubeSide.BACK, TubeSide.FRONT.getOpposite());
    }

    @Test
    public void testInvertLeft()
    {
        Assertions.assertEquals(TubeSide.RIGHT, TubeSide.LEFT.getOpposite());
    }

    @Test
    public void testInvertRight()
    {
        Assertions.assertEquals(TubeSide.LEFT, TubeSide.RIGHT.getOpposite());
    }

    @Test
    public void testInvertCenter()
    {
        Assertions.assertEquals(TubeSide.CENTER, TubeSide.CENTER.getOpposite());
    }

    @ParameterizedTest
    @MethodSource("provideGetFacingData")
    public void testGetFacing(TubeSide side, EnumFacing facing, EnumFacing expected)
    {
        Assertions.assertEquals(expected, side.getFacing(facing));
    }

    private static Stream<Arguments> provideGetFacingData()
    {
        return Stream.of(
                Arguments.of(TubeSide.FRONT, EnumFacing.NORTH, EnumFacing.NORTH),
                Arguments.of(TubeSide.FRONT, EnumFacing.SOUTH, EnumFacing.SOUTH),
                Arguments.of(TubeSide.FRONT, EnumFacing.EAST, EnumFacing.EAST),
                Arguments.of(TubeSide.FRONT, EnumFacing.WEST, EnumFacing.WEST),

                Arguments.of(TubeSide.BACK, EnumFacing.NORTH, EnumFacing.SOUTH),
                Arguments.of(TubeSide.BACK, EnumFacing.SOUTH, EnumFacing.NORTH),
                Arguments.of(TubeSide.BACK, EnumFacing.EAST, EnumFacing.WEST),
                Arguments.of(TubeSide.BACK, EnumFacing.WEST, EnumFacing.EAST),

                Arguments.of(TubeSide.LEFT, EnumFacing.NORTH, EnumFacing.WEST),
                Arguments.of(TubeSide.LEFT, EnumFacing.SOUTH, EnumFacing.EAST),
                Arguments.of(TubeSide.LEFT, EnumFacing.EAST, EnumFacing.NORTH),
                Arguments.of(TubeSide.LEFT, EnumFacing.WEST, EnumFacing.SOUTH),

                Arguments.of(TubeSide.RIGHT, EnumFacing.NORTH, EnumFacing.EAST),
                Arguments.of(TubeSide.RIGHT, EnumFacing.SOUTH, EnumFacing.WEST),
                Arguments.of(TubeSide.RIGHT, EnumFacing.EAST, EnumFacing.SOUTH),
                Arguments.of(TubeSide.RIGHT, EnumFacing.WEST, EnumFacing.NORTH),

                //Null should always result in null
                Arguments.of(TubeSide.FRONT, null, null),
                Arguments.of(TubeSide.LEFT, null, null),
                Arguments.of(TubeSide.RIGHT, null, null),
                Arguments.of(TubeSide.BACK, null, null),
                Arguments.of(TubeSide.CENTER, null, null),

                //Center should always return null
                Arguments.of(TubeSide.CENTER, EnumFacing.UP, null),
                Arguments.of(TubeSide.CENTER, EnumFacing.DOWN, null),
                Arguments.of(TubeSide.CENTER, EnumFacing.NORTH, null),
                Arguments.of(TubeSide.CENTER, EnumFacing.SOUTH, null),
                Arguments.of(TubeSide.CENTER, EnumFacing.EAST, null),
                Arguments.of(TubeSide.CENTER, EnumFacing.WEST, null)
        );
    }
}
