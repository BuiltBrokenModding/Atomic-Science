package com.builtbroken.atomic.content.machines.sensors.thermal.gui;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.items.ItemHeatProbe;
import com.builtbroken.atomic.content.machines.sensors.thermal.TileEntityThermalRedstone;
import com.builtbroken.atomic.lib.LanguageUtility;
import com.builtbroken.atomic.lib.gui.GuiContainerBase;
import com.builtbroken.atomic.lib.timer.TickTimerConditional;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.io.IOException;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class GuiThermalRedstone extends GuiContainerBase<TileEntityThermalRedstone>
{
    private GuiTextField minTriggerField;
    private GuiTextField maxTriggerField;

    private String infoMessage;

    private TickTimerConditional fieldRefreshTimer = TickTimerConditional.newSimple(40, ticks -> refreshFields())
            .setShouldTickFunction(() -> !areFieldsSelected())
            .setShouldResetFunction(() -> areFieldsSelected());

    public GuiThermalRedstone(EntityPlayer player, TileEntityThermalRedstone host)
    {
        super(new ContainerThermalRedstone(player, host), host);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        fields.add(minTriggerField = new GuiTextField(0, fontRenderer, guiLeft + 72, guiTop + 28, 80, 12));
        fields.add(maxTriggerField = new GuiTextField(1, fontRenderer, guiLeft + 72, guiTop + 48, 80, 12));
        refreshFields();

        this.buttonList.add(new GuiButton(2, guiLeft + 130, guiTop + 64, 35, 20, getLocal("button.set")));
        this.buttonList.add(new GuiButton(3, guiLeft + 110, guiTop + 125, 55, 20, getLocal("button.get")));
    }

    private String formatHeat(int number)
    {
        String string = ItemHeatProbe.formatTemp(number);
        string = string.substring(0, string.length() - 1).trim();
        string = string.replaceAll("\\s", "");
        return string;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        fieldRefreshTimer.tick();
    }

    protected boolean areFieldsSelected()
    {
        return minTriggerField.isFocused() || maxTriggerField.isFocused();
    }

    protected void refreshFields()
    {
        minTriggerField.setText("" + formatHeat(host.minHeatTrigger * 1000));
        maxTriggerField.setText("" + formatHeat(host.maxHeatTrigger * 1000));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 2)
        {
            try
            {
                //Clear info message
                infoMessage = null;

                //Parse inputs
                int min = parseInt(minTriggerField.getText());
                int max = parseInt(maxTriggerField.getText());

                //validate inputs
                if (min == -1 || max == -1)
                {
                    return;
                }
                if (min < 1000)
                {
                    infoMessage = "e:numbers under 1000 can't be used";
                    return;
                }
                if (max < min)
                {
                    infoMessage = "e:max needs to be larger than min";
                    return;
                }

                //Send packet
                host.setTriggerClient(min / 1000, max / 1000);

                //Confirm success
                infoMessage = "data set";
            }
            catch (Exception e)
            {
                e.printStackTrace(); //TODO display error to user
            }
        }
        else if (button.id == 3)
        {
            host.requestHeatValue();
        }
    }

    public int parseInt(String string)
    {
        //Null is assumed to be zero
        if (string == null)
        {
            return 0;
        }

        //format string for sanity reasons
        string = string.toLowerCase().trim();

        //Assume that empty is zero
        if (string.isEmpty())
        {
            return 0;
        }

        //Try catch for NumberFormat and parse errors
        try
        {
            //If number only do simple parse
            if (string.matches("[0-9]+"))
            {
                return Integer.parseInt(string);
            }

            //Ensure that our string only contains letters, numbers, and .
            if (!string.matches("^(?=.*[a-z])(?=.*[0-9.])[a-z0-9.]+$"))
            {
                infoMessage = "e:When using a suffix you need 1 letter and number minimal";
                return -1;
            }

            //Split decimal from suffix
            String[] split = string.split("(?<=[a-z])(?=[0-9.])|(?<=[0-9.])(?=[a-z])");
            String num = split[0];
            String suffix = split[1];

            //Convert number string to number
            double number = Double.parseDouble(num);

            //Apply suffix
            if (suffix.equals("k"))
            {
                return (int) Math.floor(number * 1000);
            }
            else if (suffix.equals("m"))
            {
                return (int) Math.floor(number * 1000 * 1000);
            }
            else if (suffix.equals("g"))
            {
                return (int) Math.floor(number * 1000 * 1000 * 1000);
            }
            //Error if user added j by mistake
            else if (suffix.endsWith("j"))
            {
                infoMessage = "e:No need to include J (joules) in fields";
                return -1;
            }
            //Error for unknown number
            else
            {
                infoMessage = "e:Unknown suffix: " + suffix;
                return -1;
            }
        }
        catch (NumberFormatException e)
        {
            infoMessage = "e:Not a number";
        }
        catch (Exception e)
        {
            e.printStackTrace(); //TODO display error to user
            infoMessage = "e:Not a number";
        }
        return -1;
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawStringCentered(getLocal("label.title"), xSize / 2, 6, 4210752);
        this.fontRenderer.drawString(getLocal("label.min"), 12, 30, 4210752);
        this.fontRenderer.drawString(getLocal("label.max"), 12, 50, 4210752);

        if (host.clientHeatValue >= 0)
        {
            this.fontRenderer.drawString(getLocal("label.heat.value") + " " + ItemHeatProbe.formatTemp(host.clientHeatValue * 1000L), 12, 80, 4210752);

            float scale = host.getHeatScale(host.clientHeatValue);
            int display = (int) Math.floor(scale * 100);
            this.fontRenderer.drawString(display + "%", 120, 92, 4210752);

            int redstone = host.getExpectedRedstoneValue(host.clientHeatValue);
            this.fontRenderer.drawString(getLocal("label.redstone") + " " + redstone, 12, 105, 4210752);
        }
        else
        {
            this.fontRenderer.drawString(getLocal("label.heat.none"), 12, 80, 4210752);
        }

        if (infoMessage != null && !infoMessage.isEmpty())
        {
            if (infoMessage.startsWith("e:"))
            {
                this.fontRenderer.drawString(getLocal("label.error") + " " + infoMessage.substring(2), 12, 150, 4210752); //TODO add string wrap
            }
            else
            {
                this.fontRenderer.drawString(getLocal("label.info") + " " + infoMessage, 12, 150, 4210752);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);

        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);
        setColor(null);

        int x = 10;
        int y = 90;
        float scale = host.getHeatScale(host.clientHeatValue);

        // Draw background progress bar
        this.drawTexturedModalRect(this.guiLeft + x, this.guiTop + y, 54, 0, 107, 11);

        if (scale > 0)
        {
            setColor(Color.RED);
            this.drawTexturedModalRect(this.guiLeft + x, this.guiTop + y, 54, 22, (int) (scale * 107), 11);
            setColor(null);
        }
    }

    private String getLocal(String suffix)
    {
        return LanguageUtility.getLocal("gui." + AtomicScience.PREFIX + "sensor.thermal.redstone." + suffix);
    }
}
