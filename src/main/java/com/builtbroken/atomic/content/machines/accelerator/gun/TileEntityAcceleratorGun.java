package com.builtbroken.atomic.content.machines.accelerator.gun;

import com.builtbroken.atomic.content.machines.container.TileEntityItemContainer;
import com.builtbroken.atomic.content.machines.laser.emitter.TileEntityLaserEmitter;
import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2018.
 */
public class TileEntityAcceleratorGun extends TileEntityMachine
{
    /**
     * Called when laser fires through a container into the gun
     *
     * @param container    - container holding an item
     * @param laserEmitter - laser that fired, used to get starting energy
     */
    public void onLaserFiredInto(TileEntityItemContainer container, TileEntityLaserEmitter laserEmitter)
    {
        final ItemStack heldItem = container.getHeldItem();
        if (!heldItem.isEmpty())
        {
            createParticle(heldItem, laserEmitter.boosterCount / container.consumeItems()); //TODO figure out how we are going to do energy
        }
    }

    public void createParticle(ItemStack item, int energyToStart)
    {

    }

    /**
     * Called from remote system to trigger laser
     */
    public void fireLaser()
    {
        final EnumFacing facing = getDirection();

    }
}
