package resonantinduction.atomic.particle.fulmination;

import java.util.EnumSet;

import net.minecraft.block.material.Material;
import net.minecraftforge.common.ForgeDirection;
import resonant.lib.utility.ConnectedTextureRenderer;
import resonant.lib.content.module.TileRender;
import resonant.lib.prefab.tile.TileElectrical;
import resonantinduction.core.Reference;
import universalelectricity.api.electricity.IVoltageOutput;
import universalelectricity.api.energy.EnergyStorageHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Fulmination TileEntity */
public class TileFulmination extends TileElectrical implements IVoltageOutput
{
    private static final long DIAN = 10000000000000L;

    public TileFulmination()
    {
        super(Material.iron);
        energy = new EnergyStorageHandler(DIAN);
        blockHardness = 10;
        blockResistance = 25000;
    }

    @Override
    public void initiate()
    {
        super.initiate();
        FulminationHandler.INSTANCE.register(this);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        produce();
        // Slowly lose energy.
        energy.extractEnergy(1, true);
    }

    @Override
    public void invalidate()
    {
        FulminationHandler.INSTANCE.unregister(this);
        super.initiate();
    }

    @Override
    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        return 0;
    }

    @Override
    public EnumSet<ForgeDirection> getInputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public EnumSet<ForgeDirection> getOutputDirections()
    {
        return EnumSet.allOf(ForgeDirection.class);
    }

    @Override
    public long getVoltageOutput(ForgeDirection side)
    {
        return 10000000000L;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected TileRender newRenderer()
    {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }
}
