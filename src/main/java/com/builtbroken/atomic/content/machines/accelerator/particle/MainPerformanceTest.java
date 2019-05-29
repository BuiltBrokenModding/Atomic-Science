package com.builtbroken.atomic.content.machines.accelerator.particle;

import com.builtbroken.jlib.lang.StringHelpers;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-29.
 */
public class MainPerformanceTest
{
    public static void main(String... args)
    {
        final IMovablePos pos = new MovablePos();
        final IMovablePos fpos = new FractionPos();

        List<Long> pTime = new LinkedList();
        List<Long> fTime = new LinkedList();

        final double movement = 0.1;

        for(int i = 0; i < 100000; i++)
        {
            final double expected = (i + 1) * movement;
            System.out.println();
            System.out.println(String.format("Expected: ....%s", expected));

            //Float
            long time = System.nanoTime();
            pos.move(movement, 0,0 );
            time = System.nanoTime() - time;
            pTime.add(time);

            //Fraction
            long time2 = System.nanoTime();
            fpos.move(movement, 0,0 );
            time2 = System.nanoTime() - time2;
            fTime.add(time2);

            //output
            outputResult(time, pos.x(), expected);
            outputResult(time2, fpos.x(), expected);

            long delta = time2 - time;
            long us = ((delta % 1000000000) % 1000000) / 1000;
            long ns = ((delta % 1000000000) % 1000000) % 1000;
            System.out.println(String.format("Time Diff: %3s us %3s ns", us, ns));
        }
    }

    public static void outputResult(long nano, double result, double expected)
    {
        long us = ((nano % 1000000000) % 1000000) / 1000;
        long ns = ((nano % 1000000000) % 1000000) % 1000;

        double delta = expected - result;

        System.out.println(String.format("%3s us %3s ns %-20s %s", us, ns, result, delta));
    }
}
