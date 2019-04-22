package com.builtbroken.test.as.providers;

import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Permutations of tubes and rotations
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-22.
 */
public class EnumFacingArgumentsProvider implements ArgumentsProvider
{

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
    {
        final List<Arguments> list = new ArrayList();
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            list.add(Arguments.of(facing));
        }

        return list.stream();
    }
}
