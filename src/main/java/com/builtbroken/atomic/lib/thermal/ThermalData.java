package com.builtbroken.atomic.lib.thermal;

import net.minecraft.block.Block;

/**
 * Stores data about a block's thermal properties
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class ThermalData
{
    /** Rate of heat movement, heat J/kgK (joules / kilo-grams kelvin) */
    public float specificHeat;

    /** heat of fusion or vaporization, heat J/kgK (joules / kilo-grams kelvin) */
    public float changeStateHeat;

    /** Temperature to change state, kelvin */
    public float changeStateTemperature;

    public Block changeBlock;
    public int changeMeta;

    public ThermalData(float specificHeat, float changeStateHeat, float changeStateTemperature, Block changeBlock, int meta)
    {
        this.specificHeat = specificHeat;
        this.changeStateHeat = changeStateHeat;
        this.changeStateTemperature = changeStateTemperature;
        this.changeBlock = changeBlock;
        this.changeMeta = meta;
    }
}
