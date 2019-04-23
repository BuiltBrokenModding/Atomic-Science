package com.builtbroken.atomic.content.machines.steam.funnel;

import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.machines.steam.SteamTank;
import com.builtbroken.atomic.content.machines.steam.TileEntitySteamInput;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

/**
 * Simple tile to collect and distribute steam
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2018.
 */
public class TileEntitySteamFunnel extends TileEntitySteamInput
{
    private FluidTank tank;

    public TileEntitySteamFunnel()
    {
        tank = new SteamTank(this, Fluid.BUCKET_VOLUME * 10);
    }

    public int getFluidAmount()
    {
        return tank.getFluidAmount();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return (T) tank;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    protected void update(int ticks, boolean isClient)
    {
        super.update(ticks, isClient);
        if(!isClient)
        {
            if (tank.getFluid() != null && tank.getFluid().getFluid() != ASFluids.STEAM.fluid)
            {
                tank.drain(tank.getCapacity(), true);
            }

            //Output steam to connected tiles
            if (tank.getFluid() != null && tank.getFluidAmount() > 0)
            {
                int amountToGive = tank.getFluidAmount();
                for (EnumFacing direction : EnumFacing.VALUES)
                {
                    BlockPos blockPos = getPos().add(direction.getDirectionVec());

                    TileEntity tile = world.getTileEntity(blockPos);
                    if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction))
                    {
                        IFluidHandler fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction);
                        if (fluidHandler != null)
                        {
                            int give = amountToGive / 6;
                            give = fluidHandler.fill(new FluidStack(tank.getFluid().getFluid(), give), true);
                            amountToGive -= give;
                        }
                    }
                }
                tank.drain(tank.getFluidAmount() - amountToGive, true);
            }

            //Generate steam
            if (getSteamGeneration() > 0)
            {
                tank.fill(new FluidStack(ASFluids.STEAM.fluid, getSteamGeneration()), true);
            }
        }
    }

    //-------------------------------------------------
    //-----Data handling ------------------------------
    //-------------------------------------------------

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        tank.writeToNBT(nbt.getCompoundTag("fluid_tank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setTag("fluid_tank", tank.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(nbt);
    }
}
