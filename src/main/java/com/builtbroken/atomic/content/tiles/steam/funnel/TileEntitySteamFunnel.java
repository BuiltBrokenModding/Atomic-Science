package com.builtbroken.atomic.content.tiles.steam.funnel;

import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.tiles.steam.SteamTank;
import com.builtbroken.atomic.content.tiles.steam.TileEntitySteamInput;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Simple tile to collect and distribute steam
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2018.
 */
public class TileEntitySteamFunnel extends TileEntitySteamInput implements IFluidHandler
{
    private FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);

    public TileEntitySteamFunnel()
    {
        tank = new SteamTank(this, FluidContainerRegistry.BUCKET_VOLUME * 10);
    }

    @Override
    public void firstTick()
    {
        super.firstTick();
    }

    @Override
    protected void update(int ticks)
    {
        super.update(ticks);

        if (tank.getFluid() != null && tank.getFluid().getFluid() != ASFluids.STEAM.fluid)
        {
            tank.drain(tank.getCapacity(), true);
        }

        //Output steam to connected tiles
        if (tank.getFluid() != null && tank.getFluidAmount() > 0)
        {
            int amountToGive = tank.getFluidAmount();
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                int x = xCoord + direction.offsetX;
                int y = yCoord + direction.offsetY;
                int z = zCoord + direction.offsetZ;

                TileEntity tile = worldObj.getTileEntity(x, y, z);
                if (tile instanceof IFluidHandler)
                {
                    IFluidHandler fluidHandler = (IFluidHandler) tile;
                    if (fluidHandler.canFill(direction.getOpposite(), tank.getFluid().getFluid()))
                    {
                        int give = amountToGive / 6;
                        give = fluidHandler.fill(direction.getOpposite(), new FluidStack(tank.getFluid().getFluid(), give), true);
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
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setTag("fluid_tank", tank.writeToNBT(new NBTTagCompound()));
    }

    //-------------------------------------------------
    //-----Fluid tank handling ------------------------
    //-------------------------------------------------

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (resource != null && tank.getFluid() != null && resource.getFluid() == tank.getFluid().getFluid())
        {
            return drain(from, resource.amount, doDrain);
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return fluid == ASFluids.STEAM.fluid;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    public int getFluidAmount()
    {
        return tank.getFluidAmount();
    }
}
