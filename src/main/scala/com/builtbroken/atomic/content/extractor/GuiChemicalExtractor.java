package com.builtbroken.atomic.content.extractor;

import com.builtbroken.jlib.data.science.units.UnitDisplay;
import com.builtbroken.mc.prefab.gui.GuiContainerBase;
import com.builtbroken.mc.prefab.gui.GuiSlotType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class GuiChemicalExtractor extends GuiContainerBase
{
    private TileChemicalExtractor tileEntity;

    public GuiChemicalExtractor(EntityPlayer player, TileChemicalExtractor tileEntity)
    {
        super(new ContainerChemicalExtractor(player, tileEntity));
        this.tileEntity = tileEntity;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRendererObj.drawString(tileEntity.getInventoryName(), 45, 6, 4210752);

        this.renderUniversalDisplay(8, 112, TileChemicalExtractor.ENERGY * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);
        //this.renderUniversalDisplay(100, 112, this.tileEntity.getVoltageInput(null), mouseX, mouseY, Unit.VOLTAGE);

        fontRendererObj.drawString("The extractor can extract", 8, 75, 4210752);
        fontRendererObj.drawString("uranium, deuterium and tritium.", 8, 85, 4210752);
        fontRendererObj.drawString("Place them in the input slot.", 8, 95, 4210752);

        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        /* TODO re-add tooltips
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
         */
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(par1, x, y);

        drawSlot(79, 49, GuiSlotType.BATTERY);
        drawSlot(52, 24);
        drawSlot(106, 24);
        // drawBar(75, 24, (float) tileEntity.time / (float) TileChemicalExtractor.TICK_TIME);
        drawMeter(8, 18, (float) tileEntity.inputTank.getFluidAmount() / (float) tileEntity.inputTank.getCapacity(), tileEntity.inputTank.getFluid());
        drawSlot(24, 18, GuiSlotType.LIQUID);
        drawSlot(24, 49, GuiSlotType.LIQUID);
        drawMeter(154, 18, (float) tileEntity.outputTank.getFluidAmount() / (float) tileEntity.outputTank.getCapacity(), tileEntity.outputTank.getFluid());
        drawSlot(134, 18, GuiSlotType.LIQUID);
        drawSlot(134, 49, GuiSlotType.LIQUID);
    }
}