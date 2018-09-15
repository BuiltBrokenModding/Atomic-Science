package com.builtbroken.atomic.lib.power;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles power interaction between tiles
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class PowerSystem
{
    private static final List<PowerHandler> powerHandlers = new ArrayList();

    static
    {
        register(new PowerHandlerFE());
    }


    /**
     * Called to register a new power handler
     *
     * @param powerHandler
     */
    public static void register(PowerHandler powerHandler)
    {
        if (powerHandler != null)
        {
            powerHandlers.add(powerHandler);
        }
    }

    /**
     * Checks if the system can support the tile for the given side
     *
     * @param sideToAccess - side to access
     * @param tileEntity   - tile to access
     * @return true if the tile is supported for the side
     */
    public static boolean canSupport(EnumFacing sideToAccess, TileEntity tileEntity)
    {
        return getHandler(sideToAccess, tileEntity) != null;
    }

    /**
     * Gets the power handler for the side and tile
     *
     * @param sideToAccess - side to access
     * @param tileEntity   - tile to access
     * @return power handler or null
     */
    public static PowerHandler getHandler(EnumFacing sideToAccess, TileEntity tileEntity)
    {
        for (PowerHandler handler : powerHandlers)
        {
            if (handler.canHandle(sideToAccess, tileEntity))
            {
                return handler;
            }
        }
        return null;
    }

    /**
     * Gets the power handler for the item
     *
     * @param stack - item
     * @return power handler or null
     */
    public static PowerHandler getHandler(ItemStack stack)
    {
        if (stack != null && stack.getItem() != null)
        {
            for (PowerHandler handler : powerHandlers)
            {
                if (handler.canHandle(stack))
                {
                    return handler;
                }
            }
        }
        return null;
    }

    /**
     * Called to output power
     *
     * @param sideAccessed - side accessed on target tile
     * @param tileEntity   - target tile
     * @param power        - energy to add
     * @param doAction     - true to do action, false to simulate
     * @return power added to tile
     */
    public static int addPower(EnumFacing sideAccessed, TileEntity tileEntity, int power, boolean doAction)
    {
        if (power > 0)
        {
            PowerHandler handler = getHandler(sideAccessed, tileEntity);
            if (handler != null)
            {
                return handler.addPower(sideAccessed, tileEntity, power, doAction);
            }
        }
        return 0;
    }

    /**
     * Called to output power
     *
     * @param tile          - source of power
     * @param direction     - direction from source to output (added to location and reversed for access side)
     * @param powerToOutput - power to give
     * @param doAction      - true to do action, false to simulate
     * @return power added to tile
     */
    public static int outputPower(TileEntity tile, EnumFacing direction, int powerToOutput, boolean doAction)
    {
        return outputPower(tile.getWorld(), tile.getPos(), direction, powerToOutput, doAction);
    }

    /**
     * Called to output power
     *
     * @param world         - world to output power inside
     * @param pos           - position of the source of power
     * @param direction     - direction from source to output (added to location and reversed for access side)
     * @param powerToOutput - power to give
     * @param doAction      - true to do action, false to simulate
     * @return power added to tile
     */
    public static int outputPower(World world, BlockPos pos, EnumFacing direction, int powerToOutput, boolean doAction)
    {
        if (powerToOutput > 0)
        {
            pos = pos.add(direction.getDirectionVec());

            TileEntity tile = world.getTileEntity(pos);
            if (tile != null)
            {
                return addPower(direction.getOpposite(), tile, powerToOutput, doAction);
            }
        }
        return 0;
    }

    /**
     * Removes power from an item
     *
     * @param itemStack - power item, can be null
     * @return power removed (FE)
     */
    public static int dischargeItem(ItemStack itemStack, int amount, boolean doAction)
    {
        if (amount > 0)
        {
            PowerHandler handler = getHandler(itemStack);
            if (handler != null)
            {
                return handler.dischargeItem(itemStack, amount, doAction);
            }
        }
        return 0;
    }

    /**
     * Adds power to an item
     *
     * @param itemStack - power item, can be null
     * @return power added (FE)
     */
    public static int chargeItem(ItemStack itemStack, int amount, boolean doAction)
    {
        if (amount > 0)
        {
            PowerHandler handler = getHandler(itemStack);
            if (handler != null)
            {
                return handler.chargeItem(itemStack, amount, doAction);
            }
        }
        return 0;
    }

    /**
     * Checks to see how much energy is stored
     *
     * @param itemStack - power item, can be null
     * @return power in item (FE)
     */
    public static int getEnergyStored(ItemStack itemStack)
    {
        PowerHandler handler = getHandler(itemStack);
        if (handler != null)
        {
            return handler.getItemPower(itemStack);
        }
        return 0;
    }

    /**
     * Checks to see how much energy is stored
     *
     * @param itemStack - power item, can be null
     * @return power in item (FE)
     */
    public static int getMaxEnergyStored(ItemStack itemStack)
    {
        PowerHandler handler = getHandler(itemStack);
        if (handler != null)
        {
            return handler.getItemMaxPower(itemStack);
        }
        return 0;
    }

    public static void forEach(Consumer<PowerHandler> action)
    {
        powerHandlers.forEach(action);
    }
}
