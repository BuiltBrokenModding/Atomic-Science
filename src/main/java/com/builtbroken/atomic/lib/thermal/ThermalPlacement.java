package com.builtbroken.atomic.lib.thermal;

import com.builtbroken.atomic.lib.placement.BlockPlacement;
import com.builtbroken.atomic.map.MapHandler;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/12/2018.
 */
public class ThermalPlacement extends BlockPlacement
{
    ThermalData data;
    long energyCheck;

    public ThermalPlacement(World world, int x, int y, int z, ThermalData data, long energyCheck)
    {
        super(world, x, y, z, data.changeBlock, data.changeMeta);
        this.data = data;
        this.energyCheck = energyCheck;
    }

    @Override
    protected boolean canDoAction()
    {
        return MapHandler.THERMAL_MAP.getJoules(world(), x, y, z) >= energyCheck;
    }
}
