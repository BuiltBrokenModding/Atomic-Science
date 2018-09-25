package com.builtbroken.atomic.content.armor;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores radiation protection data about armor or items
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/24/2018.
 */
public class ArmorRadData
{
    /** Item to match against */
    public final ItemStack item;

    /** Radiation levels of protection */
    public final List<ArmorRadLevelData> radiationLevels = new ArrayList();

    private boolean needsSorted = false;

    public ArmorRadData(ItemStack item)
    {
        this.item = item;
    }

    public boolean addRadiationLevel(ArmorRadLevelData data)
    {
        if (data != null)
        {
            for (ArmorRadLevelData armorRadLevelData : radiationLevels)
            {
                if (armorRadLevelData.levelStart == data.levelStart)
                {
                    AtomicScience.logger.error("ArmorRadData[" + item + " | " + item.getDisplayName() + "] Attempt at adding radiation level matching an existing level. " +
                            "\nCurrent: " + armorRadLevelData +
                            "\nNew: " + data, new RuntimeException("trace"));
                    return false;
                }
            }
            radiationLevels.add(data);
            needsSorted = true;
            return true;
        }
        return false;
    }

    public float applyProtection(float rad)
    {
        //Return if no values
        if (radiationLevels.size() == 0)
        {
            return rad;
        }

        //Sort if needed
        if (needsSorted)
        {
            needsSorted = false;
            Collections.sort(radiationLevels);
        }

        //Find best match
        ArmorRadLevelData best = getBestProtection(rad);
        if (best != null)
        {
            //Apply protection
            float new_rad = rad - best.protection_flat;
            new_rad = new_rad - new_rad * best.protection_percent;
            return Math.max(0, Math.min(rad, new_rad));
        }

        return rad;
    }

    public ArmorRadLevelData getBestProtection(float rad)
    {
        ArmorRadLevelData best = radiationLevels.get(0);
        for (int i = 1; i < radiationLevels.size(); i++)
        {
            ArmorRadLevelData data = radiationLevels.get(i);
            if (data.levelStart <= rad && data.levelStart > best.levelStart)
            {
                best = data;
            }
            else if (data.levelStart > rad)
            {
                break;
            }
        }
        return best;
    }
}
