package com.builtbroken.atomic.lib.power;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles power interaction between tiles
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class PowerSystem
{
    private static final List<PowerHandler> powerHandlers = new ArrayList();

    public static int addPower(ForgeDirection sideAccessed, TileEntity tileEntity, int power, boolean doAction)
    {
        PowerHandler handler = getHandler(sideAccessed, tileEntity);
        if (handler != null)
        {
            return handler.addPower(sideAccessed, tileEntity, power, doAction);
        }
        return 0;
    }

    /**
     * Checks if the system can support the tile for the given side
     *
     * @param sideToAccess - side to access
     * @param tileEntity   - tile to access
     * @return true if the tile is supported for the side
     */
    public static boolean canSupport(ForgeDirection sideToAccess, TileEntity tileEntity)
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
    public static PowerHandler getHandler(ForgeDirection sideToAccess, TileEntity tileEntity)
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
     * @param world         - source of power
     * @param x             - source of power
     * @param y             - source of power
     * @param z             - source of power
     * @param direction     - direction from source to output (added to location and reversed for access side)
     * @param powerToOutput - power to give
     * @param doAction      - true to do action, false to simulate
     * @return power added to tile
     */
    public static int outputPower(World world, int x, int y, int z, ForgeDirection direction, int powerToOutput, boolean doAction)
    {
        if (powerToOutput > 0)
        {
            x += direction.offsetX;
            y += direction.offsetY;
            z += direction.offsetZ;

            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile != null)
            {
                return addPower(direction.getOpposite(), tile, powerToOutput, doAction);
            }
        }
        return 0;
    }

    public static void register(PowerHandler powerHandler)
    {
        if (powerHandler != null)
        {
            powerHandlers.add(powerHandler);
        }
    }

    /**
     * Checks to see how much energy is stored
     *
     * @param itemStack - power item, can be null
     * @return power in item (UE)
     */
    public static int getEnergyStored(ItemStack itemStack)
    {
        PowerHandler handler = getHandler(itemStack);
        if (handler != null)
        {
            return handler.getPowerStored(itemStack);
        }
        return 0;
    }

    /**
     * Removes power from an item
     *
     * @param itemStack - power item, can be null
     * @return power removed (UE)
     */
    public static int removePower(ItemStack itemStack, int amount, boolean doAction)
    {
        PowerHandler handler = getHandler(itemStack);
        if (handler != null)
        {
            return handler.removePower(itemStack, amount, false);
        }
        return 0;
    }
}
