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

    public ItemFuelRod(String name, String texture, int maxFuelRuntime, int radioactiveMaterialValue)
    {
        super(name, texture, radioactiveMaterialValue);
        this.maxFuelRuntime = maxFuelRuntime;
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
        //To boil 1 block of water takes 1,562,379.05 KJ
        return 1562379 * 10;
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
