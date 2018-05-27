package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.api.item.IFuelRodItem;
import com.builtbroken.atomic.api.reactor.IReactor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Simple fuel rod
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public class ItemFuelRod extends ItemRadioactive implements IFuelRodItem
{
    /** Time in ticks the fuel can run, when full */
    public final int maxFuelRuntime;
    /** Radioactivity of the fuel rod when the reactor is active */
    public final int reactorRadioactivity;
    /** Heat of the fuel rod when the reactor is active */
    public final int reactorHeatOutput;

    public ItemFuelRod(String name, String texture, int maxFuelRuntime, int radioactiveMaterialValue, int reactorRadioactivity, int reactorHeatOutput)
    {
        super(name, texture, radioactiveMaterialValue);
        this.maxFuelRuntime = maxFuelRuntime;
        this.reactorRadioactivity = reactorRadioactivity;
        this.reactorHeatOutput = reactorHeatOutput;
    }

    @Override
    public int getFuelRodRuntime(ItemStack stack, IReactor reactor)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("fuelTimer", getMaxFuelRodRuntime(stack, reactor));
        }
        return stack.getTagCompound().getInteger("fuelTimer");
    }

    @Override
    public int getMaxFuelRodRuntime(ItemStack stack, IReactor reactor)
    {
        return maxFuelRuntime;
    }

    @Override
    public int getHeatOutput(ItemStack stack, IReactor reactor)
    {
        return reactorHeatOutput;
    }

    @Override
    public int getRadioactiveMaterial(ItemStack stack, IReactor reactor)
    {
        return reactorRadioactivity;
    }

    @Override
    public ItemStack onReactorTick(IReactor reactor, ItemStack stack, int tick, int fuelTick)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("fuelTimer", fuelTick - 1);
        return stack;
    }
}
