package com.builtbroken.atomic.content.fulmination;

import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.prefab.tile.TileModuleMachine;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.common.util.ForgeDirection;

/** Fulmination TileEntity */
public class TileFulmination extends TileModuleMachine implements IEnergyBufferProvider
{
    private static final long DIAN = 10000000000000L;

    public TileFulmination()
    {
        super("fulmination", Material.iron);
    }

    @Override
    public void firstTick()
    {
        super.firstTick();
        FulminationHandler.INSTANCE.register(this);
    }

    @Override
    public void update()
    {
        super.update();
        //TODO export energy
    }

    @Override
    public void invalidate()
    {
        FulminationHandler.INSTANCE.unregister(this);
        super.invalidate();
    }

    @Override
    public IEnergyBuffer getEnergyBuffer(ForgeDirection side)
    {
        return null;
    }

    @Override
    protected IInventory createInventory()
    {
        return null;
    }
}
