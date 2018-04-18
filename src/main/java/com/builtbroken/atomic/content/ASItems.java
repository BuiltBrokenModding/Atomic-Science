package com.builtbroken.atomic.content;

import com.builtbroken.atomic.content.items.ItemHazmat;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
public class ASItems
{
    public static ItemHazmat itemArmorHazmatHelm;
    public static ItemHazmat itemArmorHazmatChest;
    public static ItemHazmat itemArmorHazmatLegs;
    public static ItemHazmat itemArmorHazmatBoots;

    public static void register()
    {
        ItemHazmat.hazmatArmorMaterial = EnumHelper.addArmorMaterial("HAZMAT", 0, new int[]{0, 0, 0, 0}, 0);
        GameRegistry.registerItem(itemArmorHazmatHelm = new ItemHazmat(0, "mask"), "hazmat_helm");
        GameRegistry.registerItem(itemArmorHazmatChest = new ItemHazmat(1, "body"), "hazmat_chest");
        GameRegistry.registerItem(itemArmorHazmatLegs = new ItemHazmat(2, "leggings"), "hazmat_legs");
        GameRegistry.registerItem(itemArmorHazmatBoots = new ItemHazmat(3, "boots"), "hazmat_boots");
    }
}
