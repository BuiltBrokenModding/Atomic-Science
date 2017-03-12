package resonantinduction.atomic;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import resonant.lib.render.block.BlockRenderingHandler;
import resonantinduction.atomic.fission.reactor.GuiReactorCell;
import resonantinduction.atomic.fission.reactor.RenderReactorCell;
import resonantinduction.atomic.fission.reactor.TileReactorCell;
import resonantinduction.atomic.fusion.RenderPlasmaHeater;
import resonantinduction.atomic.fusion.TilePlasmaHeater;
import resonantinduction.atomic.particle.accelerator.EntityParticle;
import resonantinduction.atomic.particle.accelerator.GuiAccelerator;
import resonantinduction.atomic.particle.accelerator.RenderParticle;
import resonantinduction.atomic.particle.accelerator.TileAccelerator;
import resonantinduction.atomic.particle.quantum.GuiQuantumAssembler;
import resonantinduction.atomic.particle.quantum.TileQuantumAssembler;
import resonantinduction.atomic.process.RenderChemicalExtractor;
import resonantinduction.atomic.process.TileChemicalExtractor;
import resonantinduction.atomic.process.fission.GuiCentrifuge;
import resonantinduction.atomic.process.fission.GuiChemicalExtractor;
import resonantinduction.atomic.process.fission.GuiNuclearBoiler;
import resonantinduction.atomic.process.fission.RenderCentrifuge;
import resonantinduction.atomic.process.fission.RenderNuclearBoiler;
import resonantinduction.atomic.process.fission.TileCentrifuge;
import resonantinduction.atomic.process.fission.TileNuclearBoiler;
import resonantinduction.atomic.process.sensor.RenderThermometer;
import resonantinduction.atomic.process.sensor.TileThermometer;
import resonantinduction.atomic.process.turbine.RenderElectricTurbine;
import resonantinduction.atomic.process.turbine.TileElectricTurbine;
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
