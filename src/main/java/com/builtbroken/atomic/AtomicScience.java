package com.builtbroken.atomic;

import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASIndirectEffects;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.commands.CommandAS;
import com.builtbroken.atomic.lib.network.netty.PacketSystem;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.thread.ThreadRadExposure;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public static final String GUI_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "gui/";
    public static final String MODEL_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "models/";

    public static final String MODEL_DIRECTORY = "models/";

    @Mod.Instance(DOMAIN)
    public static AtomicScience INSTANCE;

    public static Logger logger = LogManager.getLogger(DOMAIN);

    public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    public static CreativeTabs creativeTab;

    @SidedProxy(clientSide = "com.builtbroken.atomic.ClientProxy", serverSide = "com.builtbroken.atomic.CommonProxy")
    public static CommonProxy proxy;

    public static Configuration config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        config = new Configuration(event.getSuggestedConfigurationFile(), "/bbm/Atomic_Science.cfg");

        //Register handlers
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        MapHandler.register();

        //Create tab
        creativeTab = new CreativeTabs(DOMAIN)
        {
            @Override
            public Item getTabIconItem()
            {
                return ASItems.itemArmorHazmatHelm;
            }
        };

        //Register content
        ASIndirectEffects.register();
        ASFluids.register(); //must run before items and blocks
        ASItems.register();
        ASBlocks.register();

        //Proxy
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
        PacketSystem.register();
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event)
    {
        //Start thread
        MapHandler.THREAD_RAD_EXPOSURE = new ThreadRadExposure();
        MapHandler.THREAD_RAD_EXPOSURE.start();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        // Setup command
        ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
        ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
        serverCommandManager.registerCommand(new CommandAS());
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
        //Kill old thread
        MapHandler.THREAD_RAD_EXPOSURE.kill();
    }
}
