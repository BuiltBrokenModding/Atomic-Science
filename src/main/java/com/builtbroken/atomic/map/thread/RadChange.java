package com.builtbroken.atomic.map.thread;

/**
 * Used to store data for thread
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2018.
 */
public class RadChange
{
    public final int dim;
    public final int x;
    public final int y;
    public final int z;
    public final int old_value;
    public final int new_value;

    public RadChange(int dim, int x, int y, int z, int old_value, int new_value)
    {
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.old_value = old_value;
        this.new_value = new_value;
    }
}
