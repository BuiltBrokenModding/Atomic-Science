package com.builtbroken.atomic.lib.math;

import com.builtbroken.atomic.lib.transform.SetPosFloatFunction;
import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/13/2019.
 */
public class BlockPosHelpers
{

    /**
     * Gets the different between the position and the center of the block
     *
     * @param pos    - block's position
     * @param entity - entity's position
     * @return -0.5 to 0.5
     */
    public static float getCenterDeltaX(BlockPos pos, IPos3D entity)
    {
        return entity.xf() - (pos.getX() + MathConstF.CENTER);
    }

    /**
     * Gets the different between the position and the center of the block
     *
     * @param pos    - block's position
     * @param entity - entity's position
     * @return -0.5 to 0.5
     */
    public static float getCenterDeltaZ(BlockPos pos, IPos3D entity)
    {
        return entity.zf() - (pos.getZ() + MathConstF.CENTER);
    }

    /**
     * Gets the different between the position and the center of the block
     *
     * @param pos    - block's position
     * @param entity - entity's position
     * @return -0.5 to 0.5
     */
    public static float getCenterDeltaY(BlockPos pos, IPos3D entity)
    {
        return entity.yf() - (pos.getY() + MathConstF.CENTER);
    }

    /**
     * Helper to center data based on a block's position
     *
     * @param pos           - block's position
     * @param floatFunction - function to invoke to center
     */
    public static void center(BlockPos pos, SetPosFloatFunction floatFunction)
    {
        floatFunction.set(pos.getX() + MathConstF.CENTER, pos.getY() + MathConstF.CENTER, pos.getZ() + MathConstF.CENTER);
    }
}
