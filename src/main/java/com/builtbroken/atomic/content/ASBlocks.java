package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.blocks.BlockUraniumOre;
import com.builtbroken.atomic.content.fluid.BlockSimpleFluid;
import com.builtbroken.atomic.content.machines.accelerator.detector.BlockParticleDetector;
import com.builtbroken.atomic.content.machines.accelerator.detector.TileEntityParticleDetector;
import com.builtbroken.atomic.content.machines.accelerator.exit.BlockAcceleratorExit;
import com.builtbroken.atomic.content.machines.accelerator.exit.TileEntityAcceleratorExit;
import com.builtbroken.atomic.content.machines.accelerator.gun.BlockAcceleratorGun;
import com.builtbroken.atomic.content.machines.accelerator.gun.TileEntityAcceleratorGun;
import com.builtbroken.atomic.content.machines.accelerator.magnet.BlockMagnet;
import com.builtbroken.atomic.content.machines.accelerator.magnet.TileEntityMagnet;
import com.builtbroken.atomic.content.machines.accelerator.tube.normal.BlockAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.tube.normal.TileEntityAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.tube.powered.BlockAcceleratorTubePowered;
import com.builtbroken.atomic.content.machines.accelerator.tube.powered.TileEntityAcceleratorTubePowered;
import com.builtbroken.atomic.content.machines.container.item.BlockItemContainer;
import com.builtbroken.atomic.content.machines.container.item.TileEntityItemContainer;
import com.builtbroken.atomic.content.machines.laser.booster.BlockLaserBooster;
import com.builtbroken.atomic.content.machines.laser.booster.TileEntityLaserBooster;
import com.builtbroken.atomic.content.machines.laser.emitter.BlockLaserEmitter;
import com.builtbroken.atomic.content.machines.laser.emitter.TileEntityLaserEmitter;
import com.builtbroken.atomic.content.machines.pipe.item.BlockCapRelay;
import com.builtbroken.atomic.content.machines.pipe.item.TileEntityCapRelay;
import com.builtbroken.atomic.content.machines.pipe.reactor.inv.BlockRodPipeInv;
import com.builtbroken.atomic.content.machines.pipe.reactor.inv.TileEntityRodPipeInv;
import com.builtbroken.atomic.content.machines.pipe.reactor.pass.BlockRodPipe;
import com.builtbroken.atomic.content.machines.pipe.reactor.pass.TileEntityRodPipe;
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
import com.builtbroken.atomic.content.machines.sensors.thermal.BlockThermalRedstone;
import com.builtbroken.atomic.content.machines.sensors.thermal.TileEntityThermalRedstone;
import com.builtbroken.atomic.content.machines.steam.funnel.BlockSteamFunnel;
import com.builtbroken.atomic.content.machines.steam.funnel.TileEntitySteamFunnel;
import com.builtbroken.atomic.content.machines.steam.generator.BlockSteamGenerator;
import com.builtbroken.atomic.content.machines.steam.generator.TileEntitySteamGenerator;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
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
    public static Block blockCapRelay;

    public static Block blockThermalSensorRedstone;

    public static Block blockMagnet;
    public static Block blockAcceleratorTube;
    public static Block blockAcceleratorTubePowered;
    public static Block blockAcceleratorGun;
    public static Block blockAcceleratorExit;
    public static Block blockParticleDetector;

    public static Block blockLaserEmitter;
    public static Block blockLaserBooster;

    public static Block blockItemContainer;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(blockUraniumOre = new BlockUraniumOre());

        blockReactorCell = registerBlock(event, new BlockReactorCell(), TileEntityReactorCell.class);

        blockReactorController = registerBlock(event, new BlockReactorController(), TileEntityReactorController.class);

        blockSteamFunnel = registerBlock(event, new BlockSteamFunnel(), TileEntitySteamFunnel.class);

        blockSteamTurbine = registerBlock(event, new BlockSteamGenerator(), TileEntitySteamGenerator.class);

        blockChemExtractor = registerBlock(event, new BlockChemExtractor(), TileEntityChemExtractor.class);

        blockChemBoiler = registerBlock(event, new BlockChemBoiler(), TileEntityChemBoiler.class);

        blockChemCentrifuge = registerBlock(event, new BlockChemCentrifuge(), TileEntityChemCentrifuge.class);

        blockRodPipe = registerBlock(event, new BlockRodPipe(), TileEntityRodPipe.class);

        blockCapRelay = registerBlock(event, new BlockCapRelay(), TileEntityCapRelay.class);

        blockRodPipeInv = registerBlock(event, new BlockRodPipeInv(), TileEntityRodPipeInv.class);

        blockThermalSensorRedstone = registerBlock(event, new BlockThermalRedstone(), TileEntityThermalRedstone.class);

        blockMagnet = registerBlock(event, new BlockMagnet(), TileEntityMagnet.class);

        blockAcceleratorTube = registerBlock(event, new BlockAcceleratorTube(), TileEntityAcceleratorTube.class);

        blockAcceleratorTubePowered = registerBlock(event, new BlockAcceleratorTubePowered(), TileEntityAcceleratorTubePowered.class);

        blockAcceleratorGun = registerBlock(event, new BlockAcceleratorGun(), TileEntityAcceleratorGun.class);

        blockAcceleratorExit = registerBlock(event, new BlockAcceleratorExit(), TileEntityAcceleratorExit.class);

        blockLaserEmitter = registerBlock(event, new BlockLaserEmitter(), TileEntityLaserEmitter.class);

        blockLaserBooster = registerBlock(event, new BlockLaserBooster(), TileEntityLaserBooster.class);

        blockItemContainer = registerBlock(event, new BlockItemContainer(), TileEntityItemContainer.class);

        blockParticleDetector = registerBlock(event, new BlockParticleDetector(), TileEntityParticleDetector.class);

        for (ASFluids value : ASFluids.values())
        {
            if (value.makeBlock && value.fluid.getBlock() == null)
            {
                event.getRegistry().register(createFluidBlock(value));
            }
        }
    }

    static <B extends Block> B registerBlock(RegistryEvent.Register<Block> reg, B block, Class<? extends TileEntity> tileEntity)
    {
        reg.getRegistry().register(block);
        GameRegistry.registerTileEntity(tileEntity, block.getRegistryName());
        return block;
    }

    static Block createFluidBlock(ASFluids fluid)
    {
        //TODO allow switching block type
        BlockSimpleFluid blockSimpleFluid = new BlockSimpleFluid(fluid.fluid, fluid.name().toLowerCase());
        blockSimpleFluid.setRegistryName(AtomicScience.PREFIX + fluid.name().toLowerCase());
        return blockSimpleFluid;
    }
}
