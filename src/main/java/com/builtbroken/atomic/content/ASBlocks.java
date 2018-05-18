package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.blocks.BlockRadioactiveDirt;
import com.builtbroken.atomic.content.blocks.BlockRadioactiveGrass;
import com.builtbroken.atomic.content.machines.reactor.fission.BlockReactorCell;
import com.builtbroken.atomic.content.machines.reactor.fission.TileEntityReactorCell;
import com.builtbroken.atomic.content.machines.steam.funnel.BlockSteamFunnel;
import com.builtbroken.atomic.content.machines.steam.funnel.TileEntitySteamFunnel;
import com.builtbroken.atomic.content.machines.steam.generator.BlockSteamGenerator;
import com.builtbroken.atomic.content.machines.steam.generator.TileEntitySteamGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
public class ASBlocks
{
    public static Block blockRadioactiveDirt;
    public static Block blockRadioactiveGrass;

    public static Block blockReactorCell;
    public static Block blockSteamFunnel;
    public static Block blockSteamTurbine;

    public static void register()
    {
        GameRegistry.registerBlock(blockRadioactiveDirt = new BlockRadioactiveDirt(), "radioactive_dirt");
        GameRegistry.registerBlock(blockRadioactiveGrass = new BlockRadioactiveGrass(), "radioactive_grass");

        GameRegistry.registerBlock(blockReactorCell = new BlockReactorCell(), "reactor_cell");
        GameRegistry.registerBlock(blockSteamFunnel = new BlockSteamFunnel(), "steam_funnel");
        GameRegistry.registerBlock(blockSteamTurbine = new BlockSteamGenerator(), "steam_turbine");

        GameRegistry.registerTileEntity(TileEntityReactorCell.class, AtomicScience.PREFIX + "reactor_cell");
        GameRegistry.registerTileEntity(TileEntitySteamFunnel.class, AtomicScience.PREFIX + "steam_funnel");
        GameRegistry.registerTileEntity(TileEntitySteamGenerator.class, AtomicScience.PREFIX + "steam_turbine");
    }
}
