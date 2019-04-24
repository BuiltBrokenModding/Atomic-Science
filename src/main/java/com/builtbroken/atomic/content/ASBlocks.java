package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.blocks.BlockUraniumOre;
import com.builtbroken.atomic.content.fluid.BlockSimpleFluid;
import com.builtbroken.atomic.content.machines.accelerator.exit.BlockAcceleratorExit;
import com.builtbroken.atomic.content.machines.accelerator.exit.TileEntityAcceleratorExit;
import com.builtbroken.atomic.content.machines.accelerator.gun.BlockAcceleratorGun;
import com.builtbroken.atomic.content.machines.accelerator.gun.TileEntityAcceleratorGun;
import com.builtbroken.atomic.content.machines.accelerator.magnet.BlockMagnet;
import com.builtbroken.atomic.content.machines.accelerator.magnet.TileEntityMagnet;
import com.builtbroken.atomic.content.machines.accelerator.tube.BlockAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.tube.normal.TileEntityAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.tube.powered.TileEntityAcceleratorTubePowered;
import com.builtbroken.atomic.content.machines.container.item.BlockItemContainer;
import com.builtbroken.atomic.content.machines.container.item.TileEntityItemContainer;
import com.builtbroken.atomic.content.machines.laser.booster.BlockLaserBooster;
import com.builtbroken.atomic.content.machines.laser.booster.TileEntityLaserBooster;
import com.builtbroken.atomic.content.machines.laser.emitter.BlockLaserEmitter;
import com.builtbroken.atomic.content.machines.laser.emitter.TileEntityLaserEmitter;
import com.builtbroken.atomic.content.machines.processing.boiler.BlockChemBoiler;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.content.machines.processing.centrifuge.BlockChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.extractor.BlockChemExtractor;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.content.machines.reactor.fission.controller.BlockReactorController;
import com.builtbroken.atomic.content.machines.reactor.fission.controller.TileEntityReactorController;
import com.builtbroken.atomic.content.machines.reactor.fission.core.BlockReactorCell;
import com.builtbroken.atomic.content.machines.reactor.fission.core.TileEntityReactorCell;
import com.builtbroken.atomic.content.machines.reactor.pipe.BlockRodPipe;
import com.builtbroken.atomic.content.machines.reactor.pipe.TileEntityRodPipe;
import com.builtbroken.atomic.content.machines.reactor.pipe.inv.BlockRodPipeInv;
import com.builtbroken.atomic.content.machines.reactor.pipe.inv.TileEntityRodPipeInv;
import com.builtbroken.atomic.content.machines.sensors.thermal.BlockThermalRedstone;
import com.builtbroken.atomic.content.machines.sensors.thermal.TileEntityThermalRedstone;
import com.builtbroken.atomic.content.machines.steam.funnel.BlockSteamFunnel;
import com.builtbroken.atomic.content.machines.steam.funnel.TileEntitySteamFunnel;
import com.builtbroken.atomic.content.machines.steam.generator.BlockSteamGenerator;
import com.builtbroken.atomic.content.machines.steam.generator.TileEntitySteamGenerator;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
@Mod.EventBusSubscriber(modid = AtomicScience.DOMAIN)
public final class ASBlocks
{
    public static Block blockReactorCell;
    public static Block blockReactorController;
    public static Block blockSteamFunnel;
    public static Block blockSteamTurbine;

    public static Block blockUraniumOre;

    public static Block blockChemExtractor;
    public static Block blockChemBoiler;
    public static Block blockChemCentrifuge;

    public static Block blockRodPipe;
    public static Block blockRodPipeInv;

    public static Block blockThermalSensorRedstone;

    public static Block blockMagnet;
    public static Block blockAcceleratorTube;
    public static Block blockAcceleratorGun;
    public static Block blockAcceleratorExit;

    public static Block blockLaserEmitter;
    public static Block blockLaserBooster;

    public static Block blockItemContainer;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(blockUraniumOre = new BlockUraniumOre());

        event.getRegistry().register(blockReactorCell = new BlockReactorCell());
        GameRegistry.registerTileEntity(TileEntityReactorCell.class, new ResourceLocation(AtomicScience.PREFIX + "reactor_cell"));

        event.getRegistry().register(blockReactorController = new BlockReactorController());
        GameRegistry.registerTileEntity(TileEntityReactorController.class, new ResourceLocation(AtomicScience.PREFIX + "reactor_controller"));

