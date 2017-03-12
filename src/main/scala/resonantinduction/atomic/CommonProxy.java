package resonantinduction.atomic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.prefab.ProxyBase;
import resonantinduction.atomic.fission.reactor.ContainerReactorCell;
import resonantinduction.atomic.fission.reactor.TileReactorCell;
import resonantinduction.atomic.fusion.ContainerNuclearBoiler;
import resonantinduction.atomic.particle.accelerator.ContainerAccelerator;
import resonantinduction.atomic.particle.accelerator.TileAccelerator;
import resonantinduction.atomic.particle.quantum.ContainerQuantumAssembler;
import resonantinduction.atomic.particle.quantum.TileQuantumAssembler;
import resonantinduction.atomic.process.ContainerChemicalExtractor;
import resonantinduction.atomic.process.TileChemicalExtractor;
import resonantinduction.atomic.process.fission.ContainerCentrifuge;
import resonantinduction.atomic.process.fission.TileCentrifuge;
import resonantinduction.atomic.process.fission.TileNuclearBoiler;

public class CommonProxy extends ProxyBase
{
    public int getArmorIndex(String armor)
    {
        return 0;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof TileCentrifuge)
        {
            return new ContainerCentrifuge(player.inventory, ((TileCentrifuge) tileEntity));
        }
        else if (tileEntity instanceof TileChemicalExtractor)
        {
            return new ContainerChemicalExtractor(player.inventory, ((TileChemicalExtractor) tileEntity));
        }
        else if (tileEntity instanceof TileAccelerator)
        {
            return new ContainerAccelerator(player.inventory, ((TileAccelerator) tileEntity));
        }
        else if (tileEntity instanceof TileQuantumAssembler)
        {
            return new ContainerQuantumAssembler(player.inventory, ((TileQuantumAssembler) tileEntity));
        }
        else if (tileEntity instanceof TileNuclearBoiler)
        {
            return new ContainerNuclearBoiler(player.inventory, ((TileNuclearBoiler) tileEntity));
        }
        else if (tileEntity instanceof TileReactorCell)
        {
            return new ContainerReactorCell(player, ((TileReactorCell) tileEntity));
        }

        return null;
    }

    public boolean isFancyGraphics()
    {
        return false;
    }
}
