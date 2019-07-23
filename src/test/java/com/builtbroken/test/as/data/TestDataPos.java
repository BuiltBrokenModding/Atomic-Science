package com.builtbroken.test.as.data;

import com.builtbroken.atomic.map.data.DataPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-07-23.
 */
public class TestDataPos
{
    @Test
    public void testHash()
    {
        for (int i = 0; i < 1000; i++)
        {
            int x = (int) (Math.random() * 1000 - Math.random() * 1000);
            int y = (int) (Math.random() * 1000 - Math.random() * 1000);
            int z = (int) (Math.random() * 1000 - Math.random() * 1000);

            ///Create
            DataPos pos = DataPos.get(x, y, z);
            DataPos pos2 = DataPos.get(x, y, z);

            //Compare
            Assertions.assertEquals(pos, pos2);
            Assertions.assertEquals(pos.hashCode(), pos2.hashCode());

            //Recycle
            pos.dispose();
            pos2.dispose();
        }
    }

    @Test
    public void testMap()
    {
        Map<DataPos, Boolean> map = new HashMap();
        for (int i = 0; i < 1000; i++)
        {
            int x = (int) (Math.random() * 1000 - Math.random() * 1000);
            int y = (int) (Math.random() * 1000 - Math.random() * 1000);
            int z = (int) (Math.random() * 1000 - Math.random() * 1000);

            //Insert
            DataPos pos = DataPos.get(x, y, z);
            map.put(pos, true);

            //Check
            DataPos pos2 = DataPos.get(x, y, z);
            Assertions.assertTrue(map.containsKey(pos2));

            //Recycle
            pos2.dispose();
        }
    }
}