        event.getRegistry().register(blockSteamFunnel = new BlockSteamFunnel());
        GameRegistry.registerTileEntity(TileEntitySteamFunnel.class, new ResourceLocation(AtomicScience.PREFIX + "steam_funnel"));

        event.getRegistry().register(blockSteamTurbine = new BlockSteamGenerator());
        GameRegistry.registerTileEntity(TileEntitySteamGenerator.class, new ResourceLocation(AtomicScience.PREFIX + "steam_turbine"));

        event.getRegistry().register(blockChemExtractor = new BlockChemExtractor());
        GameRegistry.registerTileEntity(TileEntityChemExtractor.class, new ResourceLocation(AtomicScience.PREFIX + "chem_extractor"));

        event.getRegistry().register(blockChemBoiler = new BlockChemBoiler());
        GameRegistry.registerTileEntity(TileEntityChemBoiler.class, new ResourceLocation(AtomicScience.PREFIX + "chem_boiler"));

        event.getRegistry().register(blockChemCentrifuge = new BlockChemCentrifuge());
        GameRegistry.registerTileEntity(TileEntityChemCentrifuge.class, new ResourceLocation(AtomicScience.PREFIX + "chem_centrifuge"));

        event.getRegistry().register(blockRodPipe = new BlockRodPipe());
        GameRegistry.registerTileEntity(TileEntityRodPipe.class, new ResourceLocation(AtomicScience.PREFIX + "rod_pipe"));

        event.getRegistry().register(blockRodPipeInv = new BlockRodPipeInv());
        GameRegistry.registerTileEntity(TileEntityRodPipeInv.class, new ResourceLocation(AtomicScience.PREFIX + "rod_pipe_inv"));

        event.getRegistry().register(blockThermalSensorRedstone = new BlockThermalRedstone());
        GameRegistry.registerTileEntity(TileEntityThermalRedstone.class, new ResourceLocation(AtomicScience.PREFIX + "sensor_thermal_redstone"));

        event.getRegistry().register(blockMagnet = new BlockMagnet());
        GameRegistry.registerTileEntity(TileEntityMagnet.class, new ResourceLocation(AtomicScience.PREFIX + "magnet"));

        event.getRegistry().register(blockAcceleratorTube = new BlockAcceleratorTube());
        GameRegistry.registerTileEntity(TileEntityAcceleratorTube.class, new ResourceLocation(AtomicScience.PREFIX + "accelerator_tube"));
        GameRegistry.registerTileEntity(TileEntityAcceleratorTubePowered.class, new ResourceLocation(AtomicScience.PREFIX + "accelerator_tube_powered"));

        event.getRegistry().register(blockAcceleratorGun = new BlockAcceleratorGun());
        GameRegistry.registerTileEntity(TileEntityAcceleratorGun.class, new ResourceLocation(AtomicScience.PREFIX + "accelerator_gun"));

        event.getRegistry().register(blockAcceleratorExit = new BlockAcceleratorExit());
        GameRegistry.registerTileEntity(TileEntityAcceleratorExit.class, new ResourceLocation(AtomicScience.PREFIX + "accelerator_exit"));

        event.getRegistry().register(blockLaserEmitter = new BlockLaserEmitter());
        GameRegistry.registerTileEntity(TileEntityLaserEmitter.class, new ResourceLocation(AtomicScience.PREFIX + "laser_emitter"));

        event.getRegistry().register(blockLaserBooster = new BlockLaserBooster());
        GameRegistry.registerTileEntity(TileEntityLaserBooster.class, new ResourceLocation(AtomicScience.PREFIX + "laser_booster"));

        event.getRegistry().register(blockItemContainer = new BlockItemContainer());
        GameRegistry.registerTileEntity(TileEntityItemContainer.class, new ResourceLocation(AtomicScience.PREFIX + "item_container"));

        for (ASFluids value : ASFluids.values())
        {
            if (value.makeBlock && value.fluid.getBlock() == null)
            {
                event.getRegistry().register(createFluidBlock(value));
            }
        }
    }

    static Block createFluidBlock(ASFluids fluid)
    {
        //TODO allow switching block type
        BlockSimpleFluid blockSimpleFluid = new BlockSimpleFluid(fluid.fluid, fluid.name().toLowerCase());
        blockSimpleFluid.setRegistryName(AtomicScience.PREFIX + fluid.name().toLowerCase());
        return blockSimpleFluid;
    }
}
