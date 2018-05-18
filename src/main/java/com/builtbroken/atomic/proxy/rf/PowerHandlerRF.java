package com.builtbroken.atomic.proxy.rf;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.builtbroken.atomic.lib.power.PowerHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class PowerHandlerRF extends PowerHandler
{
    float conversationRateToRF = 1;

    @Override
    public boolean canHandle(ForgeDirection side, TileEntity tile)
    {
        return (tile instanceof IEnergyProvider || tile instanceof IEnergyReceiver) && ((IEnergyConnection) tile).canConnectEnergy(side);
    }

    @Override
    public int addPower(ForgeDirection side, Object object, int power, boolean doAction)
    {
        if (object instanceof IEnergyReceiver)
        {
            return toUE(((IEnergyReceiver) object).receiveEnergy(side, toPower(power), !doAction));
        }
        return 0;
    }

    @Override
    public int removePower(ForgeDirection side, Object object, int power, boolean doAction)
    {
        if (object instanceof IEnergyProvider)
        {
            return toUE(((IEnergyProvider) object).extractEnergy(side, toPower(power), !doAction));
        }
        return 0;
    }

    public int toPower(int fromUE)
    {
        return (int) Math.floor(fromUE * conversationRateToRF);
    }

    public int toUE(int fromPower)
    {
        return (int) Math.floor(fromPower / conversationRateToRF);
    }
}
