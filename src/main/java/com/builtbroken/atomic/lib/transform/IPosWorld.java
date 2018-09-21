package com.builtbroken.atomic.lib.transform;

import com.builtbroken.atomic.lib.transform.vector.Location;
import com.builtbroken.atomic.lib.transform.vector.Pos;
import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Useful interface to define that an object has a 3D location, and a defined world.
 *
 * @author DarkGuardsman
 */
public interface IPosWorld extends IPos3D
{
    default World world()
    {
        return DimensionManager.getWorld(dim());
    }

    int dim();

    /**
     * Converts the object to a location object.
     *
     * @return location object
     */
    default Location toLocation()
    {
        return this instanceof Location ? (Location) this : new Location(this);
    }

    /**
     * Converts the object to a position object.
     *
     * @return position object
     */
    default Pos toPos()
    {
        return new Pos(x(), y(), z());
    }
}
