package resonantinduction.atomic.process.turbine;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import resonant.api.IBoilHandler;
import resonant.lib.prefab.turbine.TileTurbine;
import resonantinduction.atomic.Atomic;
import resonantinduction.core.Settings;

public class TileElectricTurbine extends TileTurbine implements IBoilHandler
{
    public TileElectricTurbine()
    {
        super();
        maxPower = 5000000;
    }

    @Override
    public void updateEntity()
    {
        if (getMultiBlock().isConstructed())
        {
            torque = defaultTorque * 500 * getArea();
        }
        else
        {
            torque = defaultTorque * 500;
        }

        super.updateEntity();
    }

    @Override
    public void onProduce()
    {
        energy.receiveEnergy((long) (power * Settings.turbineOutputMultiplier), true);
        produce();
    }

    @Override
    public void playSound()
    {
        if (this.worldObj.getWorldTime() % (Atomic.SECOND_IN_TICKS * 1.3F) == 0)
        {
            double maxVelocity = (getMaxPower() / torque) * 4;
            float percentage = angularVelocity * 4 / (float) maxVelocity;
            this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "atomicscience:turbine", percentage, 1.0F);
        }
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return from == ForgeDirection.DOWN && super.canFill(from, fluid);
    }
}
