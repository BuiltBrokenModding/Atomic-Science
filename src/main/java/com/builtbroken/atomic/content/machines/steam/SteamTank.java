package com.builtbroken.atomic.content.machines.steam;

import com.builtbroken.atomic.content.ASFluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/16/2018.
 */
public class SteamTank extends FluidTank
{
    public SteamTank(TileEntity tile, int capacity)
    {
        super(capacity);
        this.tile = tile;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if (resource != null && resource.getFluid() == ASFluids.STEAM.fluid)
        {
            return super.fill(resource, doFill);
        }
        return 0;
    }
}
