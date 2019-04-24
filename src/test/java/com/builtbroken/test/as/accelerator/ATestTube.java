package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.tube.normal.TileEntityAcceleratorTube;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class ATestTube extends TileEntityAcceleratorTube
{

    public final TileEntity[] tiles = new TileEntity[6];

    @Override //Override normal access so we can force it to return what we want
    public TileEntity getTileEntity(EnumFacing side)
    {
        Assertions.assertNotNull(side, "ATestTube#getTileEntity(EnumFacing) should never get null params");
        return tiles[side.ordinal()];
    }

    public void setTiles(EnumFacing side, TileEntity tile)
    {
        tiles[side.ordinal()] = tile;
        if(tile instanceof TileEntityAcceleratorTube)
        {
            acceleratorNode.connect(((TileEntityAcceleratorTube)tile).getNode(), side);
        }
    }

    public AcceleratorNode getNode()
    {
        return acceleratorNode;
    }
}
