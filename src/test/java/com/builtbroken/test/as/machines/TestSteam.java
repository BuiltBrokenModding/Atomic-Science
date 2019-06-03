package com.builtbroken.test.as.machines;

import com.builtbroken.atomic.lib.vapor.VaporHandler;
import com.builtbroken.test.as.world.FakeWorldAccess;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-03.
 */
public class TestSteam
{

    @BeforeAll
    public static void beforeAll()
    {
        Bootstrap.register();
    }

    @ParameterizedTest
    @MethodSource("shouldPathThroughData")
    public void testShouldPath(Block block, boolean yes)
    {
        FakeWorldAccess fakeWorldAccess = new FakeWorldAccess();
        fakeWorldAccess.addBlock(BlockPos.ORIGIN, block.getDefaultState());
        Assertions.assertEquals(yes, VaporHandler.canSteamPassThrough(fakeWorldAccess, BlockPos.ORIGIN));
    }

    private static Stream<Arguments> shouldPathThroughData()
    {
        return Stream.of(
                Arguments.of(Blocks.AIR, true),
                Arguments.of(Blocks.WATER, true),
                Arguments.of(Blocks.BEDROCK, false),
                Arguments.of(Blocks.CLAY, false)
        );
    }
}
