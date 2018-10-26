package com.builtbroken.visualization.component;

import com.builtbroken.visualization.data.Grid;

import javax.swing.*;
import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2018.
 */
public class RenderPanel extends JPanel
{
    public Grid currentLayer;

    public int paddingInside = 5;

    public RenderPanel()
    {
        addComponentListener(new ResizeListenerBoxSize());
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //TODO scale box render to be even at all times

        //Draw outside boarder
        g2.setPaint(Color.BLACK);
        drawBorder(g2, 1);

        //Draw inside boarder
        g2.setPaint(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        drawBorder(g2, paddingInside);

        //Reset
        g2.setStroke(new BasicStroke(1));

        drawLayer(g2);
    }

    protected void drawLayer(Graphics2D g2)
    {
        if (currentLayer != null)
        {
            int numberOfBoxes = currentLayer.size;
            int spacing = 1;
            int spacingTotal = (numberOfBoxes - 1) * spacing;

            int width = getWidth() - paddingInside * 2 - spacing * 2 - spacingTotal; //width - padding - separation from padding - spacing between boxes
            int height = getHeight() - paddingInside * 2 - spacing * 2 - spacingTotal;

            int sizeX = width / numberOfBoxes;
            int sizeY = height / numberOfBoxes;

            for (int x = 0; x < numberOfBoxes; x++)
            {
                for (int y = 0; y < numberOfBoxes; y++)
                {
                    int drawX = paddingInside + spacing + x * spacing + x * sizeX;
                    int drawY = paddingInside + spacing + y * spacing + y * sizeY;

                    int data = currentLayer.getData(x, y);

                    if (data != 0)
                    {
                        //Already pathed
                        if (data == 1)
                        {
                            g2.setPaint(Color.RED);
                        }
                        //Next pathed
                        else if (data == 2)
                        {
                            g2.setPaint(Color.BLUE);
                        }
                        //current pathed
                        else if (data == 3)
                        {
                            g2.setPaint(Color.YELLOW);
                        }
                        //Center
                        else if (data == 4)
                        {
                            g2.setPaint(Color.GREEN);
                        }
                        //Center
                        else if (data == 5)
                        {
                            g2.setPaint(Color.PINK);
                        }
                        g2.fillRect(drawX, drawY, sizeX, sizeY);
                    }
                }
            }
        }
    }

    /**
     * Draws a border around the component to define the edge
     *
     * @param g2
     */
    protected void drawBorder(Graphics2D g2, int padding)
    {
        g2.drawRect(padding, padding, getWidth() - padding * 2, getHeight() - padding * 2);
    }
}
