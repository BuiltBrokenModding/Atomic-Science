package com.builtbroken.atomic.lib.gui;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.lib.LanguageUtility;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class GuiContainerBase<H> extends GuiContainer
{
    public static final ResourceLocation GUI_COMPONENTS = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.GUI_TEXTURE_DIRECTORY  + "gui_components.png");
    public static final ResourceLocation GUI_MC_BASE = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.GUI_TEXTURE_DIRECTORY  + "mc_base.png");

    public ResourceLocation baseTexture;

    public String currentToolTip = "";

    protected HashMap<Rectangle, String> tooltips = new HashMap();
    protected ArrayList<GuiTextField> fields = new ArrayList();

    protected int meterHeight = 49;
    protected int meterWidth = 14;

    protected int containerWidth;
    protected int containerHeight;
    private float lastChangeFrameTime;

    /** Debug toogle to render text for the ID and inventory ID for a slot */
    public boolean renderSlotDebugIDs = false;

    /** Object that is the host of the GUI */
    protected H host;

    public GuiContainerBase(Container container, H host)
    {
        this(container);
        this.host = host;
    }

    public GuiContainerBase(Container container)
    {
        super(container);
        this.baseTexture = GUI_MC_BASE;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.fields.clear();
        this.tooltips.clear();
    }

    public void actionPerformedCallback(GuiButton button)
    {
        actionPerformed(button);
    }

    /**
     * Adds a button to the GUI
     *
     * @param button
     * @param <E>
     * @return
     */
    protected <E extends GuiButton> E addButton(E button)
    {
        buttonList.add(button);
        return button;
    }

    /**
     * Called to add a tooltip to the GUI
     *
     * @param triggerArea - area to show tip inside
     * @param text        - text to display
     */
    protected void addToolTip(Rectangle triggerArea, String text)
    {
        addToolTip(triggerArea, text, false);
    }

    /**
     * Called to add a tooltip translation to the GUI
     *
     * @param triggerArea - area to show tip inside
     * @param translation - translation key to get text
     */
    protected void addToolTipWithTranslation(Rectangle triggerArea, String translation)
    {
        addToolTip(triggerArea, translation, true);
    }

    /**
     * Adds a tooltip to the GUI
     *
     * @param triggerArea - area to show tip inside
     * @param text        - text to display
     * @param translate   - should the text be translated
     */
    protected void addToolTip(Rectangle triggerArea, String text, boolean translate)
    {
        String actual_text = text.trim();
        if (translate)
        {
            String translation = LanguageUtility.getLocal(actual_text);
            if (translation != null && !translation.isEmpty())
            {
                actual_text = translation.trim();
            }
        }
        tooltips.put(triggerArea, actual_text);
    }

    protected void drawString(String str, int x, int y, int color)
    {
        fontRendererObj.drawString(str, x, y, color);
    }

    protected void drawString(String str, int x, int y)
    {
        drawString(str, x, y, 4210752);
    }

    protected void drawString(String str, int x, int y, Color color)
    {
        drawString(str, x, y, color.getRGB());
    }

    protected void drawStringCentered(String str, int x, int y)
    {
        drawStringCentered(str, x, y, 4210752);
    }

    protected void drawStringCentered(String str, int x, int y, Color color)
    {
        drawStringCentered(str, x, y, color.getRGB());
    }

    protected void drawStringCentered(String str, int x, int y, int color)
    {
        drawString(str, x - (fontRendererObj.getStringWidth(str) / 2), y, color);
    }

    protected GuiTextField newField(int x, int y, int w, String msg)
    {
        return this.newField(x, y, w, 20, msg);
    }

    protected GuiTextField newField(int x, int y, int w, int h, String msg)
    {
        GuiTextField x_field = new GuiTextField(this.fontRendererObj, x, y, w, h);
        x_field.setText("" + msg);
        x_field.setMaxStringLength(15);
        x_field.setTextColor(16777215);
        fields.add(x_field);
        return x_field;
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float p_73863_3_)
    {
        super.drawScreen(mouseX, mouseY, p_73863_3_);

        ///============================================================
        ///================Render Fields===============================
        ///============================================================
        if (fields != null && fields.size() > 0)
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            for (GuiTextField field : fields)
            {
                field.drawTextBox();
            }
        }

        ///============================================================
        ///===============Render tooltips===============================
        ///============================================================
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        //TODO rework to be object based
        //TODO rework to be attached to components rather than free floating
        for (Entry<Rectangle, String> entry : this.tooltips.entrySet())
        {
            if (entry.getKey().contains(new Point(mouseX - this.guiLeft, mouseY - this.guiTop)))
            {
                this.currentToolTip = entry.getValue();
                break;
            }
        }

        if (this.currentToolTip != null && this.currentToolTip != "")
        {
            this.drawTooltip(mouseX, mouseY, currentToolTip.split(";"));
        }

        this.currentToolTip = "";

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        ///============================================================
    }

    @Override
    protected void keyTyped(char c, int id)
    {
        //Key for debug render
        if (id == Keyboard.KEY_INSERT)
        {
            renderSlotDebugIDs = !renderSlotDebugIDs;
        }
        else
        {
            boolean f = false;
            for (GuiTextField field : fields)
            {
                field.textboxKeyTyped(c, id);
                if (field.isFocused())
                {
                    return;
                }
            }
            if (!f)
            {
                super.keyTyped(c, id);
            }
        }
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        for (GuiTextField field : fields)
        {
            field.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;

        this.mc.renderEngine.bindTexture(this.baseTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
    }

    //TODO update and docs
    protected void drawBulb(int x, int y, boolean isOn)
    {
        this.mc.renderEngine.bindTexture(this.baseTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (isOn)
        {
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 161, 0, 6, 6);

        }
        else
        {
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 161, 4, 6, 6);
        }
    }

    //TODO update and docs
    protected void drawSlot(int x, int y, ItemStack itemStack)
    {
        this.mc.renderEngine.bindTexture(this.baseTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18);

        if (itemStack != null)
        {
            this.drawItemStack(itemStack, this.containerWidth + x, this.containerHeight + y);
        }
    }

    //TODO update and docs
    protected void drawItemStack(ItemStack itemStack, int x, int y)
    {
        x += 1;
        y += 1;
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);

        // drawTexturedModelRectFromIcon
        // GL11.glEnable(GL11.GL_BLEND);
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.renderEngine, itemStack, x, y);
        // GL11.glDisable(GL11.GL_BLEND);
    }

    //TODO update and docs
    protected void drawTextWithTooltip(String textName, String format, int x, int y, int mouseX, int mouseY)
    {
        this.drawTextWithTooltip(textName, format, x, y, mouseX, mouseY, 4210752);
    }

    //TODO update and docs
    protected void drawTextWithTooltip(String textName, String format, int x, int y, int mouseX, int mouseY, int color)
    {
        String name = LanguageUtility.getLocal("gui." + textName + ".name");
        String text = format.replaceAll("%1", name);
        fontRendererObj.drawString(text, x, y, color);

        String tooltip = LanguageUtility.getLocal("gui." + textName + ".tooltip");

        if (tooltip != null && tooltip != "")
        {
            if (new Rectangle(x, y, (int) (text.length() * 4.8), 12).contains(new Point(mouseX, mouseY)))
            {
                this.currentToolTip = tooltip;
            }
        }
    }

    //TODO update and docs
    protected void drawTextWithTooltip(String textName, int x, int y, int mouseX, int mouseY)
    {
        this.drawTextWithTooltip(textName, "%1", x, y, mouseX, mouseY);
    }

    protected void drawContainerSlots()
    {
        for (Object object : inventorySlots.inventorySlots)
        {
            drawSlot((Slot) object);
        }
    }

    //TODO update and docs
    protected void drawSlot(Slot slot)
    {
        drawSlot(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1); //TODO add option to ISlotRender to disable default rendering

        if (slot instanceof ISlotRender)
        {
            //Only draw slot background if empty
            if (!slot.getHasStack())
            {
                ((ISlotRender) slot).renderSlotOverlay(this, this.containerWidth + slot.xDisplayPosition - 1, this.containerHeight + slot.yDisplayPosition - 1);
            }
            //TODO add foreground rendering for slot
        }
        if (AtomicScience.runningAsDev && renderSlotDebugIDs)
        {
            this.drawStringCentered("" + slot.getSlotIndex(), guiLeft + slot.xDisplayPosition + 9, guiTop + slot.yDisplayPosition + 9, Color.YELLOW);
            this.drawStringCentered("" + slot.slotNumber, guiLeft + slot.xDisplayPosition + 9, guiTop + slot.yDisplayPosition + 1, Color.RED);
        }
    }

    //TODO update and docs
    protected void drawSlot(int x, int y)
    {
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18);
    }

    /**
     * Draws a large green fill bar, with background, for
     * use in rendering gauges or progress bars
     *
     * @param x       - render pos
     * @param y       - render pos
     * @param percent - 0f to 1f on how full the bar should render
     * @param color   - color of the bar, null uses default
     */
    public void drawLargeBar(int x, int y, int w, float percent, Color color)
    {
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);

        int width = Math.round(percent * 138);
        //Draws bar background
        setColor(null);
        drawRectWithScaledWidth(containerWidth + x, containerHeight + y, 54, 33, 140, 15, w);
        //draws the percent fill bar
        setColor(color);
        drawRectWithScaledWidth(containerWidth + x + 1, containerHeight + y + 1, 55, 65, width, 13, w);
    }

    /**
     * Draws a smaller green fill bar than {@link #drawLargeBar(int, int, int, float, Color)},
     * with background, for use in rendering gauges or progress bars
     *
     * @param x       - render pos
     * @param y       - render pos
     * @param percent - 0f to 1f on how full the bar should render
     * @param color   - color of the bar, null uses default
     */
    public void drawSmallBar(int x, int y, int w, float percent, Color color)
    {
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);

        final int width = Math.round(percent * 105);
        //Draws bar background
        setColor(null);
        drawRectWithScaledWidth(containerWidth + x, containerHeight + y, 54, 0, 107, 11, w);

        //draws the percent fill bar
        setColor(color);
        drawRectWithScaledWidth(containerWidth + x + 1, containerHeight + y + 1, 55, 24, width, 9, w);
    }

    /**
     * Draws a smaller green fill bar than {@link #drawSmallBar(int, int, int, float, Color)},
     * with background, for use in rendering gauges or progress bars
     *
     * @param x       - render pos
     * @param y       - render pos
     * @param percent - 0f to 1f on how full the bar should render
     * @param color   - color of the bar, null uses default
     */
    public void drawMicroBar(int x, int y, float percent, Color color)
    {
        drawMicroBar(x, y, -1, percent, color);
    }

    /**
     * Draws a smaller green fill bar than {@link #drawSmallBar(int, int, int, float, Color)},
     * with background, for use in rendering gauges or progress bars
     *
     * @param x       - render pos
     * @param y       - render pos
     * @param w       - width of the bar, min 6
     * @param percent - 0f to 1f on how full the bar should render
     * @param color   - color of the bar, null uses default
     */
    public void drawMicroBar(int x, int y, int w, float percent, Color color)
    {
        //Local constants
        final int backgroundWidth = 56;
        final int fillBarWidth = 54;

        //Test texture to correct resource
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);


        //Render background bar
        setColor(null);
        drawRectWithScaledWidth(containerWidth + x, containerHeight + y, 54, 79, backgroundWidth, 7, w);


        //Render foreground bar
        final int width = Math.round(percent * fillBarWidth);
        setColor(color);
        drawRectWithScaledWidth(containerWidth + x + 1, containerHeight + y + 1, 55, 87, width, 5, (int) ((w - 2) * percent));
    }

    /**
     * Draws a rectangle with an increased or decreased width value
     * <p>
     * This works by duplicating the middle (3, width - 3) of the rectangle
     *
     * @param x        - render pos
     * @param y        - render pos
     * @param u        - x pos of the texture in it's texture sheet
     * @param v        - y pos of the texture in it's texture sheet
     * @param width    - width of the texture
     * @param height   - height of the texture
     * @param newWidth - new width to render the rectangle, minimal size of 6
     */
    protected void drawRectWithScaledWidth(int x, int y, int u, int v, int width, int height, int newWidth)
    {
        if (width > 0)
        {
            //If both widths are the same redirect to original call
            if (newWidth <= 0 || width == newWidth)
            {
                drawTexturedModalRect(x, y, u, v, width, height);
            }

            //Size of the middle section of the image
            final int midWidth = width - 6;

            //Start cap of image rect
            drawTexturedModalRect(x, y, u, v, 3, height);
            x += 3;

            //only render middle if it is larger than 6
            if (newWidth > 6)
            {
                //Loop over number of sections that need to be rendered
                int loops = newWidth / width;
                while (loops > 0)
                {
                    drawTexturedModalRect(x, y, u + 3, v, midWidth, height);
                    x += midWidth;
                    loops -= 1;
                }

                //Check if there is a remainder that still needs rendered
                loops = newWidth % width;
                if (loops != 0)
                {
                    drawTexturedModalRect(x, y, u + 3, v, loops, height);
                    x += loops;
                }
            }

            if (width > 3)
            {
                //End cap of image rect
                drawTexturedModalRect(x, y, u + width - 3, v, 3, height);
            }
        }
    }

    /**
     * Sets the render color for the GUI render
     *
     * @param color - color, null will force default
     */
    protected void setColor(Color color)
    {
        if (color == null)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        }
    }

    //TODO update and docs
    protected void drawElectricity(int x, int y, float scale)
    {
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        /** Draw background progress bar/ */
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 0, 107, 11);

        if (scale > 0)
        {
            /** Draw white color actual progress. */
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 22, (int) (scale * 107), 11);
        }
    }

    //TODO update and docs
    protected void drawMeter(int x, int y, float scale, float r, float g, float b)
    {
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        /** Draw the background meter. */
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 0, this.meterWidth, this.meterHeight);

        /** Draw liquid/gas inside */
        GL11.glColor4f(r, g, b, 1.0F);
        int actualScale = (int) ((this.meterHeight - 1) * scale);
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y + (this.meterHeight - 1 - actualScale), 40, 49, this.meterHeight - 1, actualScale);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        /** Draw measurement lines */
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 49 * 2, this.meterWidth, this.meterHeight);
    }

    //TODO update and docs
    protected void drawMeter(int x, int y, float scale, FluidStack liquidStack)
    {
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        /** Draw the background meter. */
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 0, meterWidth, meterHeight);

        /** Draw liquid/gas inside */
        if (liquidStack != null)
        {
            this.drawFluid(this.containerWidth + x, this.containerHeight + y, -10, 1, 12, (int) ((meterHeight - 1) * scale), liquidStack);
        }

        /** Draw measurement lines */
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 49 * 2, meterWidth, meterHeight);
    }

    public void drawTooltip(int x, int y, String... tooltips)
    {
        drawTooltip(x, y, Lists.newArrayList(tooltips));
    }

    //TODO update and docs
    public void drawTooltip(int x, int y, List<String> tooltips)
    {
        drawHoveringText(tooltips, x, y, fontRendererObj);
    }

    protected void drawFluid(int j, int k, int line, int col, int width, int squaled, FluidStack liquid)
    {
        squaled -= 1;

        if (liquid == null)
        {
            return;
        }

        int start = 0;

        IIcon liquidIcon = null;
        Fluid fluid = liquid.getFluid();

        if (fluid != null && fluid.getStillIcon() != null)
        {
            liquidIcon = fluid.getStillIcon();
        }

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(FMLClientHandler.instance().getClient().renderEngine.getResourceLocation(fluid.getSpriteNumber()));

        if (liquidIcon != null)
        {
            while (true)
            {
                int x;

                if (squaled > 16)
                {
                    x = 16;
                    squaled -= 16;
                }
                else
                {
                    x = squaled;
                    squaled = 0;
                }

                this.drawTexturedModelRectFromIcon(j + col, k + line + 58 - x - start, liquidIcon, width, 16 - (16 - x));
                start = start + 16;

                if (x == 0 || squaled == 0)
                {
                    break;
                }
            }
        }
    }

    /**
     * Renders a furnace style arrow for displaying cook time
     *
     * @param x           - position width
     * @param y           - position height
     * @param cookTime    - current time
     * @param maxCookTime - max time
     */
    protected void renderFurnaceCookArrow(int x, int y, int cookTime, int maxCookTime)
    {
        //Fix for tiles that do not cap value
        cookTime = Math.min(cookTime, maxCookTime);

        //Draw background
        drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 18, 0, 22, 15);

        //Only draw arrow if time is above zero
        if (cookTime > 0)
        {
            //Calculate scale
            float p = cookTime / (maxCookTime + 0.0f);

            //Draw arrow
            drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 18, 15, (int) Math.floor(22 * p), 15);
        }
    }
}
