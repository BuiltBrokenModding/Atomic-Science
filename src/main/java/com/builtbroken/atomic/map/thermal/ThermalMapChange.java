package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.map.data.DataMap;
import com.builtbroken.atomic.map.data.DataPos;

import java.util.HashMap;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
public class ThermalMapChange
{
    private final HashMap<DataPos, DataPos> old_data;
    private final HashMap<DataPos, DataPos> new_data;
    private final DataMap map;

    public final int size;

    public ThermalMapChange(DataMap map, HashMap<DataPos, DataPos> old_data, HashMap<DataPos, DataPos> new_data)
    {
        this.map = map;
        this.old_data = old_data;
        this.new_data = new_data;
        size = old_data.size() + new_data.size();
    }

    public void pop()
    {
        //Clear old data
        for (Map.Entry<DataPos, DataPos> entry : old_data.entrySet())
        {
            final DataPos dataPos = entry.getKey();
            int heat = map.getData(dataPos.xi(), dataPos.yi(), dataPos.zi());

            //Remove heat
            heat -= Math.max(entry.getValue().x * 0.1, entry.getValue().x - entry.getValue().y);

            //Update map
            map.setData(dataPos.xi(), dataPos.yi(), dataPos.zi(), Math.max(0, heat));

            //Recycle for next path
            dataPos.dispose();
            entry.getValue().dispose();
        }
        old_data.clear();

        //Add new data
        for (Map.Entry<DataPos, DataPos> entry : new_data.entrySet())
        {
            final DataPos dataPos = entry.getKey();
            int heat = map.getData(dataPos.xi(), dataPos.yi(), dataPos.zi());

            //add heat
            heat += Math.max(entry.getValue().x * 0.1, entry.getValue().x - entry.getValue().y);

            //Update map
            map.setData(dataPos.xi(), dataPos.yi(), dataPos.zi(), Math.max(0, heat));

            //Recycle for next path
            dataPos.dispose();
            entry.getValue().dispose();
        }
        new_data.clear();
    }
}
