package com.builtbroken.atomic.content.machines.accelerator.exit;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.tube.TileEntityAcceleratorTubePrefab;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2019.
 */
public class TileEntityAcceleratorExit extends TileEntityAcceleratorTubePrefab
{
    @Override
    public void onLoad()
    {
        if (isServer())
        {
            acceleratorNode.setData(getPos(), getDirection(), TubeConnectionType.END_CAP);
        }
    }
}
