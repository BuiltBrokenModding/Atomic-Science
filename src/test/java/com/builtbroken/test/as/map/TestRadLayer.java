package com.builtbroken.test.as.map;

import com.builtbroken.atomic.map.RadiationLayer;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/25/2018.
 */
public class TestRadLayer extends TestCase
{
    @Test
    public void testInit()
    {
        RadiationLayer layer = new RadiationLayer(1);
        assertEquals(1, layer.layer);
        assertNotNull(layer.data);
        assertEquals(16 * 16, layer.data.length);

        for (int i = 0; i < layer.data.length; i++)
        {
            assertEquals(0, layer.data[i]);
        }
    }

    @Test
    public void testIndex()
    {
        RadiationLayer layer = new RadiationLayer(1);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                int expected = x * 16 + z;
                assertEquals(expected, layer.index(x, z));
            }
        }
    }

    @Test
    public void testData()
    {
        RadiationLayer layer = new RadiationLayer(1);
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

    @Test
    public void testEmpty()
    {
        RadiationLayer layer = new RadiationLayer(1);

        //Init should be empty
        assertTrue(layer.isEmpty());

        //Set should make it not empty
        layer.setData(0, 0, 5);
        assertFalse(layer.isEmpty());

        //Set zero should make it empty again
        layer.setData(0, 0, 0);
        assertTrue(layer.isEmpty());
    }
}
