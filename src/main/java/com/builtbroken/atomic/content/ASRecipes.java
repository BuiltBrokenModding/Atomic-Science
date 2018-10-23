package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.armor.RecipesHazmatArmorDyes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/23/2018.
 */
@Mod.EventBusSubscriber(modid = AtomicScience.DOMAIN)
public class ASRecipes
{
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        event.getRegistry().register(new RecipesHazmatArmorDyes().setRegistryName(AtomicScience.PREFIX + "hazmat_armor_dye"));
    }
}
