package com.builtbroken.atomic.content.machines.accelerator.recipe;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-29.
 */
public interface IAcceleratorRecipe<O, I, P>
{
    O getOutput(I input, P particle, float energy);
}
