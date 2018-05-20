package com.builtbroken.atomic.proxy.rf;

import cofh.api.energy.IEnergyContainerItem;
import com.builtbroken.atomic.lib.power.PowerHandler;
import net.minecraft.item.ItemStack;

/**
 * Handles RF API for items
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class PowerHandlerRFItem extends PowerHandler
{
    @Override
    public boolean canHandle(ItemStack stack)
    {
        return stack.getItem() instanceof IEnergyContainerItem;
    }

    @Override
    public int addPower(ItemStack stack, int power, boolean doAction)
    {
        if (stack.getItem() instanceof IEnergyContainerItem)
        {
            return ProxyRedstoneFlux.toUE(((IEnergyContainerItem) stack.getItem()).receiveEnergy(stack, ProxyRedstoneFlux.toRF(power), !doAction));
        }
        return 0;
    }

    @Override
    public int removePower(ItemStack stack, int power, boolean doAction)
    {
        if (stack.getItem() instanceof IEnergyContainerItem)
        {
            return ProxyRedstoneFlux.toUE(((IEnergyContainerItem) stack.getItem()).extractEnergy(stack, ProxyRedstoneFlux.toRF(power), !doAction));
        }
        return 0;
    }

    @Override
    public int getPowerStored(ItemStack stack)
    {
        if (stack.getItem() instanceof IEnergyContainerItem)
        {
            return ProxyRedstoneFlux.toUE(((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack));
        }
        return 0;
    }
}
