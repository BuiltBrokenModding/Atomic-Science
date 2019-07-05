package com.builtbroken.atomic.map.thermal.node;

import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.exposure.node.RadiationSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Thermal source taken from the map itself
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 7/5/2019.
 */
public class ThermalSourceMap extends ThermalSource<BlockPos>
{
    private int value;
    private int dim;
    private BlockPos pos;

    public ThermalSourceMap(int dim, BlockPos host, int value) //TODO apply flywheel pattern
    {
        this.pos = host;
        this.dim = dim;
        this.value = value;
    }

    @Override
    public int getHeatGenerated()
    {
        return value;
    }

    @Override
    public double z()
    {
        return getHost().getZ() + 0.5;
    }

    @Override
    public double x()
    {
        return getHost().getX() + 0.5;
    }

    @Override
    public double y()
    {
        return getHost().getY() + 0.5;
    }

    @Override
    public int zi()
    {
        return getHost().getZ();
    }

    @Override
    public int xi()
    {
        return getHost().getX();
    }

    @Override
    public int yi()
    {
        return getHost().getY();
    }

    @Override
    public boolean isStillValid()
    {
        return world() != null && world().isBlockLoaded(getHost()) && value == getType().getValue(MapHandler.GLOBAL_DATA_MAP.getData(world(), getHost()));
    }

    @Override
    public World world()
    {
        return DimensionManager.getWorld(dim);
    }

    @Override
    public BlockPos getHost()
    {
        return pos;
    }
}
