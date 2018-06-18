package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.blocks.BlockUraniumOre;
import com.builtbroken.atomic.content.machines.processing.boiler.BlockChemBoiler;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.content.machines.processing.centrifuge.BlockChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.extractor.BlockChemExtractor;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.content.machines.reactor.fission.core.BlockReactorCell;
import com.builtbroken.atomic.content.machines.reactor.fission.core.TileEntityReactorCell;
import com.builtbroken.atomic.content.machines.steam.funnel.BlockSteamFunnel;
import com.builtbroken.atomic.content.machines.steam.funnel.TileEntitySteamFunnel;
import com.builtbroken.atomic.content.machines.steam.generator.BlockSteamGenerator;
import com.builtbroken.atomic.content.machines.steam.generator.ItemBlockSteamGenerator;
import com.builtbroken.atomic.content.machines.steam.generator.TileEntitySteamGenerator;
import com.builtbroken.atomic.proxy.ContentProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
public class ASBlocks extends ContentProxy
{
    public static Block blockRadioactiveDirt;
    public static Block blockRadioactiveGrass;

    public static Block blockReactorCell;
    public static Block blockSteamFunnel;
    public static Block blockSteamTurbine;

    public static Block blockUraniumOre;

    public static Block blockChemExtractor;
    public static Block blockChemBoiler;
    public static Block blockChemCentrifuge;

    public ASBlocks()
    {
        super("blocks");
    }

    @Override
    public void preInit()
    {
        //GameRegistry.registerBlock(blockRadioactiveDirt = new BlockRadioactiveDirt(), "radioactive_dirt");
        //GameRegistry.registerBlock(blockRadioactiveGrass = new BlockRadioactiveGrass(), "radioactive_grass");
        GameRegistry.registerBlock(blockUraniumOre = new BlockUraniumOre(), "uranium_ore");

        GameRegistry.registerBlock(blockReactorCell = new BlockReactorCell(), "reactor_cell");
        GameRegistry.registerTileEntity(TileEntityReactorCell.class, AtomicScience.PREFIX + "reactor_cell");

        GameRegistry.registerBlock(blockSteamFunnel = new BlockSteamFunnel(), "steam_funnel");
        GameRegistry.registerTileEntity(TileEntitySteamFunnel.class, AtomicScience.PREFIX + "steam_funnel");

        GameRegistry.registerBlock(blockSteamTurbine = new BlockSteamGenerator(), ItemBlockSteamGenerator.class, "steam_turbine");
        GameRegistry.registerTileEntity(TileEntitySteamGenerator.class, AtomicScience.PREFIX + "steam_turbine");

        GameRegistry.registerBlock(blockChemExtractor = new BlockChemExtractor(), "chem_extractor");
        GameRegistry.registerTileEntity(TileEntityChemExtractor.class, AtomicScience.PREFIX + "chem_extractor");

        GameRegistry.registerBlock(blockChemBoiler = new BlockChemBoiler(), "chem_boiler");
        GameRegistry.registerTileEntity(TileEntityChemBoiler.class, AtomicScience.PREFIX + "chem_boiler");

        GameRegistry.registerBlock(blockChemCentrifuge = new BlockChemCentrifuge(), "chem_centrifuge");
        GameRegistry.registerTileEntity(TileEntityChemCentrifuge.class, AtomicScience.PREFIX + "chem_centrifuge");
    }
}
