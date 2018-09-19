package com.builtbroken.atomic.content.items.wrench;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASItems;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = AtomicScience.DOMAIN, value = Side.CLIENT)
public class ModelWrench implements ItemMeshDefinition
{
    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        return ASItems.itemWrench.getMode(stack).modelResourceLocation;
    }

    @SubscribeEvent
    public static void registerAllModels(ModelRegistryEvent event)
    {
        //Build model locations
        for (WrenchMode mode : WrenchMode.values())
        {
            mode.modelResourceLocation = new ModelResourceLocation(new ResourceLocation(AtomicScience.DOMAIN, "wrench/" + mode.name().toLowerCase()), "inventory");
        }

        //Set main model backup
        //ModelLoader.setCustomModelResourceLocation(ASItems.itemWrench, 0,
        //        new ModelResourceLocation(new ResourceLocation(AtomicScience.DOMAIN, "wrench/" + ASItems.itemWrench.getRegistryName().getPath()), "inventory"));

        //Set mesh definition, maps mode to model
        ModelLoader.setCustomMeshDefinition(ASItems.itemWrench, new ModelWrench());

        //Tell model registry to load models for our variants
        for (WrenchMode mode : WrenchMode.values())
        {
            ModelBakery.registerItemVariants(ASItems.itemWrench, mode.modelResourceLocation);
        }
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event)
    {
        event.getItemColors().registerItemColorHandler((stack, tintIndex) -> {

            //Only color layer 1
            if (tintIndex == 1)
            {
                return ASItems.itemWrench.getColor(stack).getColorInt();
            }

            //Default for layer 0 and all other layers
            return 16777215;
        }, ASItems.itemWrench);
    }
}
