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
public class ExitIntoArgumentsProvider implements ArgumentsProvider
{

    //https://www.baeldung.com/parameterized-tests-junit-5
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
    {
        final List<Arguments> list = new ArrayList();
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            //Arguments: rotation, relative rotation, tube type
            for (Object[] data : getExitData())
            {
                list.add(newArg(new Object[]{facing}, data));
            }
        }

        return list.stream();
    }

    public static Object[][] getExitData()
    {
        final Object[][] matrix = new Object[16][2];
        //relative rotation, tube type
        //Relative rotation from the center tube's perspective
        //  If we need to connect our right side to the center
        //   this means we might end up facing to the center's left
        //  Example: Placing on the north side and connecting our right -> we face left

        //1:1, 1 connection per tube
        matrix[0] = new Object[]{TubeSide.RIGHT, TubeConnectionType.CORNER_RIGHT};
        matrix[1] = new Object[]{TubeSide.LEFT, TubeConnectionType.CORNER_LEFT};
        matrix[2] = new Object[]{TubeSide.FRONT, TubeConnectionType.NORMAL};
        matrix[3] = new Object[]{TubeSide.FRONT, TubeConnectionType.T_SPLIT};
        matrix[4] = new Object[]{TubeSide.FRONT, TubeConnectionType.T_SPLIT_LEFT};
        matrix[5] = new Object[]{TubeSide.FRONT, TubeConnectionType.T_SPLIT_RIGHT};
        matrix[6] = new Object[]{TubeSide.FRONT, TubeConnectionType.SPLIT};

        //2:1
        matrix[7] = new Object[]{TubeSide.FRONT, TubeConnectionType.T_JOIN_RIGHT};
        matrix[8] = new Object[]{TubeSide.RIGHT, TubeConnectionType.T_JOIN_RIGHT};

        matrix[9] = new Object[]{TubeSide.FRONT, TubeConnectionType.T_JOIN_LEFT};
        matrix[10] = new Object[]{TubeSide.LEFT, TubeConnectionType.T_JOIN_LEFT};

        matrix[11] = new Object[]{TubeSide.LEFT, TubeConnectionType.T_JOIN};
        matrix[12] = new Object[]{TubeSide.RIGHT, TubeConnectionType.T_JOIN};

        //3:1
        matrix[13] = new Object[]{TubeSide.FRONT, TubeConnectionType.JOIN};
        matrix[14] = new Object[]{TubeSide.LEFT, TubeConnectionType.JOIN};
        matrix[15] = new Object[]{TubeSide.RIGHT, TubeConnectionType.JOIN};

        return matrix;
    }

    public static Arguments newArg(Object[] prefix, Object[] data)
    {
        if (prefix != null)
        {
            final Object[] re = new Object[prefix.length + 2];
            for (int i = 0; i < prefix.length; i++)
            {
                re[i] = prefix[i];
            }
            for (int i = 0; i < data.length; i++)
            {
                re[i + prefix.length] = data[i];
            }
            return Arguments.of(re);
        }
        return Arguments.of(data);
    }
}
