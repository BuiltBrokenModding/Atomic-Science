package com.builtbroken.test.as.accelerator.connection;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-19.
 */
public class EntryFromArgumentsProvider implements ArgumentsProvider
{

    //https://www.baeldung.com/parameterized-tests-junit-5
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
    {
        final List<Arguments> list = new ArrayList();
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            //Arguments: rotation, relative rotation, tube type
            for (Object[] data : getEntryData())
            {
                final TubeSide targetSide = (TubeSide) data[0];
                final TubeConnectionType targetType = (TubeConnectionType) data[1];

                list.add(Arguments.of(facing, targetSide, targetType));
            }
        }

        return list.stream();
    }

    public static Object[][] getEntryData()
    {
        final Object[][] matrix = new Object[15][2];
        //relative rotation, tube type
        //Relative rotation from the center tube's perspective
        //  If we need to connect our right side to the center
        //   this means we might end up facing to the center's left
        //  Example: Placing on the south side and connecting our right -> we face left

        //1:1 tubes
        matrix[0] = new Object[]{TubeSide.FRONT, TubeConnectionType.CORNER_RIGHT};
        matrix[1] = new Object[]{TubeSide.FRONT, TubeConnectionType.CORNER_LEFT};
        matrix[2] = new Object[]{TubeSide.FRONT, TubeConnectionType.T_JOIN_RIGHT};
        matrix[3] = new Object[]{TubeSide.FRONT, TubeConnectionType.T_JOIN_LEFT};
        matrix[4] = new Object[]{TubeSide.FRONT, TubeConnectionType.JOIN};
        matrix[5] = new Object[]{TubeSide.FRONT, TubeConnectionType.NORMAL};

        //2:1
        matrix[6] = new Object[]{TubeSide.LEFT, TubeConnectionType.T_SPLIT};
        matrix[7] = new Object[]{TubeSide.RIGHT, TubeConnectionType.T_SPLIT};

        matrix[8] = new Object[]{TubeSide.FRONT, TubeConnectionType.T_SPLIT_RIGHT};
        matrix[9] = new Object[]{TubeSide.RIGHT, TubeConnectionType.T_SPLIT_RIGHT};

        matrix[10] = new Object[]{TubeSide.FRONT, TubeConnectionType.T_SPLIT_LEFT};
        matrix[11] = new Object[]{TubeSide.LEFT, TubeConnectionType.T_SPLIT_LEFT};

        //3:1
        matrix[12] = new Object[]{TubeSide.FRONT, TubeConnectionType.SPLIT};
        matrix[13] = new Object[]{TubeSide.RIGHT, TubeConnectionType.SPLIT};
        matrix[14] = new Object[]{TubeSide.LEFT, TubeConnectionType.SPLIT};

        return matrix;
    }
}
