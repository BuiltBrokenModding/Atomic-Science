package com.builtbroken.atomic.api.accelerator;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2019.
 */
public class AcceleratorHelpers
{
    public static float getMagnetPower(TileEntity tileEntity, EnumFacing facing)
    {
        IAcceleratorMagnet magnet = getAcceleratorMagnet(tileEntity, facing);
        if(magnet != null)
        {
            return magnet.getRawMagneticPower();
        }
        return 0;
    }

    public static float getMagnetPower(IAcceleratorTube tube, TileEntity tileEntity, EnumFacing facing)
    {
        IAcceleratorMagnet magnet = getAcceleratorMagnet(tileEntity, facing);
        if(magnet != null)
        {
            return magnet.getActualMagneticPower(tube);
        }
        return 0;
    }

    public static IAcceleratorTube getAcceleratorTube(TileEntity tileEntity, EnumFacing facing)
    {
        if (tileEntity != null && tileEntity.hasCapability(AtomicScienceAPI.ACCELERATOR_TUBE_CAPABILITY, facing))
        {
            return tileEntity.getCapability(AtomicScienceAPI.ACCELERATOR_TUBE_CAPABILITY, facing);
        }
        return null;
    }

    public static IAcceleratorMagnet getAcceleratorMagnet(TileEntity tileEntity, EnumFacing facing)
    {
        if (tileEntity != null && tileEntity.hasCapability(AtomicScienceAPI.ACCELERATOR_MAGNET_CAPABILITY, facing))
        {
            return tileEntity.getCapability(AtomicScienceAPI.ACCELERATOR_MAGNET_CAPABILITY, facing);
        }
        return null;
    }
}
