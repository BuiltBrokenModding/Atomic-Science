package com.builtbroken.test.as.accelerator;

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
 * Permutations of tubes and rotations
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-19.
 */
public class TubeFaceArgumentsProvider implements ArgumentsProvider
{

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
    {
        final List<Arguments> list = new ArrayList();
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            for (TubeConnectionType type : TubeConnectionType.values())
            {
                //Arguments: tube, face
                list.add(Arguments.of(type, facing));
            }
        }

        return list.stream();
    }
}
