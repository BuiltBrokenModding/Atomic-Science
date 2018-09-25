package com.builtbroken.atomic.content.armor;

/**
 * Radiation armor level
 * <p>
 * Only 1 level is used at the time. This will be the level less than current radiation value but greater than any other values lower than the radiation value.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/24/2018.
 */
public class ArmorRadLevelData implements Comparable<ArmorRadLevelData>
{
    /** At what point does this protection value get used */
    public final float levelStart;

    /** Percentage of protection offered (radiation - protection_flat) * (1 - percentage) */
    public float protection_percent;

    /** Float protection offered (radiation - protection)
     * Reactor can easily put out 20 rads per tick */
    public float protection_flat;

    /**
     * Translation key, will be prefixed with "rad.armor.level." and sufixed with ".name"
     * Recommended to use mod's domain name in key Ex: "domain:level_name" or "icbm:military.protection"
     */
    public String translation_key;

    public ArmorRadLevelData(float levelStart)
    {
        this.levelStart = levelStart;
    }

    public ArmorRadLevelData setProtectionPercent(float value)
    {
        this.protection_percent = value;
        return this;
    }

    /**
     * Sets flat protection level
     *
     * @param value
     * @return
     */
    public ArmorRadLevelData setProtectionFlat(float value)
    {
        this.protection_flat = value;
        return this;
    }

    public ArmorRadLevelData setTranslationKey(String key)
    {
        this.translation_key = key;
        return this;
    }

    @Override
    public int compareTo(ArmorRadLevelData o)
    {
        return Float.compare(levelStart, o.levelStart);
    }

    @Override
    public String toString()
    {
        return "ArmorRadLevel[" + levelStart + "rad, " + protection_percent + "%, -" + protection_flat + "]";
    }
}
