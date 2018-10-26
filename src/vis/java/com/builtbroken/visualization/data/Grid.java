package com.builtbroken.visualization.data;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2018.
 */
public class Grid
{
    public final int[] dataArray;
    public final int size;

    public Grid(int size)
    {
        this.size = size;
        this.dataArray = new int[size * size];
    }

    public int getData(int x, int y)
    {
        if (!isValid(x, y))
        {
            throw new RuntimeException("Error: position is outside array P(" + x + ", " + y + ")");
        }
        return dataArray[getIndex(x, y)];
    }

    public void setData(int x, int y, int dataPoint)
    {
        if (!isValid(x, y))
        {
            throw new RuntimeException("Error: position is outside array P(" + x + ", " + y + ")");
        }
        dataArray[getIndex(x, y)] = dataPoint;
    }

    public int getIndex(int x, int y)
    {
        return x * size + y;
    }

    public boolean isValid(int x, int y)
    {
        return x >= 0 && x < size && y >= 0 && y < size;
    }


    public Grid copyLayer()
    {
        Grid layer = new Grid(size);
        for (int i = 0; i < dataArray.length; i++)
        {
            layer.dataArray[i] = this.dataArray[i];
        }
        return layer;
    }
}
