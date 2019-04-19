package com.builtbroken.test.as.accelerator.layout;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.tube.TileEntityAcceleratorTube;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class ATestTube extends TileEntityAcceleratorTube
{
    public final TileEntity[] tiles = new TileEntity[6];

    @Override //Override normal access so we can force it to return what we want
    public TileEntity getTileEntity(EnumFacing side)
    {
        return tiles[side.ordinal()];
    }

    public void setDirection(EnumFacing direction)
    {
        this.direction = direction;
    }

    public void setTiles(EnumFacing side, TileEntity tile)
    {
        tiles[side.ordinal()] = tile;
    }

    public void setConnectionType(TubeConnectionType type)
    {
        this.connectionType = type;
    }
}
