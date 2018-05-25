package com.builtbroken.atomic;

import com.builtbroken.atomic.config.ConfigProxy;
import com.builtbroken.atomic.content.*;
import com.builtbroken.atomic.content.commands.CommandAS;
import com.builtbroken.atomic.content.machines.processing.extractor.recipe.ChemExtractorRecipes;
import com.builtbroken.atomic.lib.MassHandler;
import com.builtbroken.atomic.lib.network.netty.PacketSystem;
import com.builtbroken.atomic.lib.placement.PlacementQueue;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.exposure.ThreadRadExposure;
import com.builtbroken.atomic.map.thermal.ThreadThermalAction;
import com.builtbroken.atomic.proxy.ContentProxy;
import com.builtbroken.atomic.proxy.ProxyLoader;
import com.builtbroken.atomic.proxy.rf.ProxyRedstoneFlux;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

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

    public final String ENERGY_HANDLER_INTERFACE = "cofh.api.energy.IEnergyReceiver";

    @Mod.Instance(DOMAIN)
    public static AtomicScience INSTANCE;

    public static Logger logger = LogManager.getLogger(DOMAIN);

    public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    public static CreativeTabs creativeTab;

    @SidedProxy(clientSide = "com.builtbroken.atomic.ClientProxy", serverSide = "com.builtbroken.atomic.ServerProxy")
    public static CommonProxy sideProxy;

    public static ProxyLoader proxyLoader;

    public static File configFolder;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configFolder = new File(event.getModConfigurationDirectory(), "/bbm/atomicscience");

        //Create tab
        creativeTab = new CreativeTabs(DOMAIN)
        {
            @Override
            public Item getTabIconItem()
            {
                return ASItems.itemArmorHazmatHelm;
            }
        };

        //Register handlers
        NetworkRegistry.INSTANCE.registerGuiHandler(this, sideProxy);
        FMLCommonHandler.instance().bus().register(new PlacementQueue());
        MapHandler.register();
        ThermalHandler.init();
        MassHandler.init();

        proxyLoader = new ProxyLoader("AS");
        proxyLoader.add(new ConfigProxy());

        //Content
        proxyLoader.add(new ASFluids.Proxy()); //must run before items and blocks
        proxyLoader.add(new ASItems());
        proxyLoader.add(new ASBlocks());
        proxyLoader.add(new ASWorldGen());

        //Recipes
        proxyLoader.add(ChemExtractorRecipes.INSTANCE);

        //Handlers
        proxyLoader.add(PacketSystem.INSTANCE);
        proxyLoader.add(ProxyRedstoneFlux.class, ContentProxy.doesClassExist(ENERGY_HANDLER_INTERFACE));
        proxyLoader.add(sideProxy);

        //Register content
        ASIndirectEffects.register();

        //Proxy
        proxyLoader.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
        //Proxy
        proxyLoader.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        //Proxy
        proxyLoader.postInit();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        //Proxy
        proxyLoader.loadComplete();
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event)
    {
        //Start thread
        MapHandler.THREAD_RAD_EXPOSURE = new ThreadRadExposure();
        MapHandler.THREAD_RAD_EXPOSURE.start();
        MapHandler.THREAD_THERMAL_ACTION = new ThreadThermalAction();
        MapHandler.THREAD_THERMAL_ACTION.start();
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
        MapHandler.THREAD_THERMAL_ACTION.kill();
    }
}
