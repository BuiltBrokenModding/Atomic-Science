package com.builtbroken.atomic.content.machines.sensors.thermal.gui;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.sensors.thermal.TileEntityThermalRedstone;
import com.builtbroken.atomic.lib.LanguageUtility;
import com.builtbroken.atomic.lib.gui.GuiContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class GuiThermalRedstone extends GuiContainerBase<TileEntityThermalRedstone>
{
    private GuiTextField minTriggerField;
    private GuiTextField maxTriggerField;

    public GuiThermalRedstone(EntityPlayer player, TileEntityThermalRedstone host)
    {
        super(new ContainerThermalRedstone(player, host), host);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        fields.add(minTriggerField = new GuiTextField(0, fontRenderer, 72, 28, 30, 12));
        fields.add(maxTriggerField = new GuiTextField(1, fontRenderer, 72, 48, 30, 12));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 43, this.height / 2 - 10, 35, 20, getLocal("button.set")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 2)
        {
            try
            {
                int min = Integer.parseInt(minTriggerField.getText().trim());
                int max = Integer.parseInt(maxTriggerField.getText().trim());
                host.setTriggerClient(min, max);
            }
            catch (Exception e)
            {
                e.printStackTrace(); //TODO display error to user
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString("\u00a77" + getLocal("label.title"), 65, 6, 4210752);

        this.fontRenderer.drawString(getLocal("label.min"), 12, 28, 4210752);
        this.fontRenderer.drawString(getLocal("label.max"), 12, 48, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }

    private String getLocal(String suffix)
    {
        return LanguageUtility.getLocal("gui." + AtomicScience.PREFIX + "sensor.thermal.redstone." + suffix);
    }
}
