package com.builtbroken.atomic;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import resonant.lib.render.block.BlockRenderingHandler;
import com.builtbroken.atomic.fission.reactor.GuiReactorCell;
import com.builtbroken.atomic.fission.reactor.RenderReactorCell;
import com.builtbroken.atomic.fission.reactor.TileReactorCell;
import com.builtbroken.atomic.fusion.RenderPlasmaHeater;
import com.builtbroken.atomic.fusion.TilePlasmaHeater;
import com.builtbroken.atomic.particle.accelerator.EntityParticle;
import com.builtbroken.atomic.particle.accelerator.GuiAccelerator;
import com.builtbroken.atomic.particle.accelerator.RenderParticle;
import com.builtbroken.atomic.particle.accelerator.TileAccelerator;
import com.builtbroken.atomic.particle.quantum.GuiQuantumAssembler;
import com.builtbroken.atomic.particle.quantum.TileQuantumAssembler;
import com.builtbroken.atomic.process.RenderChemicalExtractor;
import com.builtbroken.atomic.process.TileChemicalExtractor;
import com.builtbroken.atomic.process.fission.GuiCentrifuge;
import com.builtbroken.atomic.process.fission.GuiChemicalExtractor;
import com.builtbroken.atomic.process.fission.GuiNuclearBoiler;
import com.builtbroken.atomic.process.fission.RenderCentrifuge;
import com.builtbroken.atomic.process.fission.RenderNuclearBoiler;
import com.builtbroken.atomic.process.fission.TileCentrifuge;
import com.builtbroken.atomic.process.fission.TileNuclearBoiler;
import com.builtbroken.atomic.process.sensor.RenderThermometer;
import com.builtbroken.atomic.process.sensor.TileThermometer;
import com.builtbroken.atomic.process.turbine.RenderElectricTurbine;
import com.builtbroken.atomic.process.turbine.TileElectricTurbine;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(SoundHandler.INSTANCE);
        RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());
    }

    @Override
    public int getArmorIndex(String armor)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(armor);
    }

    @Override
    public void init()
    {
        super.init();
        ClientRegistry.bindTileEntitySpecialRenderer(TileCentrifuge.class, new RenderCentrifuge());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlasmaHeater.class, new RenderPlasmaHeater());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());

        RenderingRegistry.registerEntityRenderingHandler(EntityParticle.class, new RenderParticle());
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        Block block = Block.blocksList[world.getBlockId(x, y, z)];

        if (tileEntity instanceof TileCentrifuge)
        {
            return new GuiCentrifuge(player.inventory, ((TileCentrifuge) tileEntity));
        }
        else if (tileEntity instanceof TileChemicalExtractor)
        {
            return new GuiChemicalExtractor(player.inventory, ((TileChemicalExtractor) tileEntity));
        }
        else if (tileEntity instanceof TileAccelerator)
        {
            return new GuiAccelerator(player.inventory, ((TileAccelerator) tileEntity));
        }
        else if (tileEntity instanceof TileQuantumAssembler)
        {
            return new GuiQuantumAssembler(player.inventory, ((TileQuantumAssembler) tileEntity));
        }
        else if (tileEntity instanceof TileNuclearBoiler)
        {
            return new GuiNuclearBoiler(player.inventory, ((TileNuclearBoiler) tileEntity));
        }
        else if (tileEntity instanceof TileReactorCell)
        {
            return new GuiReactorCell(player.inventory, (TileReactorCell) tileEntity);
        }

        return null;
    }

    @Override
    public boolean isFancyGraphics()
    {
        return Minecraft.getMinecraft().gameSettings.fancyGraphics;
    }

}
