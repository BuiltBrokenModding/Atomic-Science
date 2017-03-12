package resonantinduction.atomic.particle.accelerator;

import net.minecraft.entity.player.InventoryPlayer;
import resonant.lib.gui.GuiContainerBase;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;
import universalelectricity.api.vector.Vector3;

public class GuiAccelerator extends GuiContainerBase
{
    private TileAccelerator tileEntity;

    private int containerWidth;
    private int containerHeight;

    public GuiAccelerator(InventoryPlayer par1InventoryPlayer, TileAccelerator tileEntity)
    {
        super(new ContainerAccelerator(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int x, int y)
    {
        this.fontRenderer.drawString(tileEntity.getInvName(), 40, 10, 4210752);

        String status = "";
        Vector3 position = new Vector3(this.tileEntity);
        position.translate(this.tileEntity.getDirection().getOpposite());

        if (!EntityParticle.canRenderAcceleratedParticle(this.tileEntity.worldObj, position))
        {
            status = "\u00a74Fail to emit; try rotating.";
        }
        else if (this.tileEntity.entityParticle != null && this.tileEntity.velocity > 0)
        {
            status = "\u00a76Accelerating";
        }
        else
        {
            status = "\u00a72Idle";
        }

        this.fontRenderer.drawString("Velocity: " + Math.round((this.tileEntity.velocity / TileAccelerator.clientParticleVelocity) * 100) + "%", 8, 27, 4210752);
        this.fontRenderer.drawString("Energy Used:", 8, 38, 4210752);
        this.fontRenderer.drawString(UnitDisplay.getDisplay(this.tileEntity.totalEnergyConsumed, Unit.JOULES), 8, 49, 4210752);
        this.fontRenderer.drawString(UnitDisplay.getDisplay(TileAccelerator.energyPerTick * 20, Unit.WATT), 8, 60, 4210752);
        this.fontRenderer.drawString(UnitDisplay.getDisplay(this.tileEntity.getVoltageInput(null), Unit.VOLTAGE), 8, 70, 4210752);
        this.fontRenderer.drawString("Antimatter: " + this.tileEntity.antimatter + " mg", 8, 80, 4210752);
        this.fontRenderer.drawString("Status:", 8, 90, 4210752);
        this.fontRenderer.drawString(status, 8, 100, 4210752);
        this.fontRenderer.drawString("Buffer: " + UnitDisplay.getDisplayShort(this.tileEntity.getEnergyHandler().getEnergy(), Unit.JOULES) + "/" + UnitDisplay.getDisplayShort(this.tileEntity.getEnergyHandler().getEnergyCapacity(), Unit.JOULES), 8, 110,
                4210752);
        this.fontRenderer.drawString("Facing: " + this.tileEntity.getDirection().getOpposite(), 100, 123, 4210752);
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(par1, x, y);

        this.drawSlot(131, 25);
        this.drawSlot(131, 50);
        this.drawSlot(131, 74);
        this.drawSlot(105, 74);
    }
}