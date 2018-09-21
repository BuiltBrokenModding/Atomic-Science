package com.builtbroken.test.as.map;

import com.builtbroken.atomic.lib.transform.vector.Pos;
import com.builtbroken.atomic.map.data.storage.DataChunk;
import junit.framework.TestCase;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases {@link DataChunk}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/25/2018.
 */
public class TestRadChunk extends TestCase
{
    @Test
    public void testInit()
    {
        DataChunk chunk = new DataChunk(1, 3, 2);
        assertEquals("Dim didn't set to 1", 1, chunk.dimension);
        assertEquals("X position didn't set to 3", 3, chunk.xPosition);
        assertEquals("Z position didn't set to 2", 2, chunk.zPosition);
    }

    @Test
    public void testSetGet()
    {
        //Test set all values, checks basic get & set in addition to resize
        DataChunk chunk = new DataChunk(1, 3, 2);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                for (int y = 0; y < 256; y++)
                {
                    try
                    {
                        //Set
                        int value = y * 256 + 16 * x + z;
                        assertTrue(String.format("Failed to set, %sx %sy %sz with %s", x, y, z, value), chunk.setValue(x, y, z, value));
                        int set = chunk.getValue(x, y, z);
                        assertEquals(String.format("Value was not set, %sx %sy %sz with %s", x, y, z, value), value, set);

                        //Reset
                        assertTrue(String.format("Failed to set, %sx %sy %sz with %s", x, y, z, 0), chunk.setValue(x, y, z, 0));
                        set = chunk.getValue(x, y, z);
                        assertEquals(String.format("Value was not set, %sx %sy %sz with %s", x, y, z, 0), 0, set);

                        //Check if layer y matches
                        assertEquals(y, chunk.getLayer(y).y_index);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        fail(String.format("Unexpected error, %sx %sy %sz", x, y, z));
                    }
                }
            }
        }
    }


    @Test
    public void testSetGetReverse()
    {
        //Test set all values reverse, checks resize
        DataChunk chunk = new DataChunk(1, 3, 2);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                for (int y = 255; y >= 0; y--)
                {
                    try
                    {
                        //Set
                        int value = y * 256 + 16 * x + z;
                        assertTrue(String.format("Failed to set, %sx %sy %sz with %s", x, y, z, value), chunk.setValue(x, y, z, value));
                        int set = chunk.getValue(x, y, z);
                        assertEquals(String.format("Value was not set, %sx %sy %sz with %s", x, y, z, value), value, set);

                        //Reset
                        assertTrue(String.format("Failed to set, %sx %sy %sz with %s", x, y, z, 0), chunk.setValue(x, y, z, 0));
                        set = chunk.getValue(x, y, z);
                        assertEquals(String.format("Value was not set, %sx %sy %sz with %s", x, y, z, 0), 0, set);

                        //Check if layer y matches
                        assertEquals(y, chunk.getLayer(y).y_index);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        fail(String.format("Unexpected error, %sx %sy %sz", x, y, z));
                    }
                }
            }
        }
    }

    @Test
    public void testSetGetEvery8()
    {
        //Test every 8, checks if resize works while skipping values
        DataChunk chunk = new DataChunk(1, 3, 2);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                for (int y = 0; y < 256; y += 8)
                {
                    try
                    {
                        //Set
                        int value = y * 256 + 16 * x + z;
                        assertTrue(String.format("Failed to set, %sx %sy %sz with %s", x, y, z, value), chunk.setValue(x, y, z, value));

                        //Get
                        int set = chunk.getValue(x, y, z);
                        assertEquals(String.format("Value was not set, %sx %sy %sz with %s", x, y, z, value), value, set);

                        //Reset
                        assertTrue(String.format("Failed to set, %sx %sy %sz with %s", x, y, z, 0), chunk.setValue(x, y, z, 0));

                        //Reset check
                        set = chunk.getValue(x, y, z);
                        assertEquals(String.format("Value was not set, %sx %sy %sz with %s", x, y, z, 0), 0, set);

                        //Check if layer y matches
                        assertEquals(y, chunk.getLayer(y).y_index);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        fail(String.format("Unexpected error, %sx %sy %sz", x, y, z));
                    }
                }
            }
        }
    }

    @Test
    public void testSetLargeJump()
    {
        //Test every 8, checks if resize works while skipping values
        DataChunk chunk = new DataChunk(1, 3, 2);
        assertTrue(chunk.setValue(5, 100, 5, 98));
        assertEquals(98, chunk.getValue(5, 100, 5));

        assertTrue(chunk.setValue(5, 72, 5, 94));
        assertEquals(94, chunk.getValue(5, 72, 5));

        assertTrue(chunk.setValue(5, 32, 5, 91));
        assertEquals(91, chunk.getValue(5, 32, 5));

        assertTrue(chunk.setValue(5, 0, 5, 31));
        assertEquals(31, chunk.getValue(5, 0, 5));
    }

    @Test
    public void testSetLargeJump2()
    {
        //Test every 8, checks if resize works while skipping values
        DataChunk chunk = new DataChunk(1, 3, 2);
        chunk.setValue(5, 238, 5, 98);
        chunk.setValue(5, 103, 5, 98);
        chunk.setValue(5, 93, 5, 98);
        chunk.setValue(5, 83, 5, 98);
        chunk.setValue(5, 73, 5, 98);

        assertTrue(chunk.setValue(5, 0, 5, 31));
        assertEquals(31, chunk.getValue(5, 0, 5));
    }

    @Test
    public void testSetRandom()
    {
        //Test every 8, checks if resize works while skipping values
        DataChunk chunk = new DataChunk(1, 3, 2);

        for (int i = 0; i < 10; i++)
        {
            List<Pos> positions = new ArrayList();
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    for (int y = 0; y < 256; y++)
                    {
                        positions.add(new Pos(x, y, z));
                    }
                }
            }
            Collections.shuffle(positions);
            Collections.shuffle(positions);

            for (Pos pos : positions)
            {
                int x = pos.xi();
                int y = pos.yi();
                int z = pos.zi();
                try
                {
                    //Set
                    int value = y * 256 + 16 * x + z;
                    assertTrue(String.format("Failed to set, %sx %sy %sz with %s", x, y, z, value), chunk.setValue(x, y, z, value));

                    //Get
                    int set = chunk.getValue(x, y, z);
                    assertEquals(String.format("Value was not set, %sx %sy %sz with %s", x, y, z, value), value, set);

                    //Reset
                    assertTrue(String.format("Failed to set, %sx %sy %sz with %s", x, y, z, 0), chunk.setValue(x, y, z, 0));

                    //Reset check
                    set = chunk.getValue(x, y, z);
                    assertEquals(String.format("Value was not set, %sx %sy %sz with %s", x, y, z, 0), 0, set);

                    //Check if layer y matches
                    assertEquals(y, chunk.getLayer(y).y_index);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    fail(String.format("Unexpected error, %sx %sy %sz", x, y, z));
                }
            }
        }
    }

    @Test
    public void testSaveLoad()
    {
        DataChunk chunk = new DataChunk(1, 3, 2);

        //Set test data
        chunk.setValue(0, 232, 2, 10);
        chunk.setValue(0, 51, 5, 13);
        chunk.setValue(7, 0, 0, 15);

        //Save
        NBTTagCompound saveData = new NBTTagCompound();
        chunk.save(saveData);

        //Test save
        assertEquals("Save data should have 3 keys", 3, saveData.getKeySet().size());
        assertTrue("Failed to save any layers", saveData.hasKey(DataChunk.NBT_LAYERS));
        assertEquals("Save data should have 3 layers", 3, saveData.getTagList(DataChunk.NBT_LAYERS, 10).tagCount());
        assertTrue("Failed to save y start", saveData.hasKey(DataChunk.NBT_Y_START));
        assertTrue("Failed to size of layer array", saveData.hasKey(DataChunk.NBT_SIZE));

        //Test load
        DataChunk chunk2 = new DataChunk(1, 3, 2);
        chunk2.load(saveData);

        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                for (int y = 0; y < 256; y++)
                {
                    assertEquals(String.format("%sx %sy %sz values should match", x, y, z), chunk.getValue(x, y, z), chunk2.getValue(x, y, z));
                }
            }
        }
    }
}
