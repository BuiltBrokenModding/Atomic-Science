package com.builtbroken.atomic;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.effects.IndirectEffectType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;

/**
 * Main mod class, handles references and registry calls
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
@Mod(modid = AtomicScience.DOMAIN, name = "Atomic Science", version = AtomicScience.VERSION, dependencies = AtomicScience.DEPENDENCIES)
public class AtomicScience
{
    public static final String DOMAIN = "atomicscience";
    public static final String PREFIX = DOMAIN + ":";

    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String MC_VERSION = "@MC@";
    public static final String VERSION = MC_VERSION + "-" + MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;
    public static final String DEPENDENCIES = "";

    public static final String TEXTURE_DIRECTORY = "textures/";
    public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";
    public static final String MODEL_DIRECTORY = TEXTURE_DIRECTORY + "models/";

    @Mod.Instance(DOMAIN)
    public static AtomicScience INSTANCE;

    public static CreativeTabs creativeTab;

    @SidedProxy(clientSide = "com.builtbroken.atomic.ClientProxy", serverSide = "com.builtbroken.atomic.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        creativeTab = new CreativeTabs(DOMAIN)
        {
            @Override
            public Item getTabIconItem()
            {
                return ASItems.itemArmorHazmatHelm;
            }
        };
        ASIndirectEffects.register();
        ASFluids.register(); //must run before items and blocks
        ASItems.register();
        ASBlocks.register();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }
}
