package com.builtbroken.atomic;

import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASIndirectEffects;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.ASWorldGen;
import com.builtbroken.atomic.content.commands.CommandAS;
import com.builtbroken.atomic.content.machines.processing.ProcessorRecipeHandler;
import com.builtbroken.atomic.lib.MassHandler;
import com.builtbroken.atomic.lib.placement.PlacementQueue;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.lib.transform.vector.Location;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.exposure.ThreadRadExposure;
import com.builtbroken.atomic.map.exposure.node.RadiationSource;
import com.builtbroken.atomic.map.thermal.ThreadThermalAction;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.proxy.Mods;
import com.builtbroken.atomic.proxy.ProxyLoader;
import com.builtbroken.atomic.proxy.bc.ProxyBuildcraftEnergy;
import com.builtbroken.atomic.proxy.eu.ProxyIC2;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Main mod class, handles references and registry calls
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
@Mod(modid = AtomicScience.DOMAIN, name = "Atomic Science", version = AtomicScience.VERSION, dependencies = AtomicScience.DEPENDENCIES)
@Mod.EventBusSubscriber
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

    public static final int TICKS_SECOND = 20;
    public static final int TICKS_MIN = TICKS_SECOND * 60;
    public static final int TICKS_HOUR = TICKS_MIN * 60;

    @Mod.Instance(DOMAIN)
    public static AtomicScience INSTANCE;

    public static Logger logger = LogManager.getLogger(DOMAIN);

    public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    public static CreativeTabs creativeTab;

    @SidedProxy(clientSide = "com.builtbroken.atomic.client.ClientProxy", serverSide = "com.builtbroken.atomic.ServerProxy")
    public static CommonProxy sideProxy;

    public static ProxyLoader proxyLoader;

    public static File configFolder;

    public AtomicScience()
    {
        if (runningAsDev)
        {
            FluidRegistry.enableUniversalBucket();
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configFolder = new File(event.getModConfigurationDirectory(), "/bbm/atomicscience");

        //Create tab
        creativeTab = new CreativeTabs(DOMAIN)
        {
            @Override
            public ItemStack createIcon()
            {
                return new ItemStack(ASItems.itemArmorHazmatHelm);
            }
        };

        //Register handlers
        NetworkRegistry.INSTANCE.registerGuiHandler(this, sideProxy);
        MinecraftForge.EVENT_BUS.register(new PlacementQueue());

        MapHandler.register();
        ThermalHandler.init();
        MassHandler.init();

        proxyLoader = new ProxyLoader("AS");

        //Content
        proxyLoader.add(new ASFluids.Proxy()); //must run before items and blocks
        proxyLoader.add(new ASWorldGen());

        //Recipes
        proxyLoader.add(ProcessorRecipeHandler.INSTANCE);

        //Handlers
        proxyLoader.add(PacketSystem.INSTANCE);
        proxyLoader.add(sideProxy);

        if (Mods.IC2.isLoaded())
        {
            proxyLoader.add(new ProxyIC2());
        }
        if (Mods.BUILDCRAFT_ENERGY.isLoaded())
        {
            proxyLoader.add(new ProxyBuildcraftEnergy());
        }

        //Register content
        ASIndirectEffects.register();

        //Proxy
        proxyLoader.preInit();

        //Used to compare rendering
        if (runningAsDev)
        {
            for (ASFluids value : ASFluids.values())
            {
                FluidRegistry.addBucketForFluid(value.fluid);
            }
        }
    }

    public void registerCaps()
    {
        CapabilityManager.INSTANCE.register(IRadiationSource.class, new Capability.IStorage<IRadiationSource>()
                {
                    @Nullable
                    @Override
                    public NBTBase writeNBT(Capability<IRadiationSource> capability, IRadiationSource instance, EnumFacing side)
                    {
                        return null;
                    }

                    @Override
                    public void readNBT(Capability<IRadiationSource> capability, IRadiationSource instance, EnumFacing side, NBTBase nbt)
                    {

                    }
                },
                () -> new RadiationSource<Location>(null)
                {
                    @Override
                    public double x()
                    {
                        return host != null ? host.x() : 0;
                    }

                    @Override
                    public double y()
                    {
                        return host != null ? host.y() : 0;
                    }

                    @Override
                    public double z()
                    {
                        return host != null ? host.z() : 0;
                    }

                    @Override
                    public World world()
                    {
                        return host != null ? host.world() : null;
                    }

                    @Override
                    public int getRadioactiveMaterial()
                    {
                        return 0;
                    }
                });
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

    /**
     * Inject the new values and save to the config file when the config has been changed from the GUI.
     *
     * @param event The event
     */
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(DOMAIN))
        {
            ConfigManager.sync(DOMAIN, Config.Type.INSTANCE);
        }
    }
}
