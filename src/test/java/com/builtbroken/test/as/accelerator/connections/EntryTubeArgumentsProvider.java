package com.builtbroken.test.as.accelerator.connections;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-19.
 */
public class EntryTubeArgumentsProvider implements ArgumentsProvider
{
    //https://www.baeldung.com/parameterized-tests-junit-5
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
    {
        final Arguments[] array = new Arguments[15 * 4]; //15 tests, 4 rotations to test
        int index = 0;
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            //Arguments: rotation, relative rotation, tube type

            //Relative rotation from the center tube's perspective
            //  If we need to connect our right side to the center
            //   this means we might end up facing to the center's left
            //  Example: Placing on the south side and connecting right -> we face left

            //1:1 tubes
            array[index++] = Arguments.of(facing, TubeSide.FRONT, TubeConnectionType.CORNER_RIGHT);
            array[index++] = Arguments.of(facing, TubeSide.FRONT, TubeConnectionType.CORNER_LEFT);
            array[index++] = Arguments.of(facing, TubeSide.FRONT, TubeConnectionType.T_JOIN_RIGHT);
            array[index++] = Arguments.of(facing, TubeSide.FRONT, TubeConnectionType.T_JOIN_LEFT);
            array[index++] = Arguments.of(facing, TubeSide.FRONT, TubeConnectionType.JOIN);
            array[index++] = Arguments.of(facing, TubeSide.FRONT, TubeConnectionType.NORMAL);

            //2:1
            array[index++] = Arguments.of(facing, TubeSide.LEFT, TubeConnectionType.T_SPLIT);
            array[index++] = Arguments.of(facing, TubeSide.RIGHT, TubeConnectionType.T_SPLIT);

            array[index++] = Arguments.of(facing, TubeSide.FRONT, TubeConnectionType.T_SPLIT_RIGHT);
            array[index++] = Arguments.of(facing, TubeSide.LEFT, TubeConnectionType.T_SPLIT_RIGHT);

            array[index++] = Arguments.of(facing, TubeSide.FRONT, TubeConnectionType.T_SPLIT_LEFT);
            array[index++] = Arguments.of(facing, TubeSide.RIGHT, TubeConnectionType.T_SPLIT_LEFT);

            //3:1
            array[index++] = Arguments.of(facing, TubeSide.FRONT, TubeConnectionType.SPLIT);
            array[index++] = Arguments.of(facing, TubeSide.RIGHT, TubeConnectionType.SPLIT);
            array[index++] = Arguments.of(facing, TubeSide.LEFT, TubeConnectionType.SPLIT);
        }

        return Stream.of(array);
    }
}
