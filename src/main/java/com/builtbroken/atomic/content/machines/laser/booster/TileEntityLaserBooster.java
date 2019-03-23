package com.builtbroken.atomic.content.machines.laser.booster;

import com.builtbroken.atomic.content.machines.TileEntityPowerInvMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2019.
 */
public class TileEntityLaserBooster extends TileEntityPowerInvMachine<IItemHandlerModifiable>
{
    @Override
    public int getEnergyUsage()
    {
        return 0;
    }

    @Nonnull
    @Override
    protected IItemHandlerModifiable createInventory()
    {
        return new ItemStackHandler(4)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return false;
            }
        };
    }

    @Override
    protected boolean canInventoryConnect(EnumFacing side)
    {
        return false;
    }

    @Override
    protected int inventorySize()
    {
        return 0;
    }
}
