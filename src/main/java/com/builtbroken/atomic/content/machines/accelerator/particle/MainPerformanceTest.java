package com.builtbroken.atomic.content.machines.accelerator.particle;

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
        final List<DataOut> data = new LinkedList();

        for (int tr = 0; tr < 100; tr++)
        {
            final double m = ((int) Math.round(Math.random() * 1000)) / 1000f;
            final BigDecimal movement = new BigDecimal(m);

            final IMovablePos floatPos = new MovableFPos();
            final IMovablePos doublePos = new MovableDPos();
            final FractionPos fractionPos = new FractionPos();
            fractionPos.setPrecision(1000);

            BigDecimal bigDecimal = new BigDecimal(0);

            for (int it = 0; it < 1000; it++)
            {
                bigDecimal = bigDecimal.add(movement);

                System.out.println();
                System.out.println(String.format("Expected: ....%s ....%s", bigDecimal, m));

                final DataOut dataOut = new DataOut();
                dataOut.move = m;
                dataOut.expected = bigDecimal.doubleValue();

                //Float
                dataOut.floatTime = System.nanoTime();
                floatPos.move(m, 0, 0);
                dataOut.floatOutput = floatPos.x();
                dataOut.floatTime = System.nanoTime() - dataOut.floatTime;

                //Float
                dataOut.doubleTime = System.nanoTime();
                doublePos.move(m, 0, 0);
                dataOut.doubleOutput = doublePos.x();
                dataOut.doubleTime = System.nanoTime() - dataOut.doubleTime;

                //Fraction
                dataOut.fractionTime = System.nanoTime();
                fractionPos.move(m, 0, 0);
                dataOut.fractionOutput = fractionPos.x();
                dataOut.fractionTime = System.nanoTime() - dataOut.fractionTime;

                //output
                outputResult(dataOut.floatTime, dataOut.floatOutput, bigDecimal);
                outputResult(dataOut.doubleTime, dataOut.doubleOutput, bigDecimal);
                outputResult(dataOut.fractionTime, dataOut.fractionOutput, bigDecimal);

                //Output delta in time
                long delta = dataOut.fractionTime - dataOut.floatTime;
                long us = ((delta % 1000000000) % 1000000) / 1000;
                long ns = ((delta % 1000000000) % 1000000) % 1000;
                System.out.println(String.format("Fr vs F: %3s us %3s ns", us, ns));

                delta = dataOut.fractionTime - dataOut.doubleTime;
                us = ((delta % 1000000000) % 1000000) / 1000;
                ns = ((delta % 1000000000) % 1000000) % 1000;
                System.out.println(String.format("Fr v D: %3s us %3s ns", us, ns));
            }
        }

        //Export results as csv for import into excel for graphing
        FileWriter writer = new FileWriter("results" + System.currentTimeMillis() + ".csv");
        writer.append("Fraction Time," +
                "Double Time," +
                "Float Time," +
                "Move," +
                "Expected Result," +
                "Float Result," +
                "Double Result," +
                "Fraction Result" +
                "\n");
        for (DataOut dataOut : data)
        {
            writer.append("" + dataOut.floatTime);
            writer.append(",");
            writer.append("" + dataOut.doubleTime);
            writer.append(",");
            writer.append("" + dataOut.fractionTime);
            writer.append(",");
            writer.append("" + dataOut.move);
            writer.append(",");
            writer.append("" + dataOut.expected);
            writer.append(",");
            writer.append("" + dataOut.floatOutput);
            writer.append(",");
            writer.append("" + dataOut.doubleOutput);
            writer.append(",");
            writer.append("" + dataOut.fractionOutput);
            writer.append("\n");
        }
        writer.flush();
        writer.close();

        //Done
        System.exit(0);
    }

    public static class DataOut
    {
        long floatTime;
        long doubleTime;
        long fractionTime;

        double move;
        double expected;

        double floatOutput;
        double doubleOutput;
        double fractionOutput;
    }

    public static void outputResult(long nano, double result, BigDecimal expected)
    {
        long us = ((nano % 1000000000) % 1000000) / 1000;
        long ns = ((nano % 1000000000) % 1000000) % 1000;

        double delta = expected.subtract(new BigDecimal(result)).doubleValue();

        System.out.println(String.format("%3s us %3s ns %.10f %s%.10f", us, ns, result, delta >= 0 ? " " : "", delta));
    }
}
