package com.builtbroken.atomic.content.machines.accelerator.particle;

import com.builtbroken.jlib.lang.StringHelpers;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-29.
 */
public class MainPerformanceTest
{
    public static void main(String... args) throws IOException
    {
        final IMovablePos pos = new MovablePos();
        final FractionPos fpos = new FractionPos();
        fpos.setPrecision(1000000);

        List<Long> pTime = new LinkedList();
        List<Long> fTime = new LinkedList();

        final double m = Math.random();
        final BigDecimal movement = new BigDecimal(m);


        BigDecimal bigDecimal = new BigDecimal(0);

        for(int i = 0; i < 1000000; i++)
        {
            bigDecimal = bigDecimal.add(movement);

            System.out.println();
            System.out.println(String.format("Expected: ....%s ....%s", bigDecimal, movement));

            //Float
            long time = System.nanoTime();
            pos.move(m, 0,0 );
            time = System.nanoTime() - time;
            pTime.add(time);

            //Fraction
            long time2 = System.nanoTime();
            fpos.move(m, 0,0 );
            time2 = System.nanoTime() - time2;
            fTime.add(time2);

            //output
            outputResult(time, pos.x(), bigDecimal);
            outputResult(time2, fpos.x(), bigDecimal);

            long delta = time2 - time;
            long us = ((delta % 1000000000) % 1000000) / 1000;
            long ns = ((delta % 1000000000) % 1000000) % 1000;
            System.out.println(String.format("Time Diff: %3s us %3s ns", us, ns));
        }

        //Export results as csv for import into excel for graphing
        FileWriter writer = new FileWriter("results" + System.currentTimeMillis() + ".csv");
        writer.append("Pos,FPos\n");
        for(int i = 0; i < pTime.size(); i++)
        {
            writer.append("" + pTime.get(i));
            writer.append(",");
            writer.append("" + fTime.get(i));
            writer.append("\n");
        }
        writer.flush();
        writer.close();

        //Done
        System.exit(0);
    }

    public static void outputResult(long nano, double result, BigDecimal expected)
    {
        long us = ((nano % 1000000000) % 1000000) / 1000;
        long ns = ((nano % 1000000000) % 1000000) % 1000;

        double delta = expected.subtract(new BigDecimal(result)).doubleValue();

        System.out.println(String.format("%3s us %3s ns %.10f %s%.10f", us, ns, result,delta >= 0 ? " " : "", delta));
    }
}
