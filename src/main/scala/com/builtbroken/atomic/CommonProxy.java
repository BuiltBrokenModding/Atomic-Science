package com.builtbroken.atomic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.prefab.ProxyBase;
import com.builtbroken.atomic.fission.reactor.ContainerReactorCell;
import com.builtbroken.atomic.fission.reactor.TileReactorCell;
import com.builtbroken.atomic.fusion.ContainerNuclearBoiler;
import com.builtbroken.atomic.particle.accelerator.ContainerAccelerator;
import com.builtbroken.atomic.particle.accelerator.TileAccelerator;
import com.builtbroken.atomic.particle.quantum.ContainerQuantumAssembler;
import com.builtbroken.atomic.particle.quantum.TileQuantumAssembler;
import com.builtbroken.atomic.process.ContainerChemicalExtractor;
import com.builtbroken.atomic.process.TileChemicalExtractor;
import com.builtbroken.atomic.process.fission.ContainerCentrifuge;
import com.builtbroken.atomic.process.fission.TileCentrifuge;
import com.builtbroken.atomic.process.fission.TileNuclearBoiler;

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
