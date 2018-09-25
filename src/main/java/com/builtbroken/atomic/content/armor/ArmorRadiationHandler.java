package com.builtbroken.atomic.content.armor;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/24/2018.
 */
@Mod.EventBusSubscriber(modid = AtomicScience.DOMAIN)
public class ArmorRadiationHandler
{
    //List of armor to protection ratings
    private static final List<ArmorRadData> armorRadiationList = new ArrayList(); //TODO if list becomes an issue switch to Map<Item, data>

    public static boolean isRadiationResistantArmor(ItemStack stack)
    {
        return getArmorRadData(stack) != null;
    }

    public static ArmorRadData getArmorRadData(ItemStack stack)
    {
        return getArmorRadData(stack, false);
    }

    public static ArmorRadData getArmorRadData(ItemStack stack, boolean create)
    {
        for (ArmorRadData data : armorRadiationList)
        {
            if (ItemStack.areItemsEqual(stack, data.item) && ItemStack.areItemStackTagsEqual(stack, data.item))
            {
                return data;
            }
        }
        if (create)
        {
            ArmorRadData data = new ArmorRadData(stack.copy());
            armorRadiationList.add(data);
            return data;
        }
        return null;
    }

}
