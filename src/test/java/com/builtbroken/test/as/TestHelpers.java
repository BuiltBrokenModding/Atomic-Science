package com.builtbroken.test.as;

import org.junit.jupiter.api.Assertions;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-09.
 */
public class TestHelpers
{

    public static void compareFloats3Zeros(float expected, float actual)
    {
        compareFloats3Zeros(expected, actual, "");
    }

    public static void compareFloats3Zeros(float expected, float actual, String message)
    {
        if (Math.abs(expected - actual) > 0.0001)
        {
            Assertions.fail(message
                    + "\nExpected :" + expected
                    + "\nActual   :" + actual);
        }
    }
}
