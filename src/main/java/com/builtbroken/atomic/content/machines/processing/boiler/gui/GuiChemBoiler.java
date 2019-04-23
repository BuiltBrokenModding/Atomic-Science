package com.builtbroken.atomic.content.machines.processing.boiler.gui;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.lib.LanguageUtility;
import com.builtbroken.atomic.lib.gui.GuiContainerBase;
import com.builtbroken.atomic.lib.gui.tip.ToolTipTank;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class GuiChemBoiler extends GuiContainerBase<TileEntityChemBoiler>
{
    public final Rectangle AREA_BLUE_TANK = new Rectangle(8, 20, meterWidth, meterHeight);
    public final Rectangle AREA_GREEN_TANK = new Rectangle(135, 5, meterWidth, meterHeight);
    public final Rectangle AREA_YELLOW_TANK = new Rectangle(153, 5, meterWidth, meterHeight);

    public GuiChemBoiler(EntityPlayer player, TileEntityChemBoiler host)
    {
        super(new ContainerChemBoiler(player, host), host);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        addToolTip(new ToolTipTank(AREA_BLUE_TANK, TOOLTIP_TANK, host.getBlueTank()));
        addToolTip(new ToolTipTank(AREA_GREEN_TANK, TOOLTIP_TANK, host.getGreenTank()));
        addToolTip(new ToolTipTank(AREA_YELLOW_TANK, TOOLTIP_TANK, host.getYellowTank()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawStringCentered(LanguageUtility.getLocal("tile." + AtomicScience.PREFIX + "chem.boiler.gui"), xSize / 2, 5);
        drawString("Inventory", 8, 73);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
        drawContainerSlots();

        //Render progress arrow
        int x = 72;
        int y = 30;
        renderFurnaceCookArrow(x, y, host.processTimer, TileEntityChemBoiler.PROCESSING_TIME);

        drawFluidTank(8, 20, host.getBlueTank(), Color.blue);
        drawFluidTank(135, 5, host.getGreenTank(), Color.green);
        drawFluidTank(153, 5, host.getYellowTank(), Color.yellow);

        drawElectricity(25, 15, host.getEnergyStored() / (float) host.getMaxEnergyStored());
    }
}
