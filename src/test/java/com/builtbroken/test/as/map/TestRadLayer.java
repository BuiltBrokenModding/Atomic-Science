package com.builtbroken.test.as.map;

import com.builtbroken.atomic.map.data.DataLayer;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/25/2018.
 */
public class TestRadLayer extends TestCase
{
    @Test //Test constructor
    public void testInit()
    {
        DataLayer layer = new DataLayer(1);
        assertEquals(1, layer.y_index);
        assertNotNull(layer.data);
        assertEquals(16 * 16, layer.data.length);

        for (int i = 0; i < layer.data.length; i++)
        {
            assertEquals(0, layer.data[i]);
        }
    }

    @Test //Test index method
    public void testIndex()
    {
        DataLayer layer = new DataLayer(1);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                int expected = x * 16 + z;
                assertEquals(expected, layer.index(x, z));
            }
        }
    }

    @Test //Test get and set methods
    public void testData()
    {
        DataLayer layer = new DataLayer(1);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                layer.setData(x, z, layer.index(x, z));
                assertEquals(layer.index(x, z), layer.getData(x, z));

                layer.setData(x, z, 0);
                assertEquals(0, layer.getData(x, z));
            }
        }
    }

    @Test //Test that get and set handle junk well
    public void testJunk()
    {
        DataLayer layer = new DataLayer(1);
        for(int i = 0; i < 16; i++)
        {
            assertFalse(layer.setData(i, -1, 1));
            assertEquals(0, layer.getData(i, -1));

            assertFalse(layer.setData(i, 16, 1));
            assertEquals(0, layer.getData(i, 16));

            assertFalse(layer.setData(-1, i, 1));
            assertEquals(0, layer.getData(-1, i));

            assertFalse(layer.setData(16, i, 1));
            assertEquals(0, layer.getData(16, i));
        }
    }

    @Test //Test isEmpty() method
    public void testEmpty()
    {
        DataLayer layer = new DataLayer(1);

        //Init should be empty
        assertTrue(layer.isEmpty());

        //Set should make it not empty
        layer.setData(0, 0, 5);
        assertFalse(layer.isEmpty());

        //Set zero should make it empty again
        layer.setData(0, 0, 0);
        assertTrue(layer.isEmpty());

        //Set zero check that is is zero still, also triggers prev = 0 & value = 0
        layer.setData(0, 0, 0);
        assertTrue(layer.isEmpty());
    }
}
