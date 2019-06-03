package com.builtbroken.test.as.machines;

import com.builtbroken.atomic.lib.vapor.VaporHandler;
import com.builtbroken.test.as.world.FakeWorldAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.EnumFacing;
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
    public void testShouldPath(IBlockState blockstate, boolean yes)
    {
        FakeWorldAccess fakeWorldAccess = new FakeWorldAccess();
        fakeWorldAccess.addBlock(BlockPos.ORIGIN, blockstate);
        Assertions.assertEquals(yes, VaporHandler.canSteamPassThrough(fakeWorldAccess, BlockPos.ORIGIN));
    }

    private static Stream<Arguments> shouldPathThroughData()
    {
        return Stream.of(
                Arguments.of(Blocks.AIR.getDefaultState(), true),
                Arguments.of(Blocks.WATER.getDefaultState(), true),
                Arguments.of(Blocks.ACACIA_FENCE.getDefaultState(), true),
                Arguments.of(Blocks.IRON_BARS.getDefaultState(), true),
                Arguments.of(Blocks.CACTUS.getDefaultState(), true),

                Arguments.of(Blocks.TORCH.getDefaultState(), true),
                Arguments.of(Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH), true),
                Arguments.of(Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH), true),
                Arguments.of(Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST), true),
                Arguments.of(Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST), true),

                Arguments.of(Blocks.REDSTONE_TORCH.getDefaultState(), true),
                Arguments.of(Blocks.GLASS_PANE.getDefaultState(), true),

                Arguments.of(Blocks.GRASS.getDefaultState(), false),
                Arguments.of(Blocks.STONE.getDefaultState(), false),
                Arguments.of(Blocks.GLASS.getDefaultState(), false),
                Arguments.of(Blocks.BEDROCK.getDefaultState(), false),
                Arguments.of(Blocks.CLAY.getDefaultState(), false)
        );
    }
}
