package com.builtbroken.atomic.content.machines.accelerator.gun;

import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import net.minecraft.util.EnumFacing;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2018.
 */
public class TileEntityAcceleratorGun extends TileEntityPrefab
{
    protected EnumFacing direction;

    public EnumFacing getDirection()
    {
        if (direction == null)
        {
            direction = world.getBlockState(getPos()).getValue(BlockAcceleratorGun.ROTATION_PROP);
        }
        return direction;
    }
}
