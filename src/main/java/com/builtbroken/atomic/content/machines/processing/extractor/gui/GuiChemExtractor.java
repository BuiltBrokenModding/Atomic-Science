package com.builtbroken.atomic.content.machines.processing.extractor.gui;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.lib.LanguageUtility;
import com.builtbroken.atomic.lib.gui.GuiContainerBase;
import com.builtbroken.atomic.lib.gui.tip.ToolTipTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class GuiChemExtractor extends GuiContainerBase<TileEntityChemExtractor>
{
    public final Rectangle AREA_BLUE_TANK = new Rectangle(8, 20, meterWidth, meterHeight);
    public final Rectangle AREA_GREEN_TANK = new Rectangle(155, 20, meterWidth, meterHeight);

    public GuiChemExtractor(EntityPlayer player, TileEntityChemExtractor host)
    {
        super(new ContainerChemExtractor(player, host), host);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        addToolTip(new ToolTipTank(AREA_BLUE_TANK, TOOLTIP_TANK, host.getInputTank()));
        addToolTip(new ToolTipTank(AREA_GREEN_TANK, TOOLTIP_TANK, host.getOutputTank()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawStringCentered(LanguageUtility.getLocal("tile." + AtomicScience.PREFIX + "chem.extractor.gui"), xSize / 2, 5);
        drawString("Inventory", 8, 73);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
        for (Object object : inventorySlots.inventorySlots)
        {
            if (object instanceof Slot)
            {
                drawSlot((Slot) object);
            }
        }

        //Render progress arrow
        int x = 72;
        int y = 30;
        renderFurnaceCookArrow(x, y, host.processTimer, TileEntityChemExtractor.PROCESSING_TIME);

        drawFluidTank(8, 20, host.getInputTank(), Color.blue);
        drawFluidTank(155, 20, host.getOutputTank(), Color.green);

        drawElectricity(34, 15, host.getEnergyStored() / (float) host.getMaxEnergyStored());
    }
}
