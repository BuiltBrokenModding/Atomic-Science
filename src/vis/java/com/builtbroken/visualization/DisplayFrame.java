package com.builtbroken.visualization;

import com.builtbroken.visualization.component.RenderPanel;
import com.builtbroken.visualization.data.EnumDirections;
import com.builtbroken.visualization.data.Grid;
import com.builtbroken.visualization.data.GridPoint;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2018.
 */
public class DisplayFrame extends JFrame
{
    ArrayList<Grid> renderLayers = new ArrayList();
    int layerIndex = 0;

    RenderPanel renderPanel;
    JLabel renderIndexLabel;
    JTextField playSpeedField;


    boolean currentlyPlaying;

    Timer timer = new Timer(500, action -> nextLayer());

    public DisplayFrame()
    {
        //Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setMinimumSize(new Dimension(800, 800));
        setLocation(200, 200);
        setTitle("Visualization - heat pathfinder");

        add(buildCenter());

        pack();
    }

    protected JPanel buildCenter()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(renderPanel = new RenderPanel(), BorderLayout.CENTER);
        renderPanel.setMinimumSize(new Dimension(600, 600));
        renderPanel.setPreferredSize(new Dimension(600, 600));

        panel.add(buildControls(), BorderLayout.WEST);
        return panel;
    }

    protected JPanel buildControls()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20, 2));
        panel.setMinimumSize(new Dimension(200, 0));

        //--------------------------------------------------------

        Button button = new Button("Generate Data");
        button.addActionListener(e -> generateData());
        panel.add(button);
        panel.add(new JPanel());

        //--------------------------------------------------------


        //Spacer
        panel.add(new JPanel());
        panel.add(new JPanel());

        //--------------------------------------------------------

        final Button playButton = new Button("Play");
        playButton.addActionListener(e -> play(playButton));
        panel.add(playButton);

        panel.add(playSpeedField = new JTextField());
        playSpeedField.setText("" + timer.getDelay());

        //--------------------------------------------------------

        //Spacer
        panel.add(new JPanel());
        panel.add(new JPanel());

        //--------------------------------------------------------
        panel.add(new JLabel("Layer: "));
        panel.add(renderIndexLabel = new JLabel("0"));

        button = new Button("Prev");
        button.addActionListener(e -> prevLayer());
        panel.add(button);

        button = new Button("Next");
        button.addActionListener(e -> nextLayer());
        panel.add(button);

        return panel;
    }

    protected void nextLayer()
    {
        if (layerIndex < (renderLayers.size() - 1))
        {
            layerIndex++;
            updateRenderPanel();
        }
    }

    protected void prevLayer()
    {
        if (layerIndex > 0)
        {
            layerIndex--;
            updateRenderPanel();
        }
    }

    protected void play(Button button)
    {
        if (renderLayers.size() != 0)
        {
            if (currentlyPlaying)
            {
                currentlyPlaying = false;
                timer.stop();
                button.setLabel("Play");
            }
            else
            {
                //Set play speed
                timer.setDelay(Integer.parseInt(playSpeedField.getText().trim()));

                //Note we are playing
                currentlyPlaying = true;

                //Start timer
                timer.start();

                //Change text to note the button can stop play
                button.setLabel("Stop");
            }
        }
    }

    protected void updateRenderPanel()
    {
        if (layerIndex >= 0 && layerIndex < renderLayers.size())
        {
            renderIndexLabel.setText("" + layerIndex);
            renderPanel.currentLayer = renderLayers.get(layerIndex);
            renderPanel.repaint();
        }
    }

    protected void generateData()
    {
        System.out.println("Generating data");

        //Clear old data
        renderLayers.clear();
        layerIndex = 0;

        final int size = 101;

        //Generate data
        Grid grid = new Grid(size);
        doNormalPathfinder(grid, 51, 51);

        //Update render panel
        updateRenderPanel();

        System.out.println("Done generating...");
        renderPanel.repaint();
    }

    protected void doNormalPathfinder(Grid grid, int startX, int startY)
    {
        final Queue<GridPoint> queue = new LinkedList();

        final GridPoint center = GridPoint.get(startX, startY);
        queue.add(center);

        final ArrayList<GridPoint> tempList = new ArrayList(4);

        while (!queue.isEmpty())
        {
            //Get next
            final GridPoint node = queue.poll();

            //Mark as current node
            grid.setData(node.x, node.y, 3);

            //Path to next tiles
            for (EnumDirections dir : EnumDirections.values())
            {
                final int x = node.x + dir.xDelta;
                final int y = node.y + dir.yDelta;

                //Ensure is inside view
                if (grid.isValid(x, y))
                {
                    final GridPoint nextPos = GridPoint.get(x, y);

                    //If have not pathed, add to path list
                    if (grid.getData(x, y) == 0)
                    {
                        //Mark as next node
                        grid.setData(x, y, 5);

                        //Add to queue
                        queue.offer(nextPos);

                        tempList.add(nextPos);
                    }
                    else
                    {
                        nextPos.dispose();
                    }
                }
            }

            //Take picture
            recordLayer(grid);

            //Reset nodes to blue
            tempList.forEach(pos -> grid.setData(pos.x, pos.y, 2));
            tempList.clear();

            //Mark as completed
            if (node == center)
            {
                grid.setData(node.x, node.y, 1);
            }
            else
            {
                grid.setData(node.x, node.y, 4);
            }
        }
    }

    protected void recordLayer(Grid currentLayer)
    {
        renderLayers.add(currentLayer.copyLayer());
    }
}
