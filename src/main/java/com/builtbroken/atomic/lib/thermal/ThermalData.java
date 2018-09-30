package com.builtbroken.atomic.lib.thermal;

import net.minecraft.block.state.IBlockState;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Stores data about a block's thermal properties
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class ThermalData
{
    /** Rate at which heat can move through a block */
    public float heatMovementRate;

    /** Rate of heat movement, heat J/g K (joules / grams kelvin) */
    public float specificHeat;

    /** heat of fusion or vaporization, heat J/g K (joules / grams kelvin) */
    public float changeStateHeat;

    /** Temperature to change state, kelvin */
    public float changeStateTemperature;

    public Supplier<IBlockState> blockFactory;

    public Consumer<ThermalPlacement> postPlacementCallback;

    public ThermalData(float heatMovementRate, float specificHeat, float changeStateHeat, float changeStateTemperature, Supplier<IBlockState> blockFactory, Consumer<ThermalPlacement> postPlacementCallback)
    {
        this.heatMovementRate = heatMovementRate;
        this.specificHeat = specificHeat;
        this.changeStateHeat = changeStateHeat;
        this.changeStateTemperature = changeStateTemperature;
        this.blockFactory = blockFactory;
        this.postPlacementCallback = postPlacementCallback;
    }

    public long energyToChangeStates(float mass)
    {
        return (long) (mass * changeStateHeat * 1000L); //x1000 for kg to g
    }

    public long energyForTempChange(float mass, float tempDelta)
    {
        return (long) (mass * specificHeat * tempDelta * 1000L); //x1000 for kg to g
    }

    public double energyToGetToStateChange(float mass)
    {
        return energyForTempChange(mass, changeStateTemperature);
    }
}
