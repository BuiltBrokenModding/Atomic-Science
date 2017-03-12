package resonantinduction.atomic.process.fission;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import resonant.lib.gui.GuiContainerBase;
import resonantinduction.atomic.process.ContainerChemicalExtractor;
import resonantinduction.atomic.process.TileChemicalExtractor;
import universalelectricity.api.energy.UnitDisplay.Unit;

public class GuiChemicalExtractor extends GuiContainerBase
{
    private TileChemicalExtractor tileEntity;

    public GuiChemicalExtractor(InventoryPlayer par1InventoryPlayer, TileChemicalExtractor tileEntity)
    {
        super(new ContainerChemicalExtractor(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(tileEntity.getInvName(), 45, 6, 4210752);

        this.renderUniversalDisplay(8, 112, TileChemicalExtractor.ENERGY * 20, mouseX, mouseY, Unit.WATT);
        this.renderUniversalDisplay(100, 112, this.tileEntity.getVoltageInput(null), mouseX, mouseY, Unit.VOLTAGE);

        this.fontRenderer.drawString("The extractor can extract", 8, 75, 4210752);
        this.fontRenderer.drawString("uranium, deuterium and tritium.", 8, 85, 4210752);
        this.fontRenderer.drawString("Place them in the input slot.", 8, 95, 4210752);

        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        if (this.isPointInRegion(8, 18, this.meterWidth, this.meterHeight, mouseX, mouseY) && this.tileEntity.inputTank.getFluid() != null)
        {
            if (this.tileEntity.inputTank.getFluid() != null)
            {
                this.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop + 10, this.tileEntity.inputTank.getFluid().getFluid().getLocalizedName(), this.tileEntity.inputTank.getFluid().amount + " L");
            }
        }
        if (this.isPointInRegion(154, 18, this.meterWidth, this.meterHeight, mouseX, mouseY) && this.tileEntity.outputTank.getFluid() != null)
        {
            if (this.tileEntity.outputTank.getFluid() != null)
            {
                this.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop + 10, this.tileEntity.outputTank.getFluid().getFluid().getLocalizedName(), this.tileEntity.outputTank.getFluid().amount + " L");
            }
        }
        if (this.isPointInRegion(134, 49, 18, 18, mouseX, mouseY))
        {
            if (this.tileEntity.getStackInSlot(4) == null)
            {
                // this.drawTooltip(x - this.guiLeft, y - this.guiTop + 10, "Place empty cells.");
            }
        }
        if (this.isPointInRegion(52, 24, 18, 18, mouseX, mouseY))
        {
            if (this.tileEntity.outputTank.getFluidAmount() > 0 && this.tileEntity.getStackInSlot(3) == null)
            {
                this.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop + 10, "Input slot");
            }
        }
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(par1, x, y);

        drawSlot(79, 49, SlotType.BATTERY);
        drawSlot(52, 24);
        drawSlot(106, 24);
        drawBar(75, 24, (float) tileEntity.time / (float) TileChemicalExtractor.TICK_TIME);
        drawMeter(8, 18, (float) tileEntity.inputTank.getFluidAmount() / (float) tileEntity.inputTank.getCapacity(), tileEntity.inputTank.getFluid());
        drawSlot(24, 18, SlotType.LIQUID);
        drawSlot(24, 49, SlotType.LIQUID);
        drawMeter(154, 18, (float) tileEntity.outputTank.getFluidAmount() / (float) tileEntity.outputTank.getCapacity(), tileEntity.outputTank.getFluid());
        drawSlot(134, 18, SlotType.LIQUID);
        drawSlot(134, 49, SlotType.LIQUID);
    }
}