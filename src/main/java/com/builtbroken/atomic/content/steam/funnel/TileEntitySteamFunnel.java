package com.builtbroken.atomic.content.steam.funnel;

import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.steam.TileEntitySteamInput;
import net.minecraftforge.fluids.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2018.
 */
public class TileEntitySteamFunnel extends TileEntitySteamInput implements IFluidTank
{
    private final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);

    @Override
    protected void update(int ticks)
    {
        super.update(ticks);
        //TODO output steam
    }

    @Override
    public FluidStack getFluid()
    {
        return tank.getFluid();
    }

    @Override
    public int getFluidAmount()
    {
        return tank.getFluidAmount();
    }

    @Override
    public int getCapacity()
    {
        return tank.getCapacity();
    }

    @Override
    public FluidTankInfo getInfo()
    {
        return tank.getInfo();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if (resource != null && resource.getFluid() == ASFluids.STEAM.fluid)
        {
            return tank.fill(resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        return tank.drain(maxDrain, doDrain);
    }
}
