package resonantinduction.atomic.process.fission;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import resonant.lib.gui.GuiContainerBase;
import universalelectricity.api.energy.UnitDisplay.Unit;

public class GuiCentrifuge extends GuiContainerBase
{
    private TileCentrifuge tileEntity;

    public GuiCentrifuge(InventoryPlayer par1InventoryPlayer, TileCentrifuge tileEntity)
    {
        super(new ContainerCentrifuge(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(tileEntity.getInvName(), 60, 6, 4210752);

        String displayText = "";

        if (this.tileEntity.timer > 0)
        {
            displayText = "Processing";
        }
        else if (this.tileEntity.nengYong())
        {
            displayText = "Ready";
        }
        else
        {
            displayText = "Idle";
        }

        this.fontRenderer.drawString("Status: " + displayText, 70, 50, 4210752);

        this.renderUniversalDisplay(8, 112, TileNuclearBoiler.DIAN * 20, mouseX, mouseY, Unit.WATT);
        this.renderUniversalDisplay(100, 112, this.tileEntity.getVoltageInput(null), mouseX, mouseY, Unit.VOLTAGE);

        this.fontRenderer.drawString("The centrifuge spins", 8, 75, 4210752);
        this.fontRenderer.drawString("uranium hexafluoride gas into", 8, 85, 4210752);
        this.fontRenderer.drawString("enriched uranium for fission.", 8, 95, 4210752);

        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        if (this.isPointInRegion(8, 18, this.meterWidth, this.meterHeight, mouseX, mouseY) && this.tileEntity.gasTank.getFluid() != null)
        {
            this.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop + 10, this.tileEntity.gasTank.getFluid().getFluid().getLocalizedName(), this.tileEntity.gasTank.getFluid().amount + " L");
        }
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(par1, x, y);

        this.drawSlot(80, 25);
        this.drawSlot(100, 25);
        this.drawSlot(130, 25, SlotType.BATTERY);

        this.drawBar(40, 26, (float) this.tileEntity.timer / (float) TileCentrifuge.SHI_JIAN);

        // Uranium Gas
        this.drawMeter(8, 18, (float) this.tileEntity.gasTank.getFluidAmount() / (float) this.tileEntity.gasTank.getCapacity(), this.tileEntity.gasTank.getFluid());
        this.drawSlot(24, 49, SlotType.GAS);
    }
}