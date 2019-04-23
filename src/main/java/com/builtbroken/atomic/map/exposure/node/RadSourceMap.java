package com.builtbroken.atomic.map.exposure.node;

import com.builtbroken.atomic.map.MapHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Radiation source taken from the map itself
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public class RadSourceMap extends RadiationSource<BlockPos>
{
    private int value;
    private int dim;

    public RadSourceMap(int dim, BlockPos host, int value) //TODO apply flywheel pattern
    {
        super(host);
        this.dim = dim;
        this.value = value;
    }

    @Override
    public int getRadioactiveMaterial()
    {
        return value;
    }

    @Override
    public double z()
    {
        return host.getZ() + 0.5;
    }

    @Override
    public double x()
    {
        return host.getX() + 0.5;
    }

    @Override
    public double y()
    {
        return host.getY() + 0.5;
    }

    @Override
    public int zi()
    {
        return host.getZ();
    }

    @Override
    public int xi()
    {
        return host.getX();
    }

    @Override
    public int yi()
    {
        return host.getY();
    }

    @Override
    public boolean isStillValid()
    {
        return world() != null && world().isBlockLoaded(host) && value == getType().getValue(MapHandler.GLOBAL_DATA_MAP.getData(world(), host));
    }

    @Override
    public World world()
    {
        return DimensionManager.getWorld(dim);
    }
}
