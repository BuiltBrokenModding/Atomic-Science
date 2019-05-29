package com.builtbroken.atomic.api.accelerator;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Applied to any part of the accelerator system including
 * the structure components (tubes) and moving components (particles)
 * <p>
 * Not everything is considered part of the accelerator for one reason
 * or another. Such as the lasers due to not actually being required
 * for the accelerator to work. Instead they are an external system
 * that interacts with the accelerator system.
 * <p>
 * Components would be anything that is needed for the accelerator to work
 * when simulated outside the game world. Such as tubes to provide paths,
 * particles as the reason for the accelerator, exits to note end of the path,
 * and entry points to note start of the path. *
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-29.
 */
public interface IAcceleratorComponent extends IPos3D
{

    /**
     * Dimension this component exists inside
     *
     * @return integer ID of the dimension
     */
    int dim();

    /**
     * Called to save the component to memory
     *
     * @param nbt - data to save into
     * @return save data or super.save(nbt)
     */
    NBTTagCompound save(NBTTagCompound nbt);

    /**
     * Called to load the component from memory
     *
     * @param nbt - data to read from
     */
    void load(NBTTagCompound nbt);

    /**
     * Checks if the component is no longer functional
     * and can be considered dead.
     * <p>
     * Used to remove the component from the network
     * or any connections.
     *
     * @return true if dead, false if otherwise
     */
    boolean isDead();
}
