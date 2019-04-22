package com.builtbroken.test.as.accelerator.connection;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-22.
 */
public class SuperPermArgumentsProvider implements ArgumentsProvider
{
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
    {
        final List<Arguments> list = new ArrayList();

        //Tube we will place in center
        for (TubeConnectionType type : TubeConnectionType.values())
        {
            //Rotation of center tube
            for (EnumFacing facing : EnumFacing.HORIZONTALS)
            {
                //Side of center tube
                for (TubeSide side : TubeSide.SIDES)
                {
                    addEntries(list, type, facing, side);
                }
            }
        }

        return list.stream();
    }

    private void addEntries(List<Arguments> list, TubeConnectionType type, EnumFacing facing, TubeSide side)
    {
        final TubeSideType connection = type.getTypeForSide(side);

        //Test enter connections
        for (Object[] data : EntryFromArgumentsProvider.getEntryData())
        {
            final TubeSide targetSide = (TubeSide) data[0];
            final TubeConnectionType targetType = (TubeConnectionType) data[1];

            //Get expected rotation
            EnumFacing targetRotation = side.getRotationRelative(facing, targetSide);

            list.add(Arguments.of(type, facing, side, targetType, targetRotation, TubeSideType.ENTER,
                    connection == TubeSideType.ENTER));
        }

        //Test exit connections
        for (Object[] data : ExitIntoArgumentsProvider.getExitData())
        {
            final TubeSide targetSide = (TubeSide) data[0];
            final TubeConnectionType targetType = (TubeConnectionType) data[1];

            //Get expected rotation
            EnumFacing targetRotation = side.getRotationRelative(facing, targetSide);

            list.add(Arguments.of(type, facing, side, targetType, targetRotation, TubeSideType.EXIT,
                    connection == TubeSideType.EXIT));
        }
    }
}
