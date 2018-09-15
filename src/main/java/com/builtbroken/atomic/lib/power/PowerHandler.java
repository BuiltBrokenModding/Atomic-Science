package com.builtbroken.atomic.lib.power;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Simple wrapper for power systems
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public abstract class PowerHandler
{
    /**
     * Can the system handle the tile
     *
     * @param side
     * @param tile
     * @return
     */
    public boolean canHandle(EnumFacing side, TileEntity tile)
    {
        return false;
    }

    /**
     * Can the system handle the item
     *
     * @param stack
     * @return
     */
    public boolean canHandle(ItemStack stack)
    {
        return false;
    }

    /**
     * Called to add power to the tile
     *
     * @param side
     * @param tile
     * @param power
     * @param doAction
     * @return power added
     */
    public int addPower(EnumFacing side, TileEntity tile, int power, boolean doAction)
    {
        return 0;
    }

    /**
     * Called to remove power from the system
     *
     * @param side
     * @param tile
     * @param power
     * @param doAction
     * @return power removed
     */
    public int removePower(EnumFacing side, TileEntity tile, int power, boolean doAction)
    {
        return 0;
    }


    /**
     * Called to add power to the stack
     *
     * @param stack
     * @param power
     * @param doAction
     * @return power added
     */
    public int chargeItem(ItemStack stack, int power, boolean doAction)
    {
        return 0;
    }

    /**
     * Called to remove power from the stack
     *
     * @param stack
     * @param power
     * @param doAction
     * @return power removed
     */
    public int dischargeItem(ItemStack stack, int power, boolean doAction)
    {
        return 0;
    }

    /**
     * Get power stored in the item
     *
     * @param stack
     * @return
     */
    public int getItemPower(ItemStack stack)
    {
        return 0;
    }

    /**
     * Get max power stored in the item
     *
     * @param stack
     * @return
     */
    public int getItemMaxPower(ItemStack stack)
    {
        return 0;
    }

    public void onTileInvalidate(TileEntity tile)
    {

    }

    public void onTileValidate(TileEntity tile)
    {

    }
}
