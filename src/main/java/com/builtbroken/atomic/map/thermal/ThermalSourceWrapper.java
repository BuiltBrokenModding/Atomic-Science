package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.api.thermal.IHeatSource;
import com.builtbroken.atomic.map.data.DataSourceWrapper;

/**
 * Used to wrapper sources in order to track changes between ticks
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public class ThermalSourceWrapper extends DataSourceWrapper<IHeatSource>
{
    public int heatValue;

    public ThermalSourceWrapper(IHeatSource source)
    {
        super(source);
    }

    @Override
    public boolean hasSourceChanged()
    {
        return super.hasSourceChanged() || heatValue != source.getHeatGenerated();
    }

    @Override
    public void logCurrentData()
    {
        super.logCurrentData();
        heatValue = source.getHeatGenerated();
    }
}
