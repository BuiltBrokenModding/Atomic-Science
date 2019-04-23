package com.builtbroken.atomic.lib.fluid;

import com.builtbroken.atomic.lib.SideSettings;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2018.
 */
public class FluidSideWrapper implements IFluidHandler
{
    protected final HashMap<SideSettings, IFluidTank> sideToTank = new HashMap();
    protected final HashMap<SideSettings, Boolean> sideToType = new HashMap();

    public final EnumFacing side;

    public FluidSideWrapper(EnumFacing side)
    {
        this.side = side;
    }

    public void add(SideSettings sideSettings, IFluidTank tank, boolean output)
    {
        sideToTank.put(sideSettings, tank);
        sideToType.put(sideSettings, output);
    }


    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        return FluidTankProperties.convert(sideToTank.entrySet().stream()
                .filter(entry -> entry.getKey().get(side))
                .map(entry -> entry.getValue().getInfo())
                .toArray(FluidTankInfo[]::new));
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        int fill = 0;
        for (Map.Entry<SideSettings, IFluidTank> entry : sideToTank.entrySet())
        {
            if (!sideToType.get(entry.getKey()) && entry.getKey().get(side) && resource != null && resource.amount > 0)
            {
                fill += entry.getValue().fill(resource, doFill);
            }
        }
        return fill;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        for (Map.Entry<SideSettings, IFluidTank> entry : sideToTank.entrySet()) //TODO improve handling to allow several tanks drained per pass
        {
            if (sideToType.get(entry.getKey()) && entry.getKey().get(side) && entry.getValue().getFluid() != null && entry.getValue().getFluid().isFluidEqual(resource))
            {
               return entry.getValue().drain(resource.amount, doDrain);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        for (Map.Entry<SideSettings, IFluidTank> entry : sideToTank.entrySet()) //TODO improve handling to allow several tanks drained per pass
        {
            if (sideToType.get(entry.getKey()) && entry.getKey().get(side) && entry.getValue().getFluid() != null)
            {
                return entry.getValue().drain(maxDrain, doDrain);
            }
        }
        return null;
    }

    public boolean hasTank()
    {
        return sideToTank.entrySet().stream().anyMatch(entry -> entry.getKey().get(side));
    }
}
