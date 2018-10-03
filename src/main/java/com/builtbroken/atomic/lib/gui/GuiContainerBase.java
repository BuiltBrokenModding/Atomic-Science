package com.builtbroken.atomic.lib.gui;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.lib.gui.tip.ISlotToolTip;
import com.builtbroken.atomic.lib.gui.tip.ToolTip;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiContainerBase<H> extends GuiContainer
{
    public static final String TOOLTIP_TANK = "gui.tank.tooltip";
    public static final ResourceLocation GUI_COMPONENTS = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.GUI_TEXTURE_DIRECTORY + "gui_components.png");
    public static final ResourceLocation GUI_MC_BASE = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.GUI_TEXTURE_DIRECTORY + "mc_base.png");

    public ResourceLocation baseTexture;

    public ToolTip currentToolTip = null;

    protected List<ToolTip> tooltips = new ArrayList();
    protected ArrayList<GuiTextField> fields = new ArrayList();

    protected int meterHeight = 49;
    protected int meterWidth = 14;

    protected int containerWidth;
    protected int containerHeight;

    /** Debug toogle to render text for the ID and inventory ID for a slot */
    public boolean renderSlotDebugIDs = false;

    /** Object that is the host of the GUI */
    protected H host;

    public GuiContainerBase(Container container, H host)
    {
        super(container);
        this.baseTexture = GUI_MC_BASE;
        this.host = host;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.fields.clear();
        this.tooltips.clear();

        inventorySlots.inventorySlots.stream().forEach(s -> {
            if (s instanceof ISlotToolTip)
            {
                ToolTip toolTip = ((ISlotToolTip) s).getToolTip();
                if (toolTip != null)
                {
                    tooltips.add(toolTip);
                }
            }
        });
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
     * Adds a tooltip to the GUI
     *
     * @param text      - text to display
     * @param translate - should the text be translated
     */
    protected void addToolTip(int x, int y, int w, int h, String text, boolean translate)
    {
        addToolTip(new Rectangle(x, y, w, h), text, translate);
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
        addToolTip(new ToolTip(triggerArea, text, translate));
    }

    protected void addToolTip(ToolTip toolTip)
    {
        tooltips.add(toolTip);
    }

    protected void drawString(String str, int x, int y, int color)
    {
        fontRenderer.drawString(str, x, y, color);
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
        drawString(str, x - (fontRenderer.getStringWidth(str) / 2), y, color);
    }

    protected GuiTextField newField(int x, int y, int w, String msg)
    {
        return this.newField(x, y, w, 20, msg);
    }

    protected GuiTextField newField(int x, int y, int w, int h, String msg)
    {
        GuiTextField x_field = new GuiTextField(0, this.fontRenderer, x, y, w, h); //TODO handle id
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
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            for (GuiTextField field : fields)
            {
                field.drawTextBox();
            }
        }

        ///============================================================
        ///===============Render tooltips===============================
        ///============================================================
        GlStateManager.pushMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting(); //GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableDepth(); //GL11.glDisable(GL11.GL_DEPTH_TEST);



        //TODO rework to be object based
        //TODO rework to be attached to components rather than free floating
        for (ToolTip toolTip : this.tooltips)
        {
            if (toolTip.isInArea(mouseX - this.guiLeft, mouseY - this.guiTop))
            {
                this.currentToolTip = toolTip;
                break;
            }
        }

        if (this.currentToolTip != null && this.currentToolTip.shouldShow())
        {
            this.drawTooltip(mouseX, mouseY, currentToolTip.getString().split(";"));
        }

        this.currentToolTip = null;

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        ///============================================================

        if (getSlotUnderMouse() != null && !getSlotUnderMouse().getStack().isEmpty())
        {
            renderToolTip(getSlotUnderMouse().getStack(), mouseX, mouseY);
        }
        GlStateManager.popMatrix();
    }

    @Override
    protected void keyTyped(char c, int id) throws IOException
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
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
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
        drawDefaultBackground();

        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;

        this.mc.renderEngine.bindTexture(this.baseTexture);
        GlStateManager.resetColor();

        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
    }

    /**
     * Draws all slots into the GUI
     */
    protected void drawContainerSlots()
    {
        for (Object object : inventorySlots.inventorySlots)
        {
            drawSlot((Slot) object);
        }
    }

    /**
     * Renders a slot using a standard icon or using {@link ISlotRender}
     *
     * @param slot - slot to render
     */
    protected void drawSlot(Slot slot)
    {
        if (slot instanceof ISlotRender)
        {
            GlStateManager.pushMatrix();
            ((ISlotRender) slot).renderSlotOverlay(this, this.containerWidth + slot.xPos - 1, this.containerHeight + slot.yPos - 1);
            GlStateManager.popMatrix();
        }
        else
        {
            drawSlot(slot.xPos - 1, slot.yPos - 1);
        }

        if (AtomicScience.runningAsDev && renderSlotDebugIDs)
        {
            GlStateManager.pushMatrix();
            this.drawStringCentered("" + slot.getSlotIndex(), guiLeft + slot.xPos + 9, guiTop + slot.yPos + 9, Color.YELLOW);
            this.drawStringCentered("" + slot.slotNumber, guiLeft + slot.xPos + 9, guiTop + slot.yPos + 1, Color.RED);
            GlStateManager.popMatrix();
        }
    }

    //TODO update and docs
    protected void drawSlot(int x, int y)
    {
        GlStateManager.pushMatrix();
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18);
        GlStateManager.popMatrix();
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
            GlStateManager.resetColor();
        }
        else
        {
            GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        }
    }

    //TODO update and docs
    protected void drawElectricity(int x, int y, float scale)
    {
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);
        GlStateManager.resetColor();

        /** Draw background progress bar/ */
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 0, 107, 11);

        if (scale > 0)
        {
            /** Draw white color actual progress. */
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 22, (int) (scale * 107), 11);
        }
    }

    /**
     * Renders fluid tank (background, fluid, and glass meter overlay)
     *
     * @param x    - render position, containerWidth is added
     * @param y    - render position, containerWidth is added
     * @param tank - tank containing fluid
     */
    protected void drawFluidTank(int x, int y, IFluidTank tank)
    {
        drawFluidTank(x, y, tank, null);
    }

    /**
     * Renders fluid tank (background, fluid, and glass meter overlay)
     *
     * @param x    - render position, containerWidth is added
     * @param y    - render position, containerWidth is added
     * @param tank - tank containing fluid
     */
    protected void drawFluidTank(int x, int y, IFluidTank tank, Color edgeColor)
    {
        //Get data
        final float scale = tank.getFluidAmount() / (float) tank.getCapacity();
        final FluidStack fluidStack = tank.getFluid();

        //Bing texture
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);

        //Reset color
        GlStateManager.resetColor();

        //Draw background
        if (edgeColor != null)
        {
            GlStateManager.color(edgeColor.getRed() / 255f, edgeColor.getGreen() / 255f, edgeColor.getBlue() / 255f, edgeColor.getAlpha() / 255f);
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 0, meterWidth, meterHeight);

            GlStateManager.resetColor();
            this.drawTexturedModalRect(this.containerWidth + x + 1, this.containerHeight + y + 1, 41, 1, meterWidth - 2, meterHeight - 2);
        }
        else
        {
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 0, meterWidth, meterHeight);
        }

        //Draw fluid
        if (fluidStack != null)
        {
            this.drawFluid(this.containerWidth + x, this.containerHeight + y, -10, 1, 12, (int) ((meterHeight - 1) * scale), fluidStack);
        }

        //Draw lines
        this.mc.renderEngine.bindTexture(GUI_COMPONENTS);
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 49 * 2, meterWidth, meterHeight);

        //Reset color
        GlStateManager.resetColor();
    }

    public void drawTooltip(int x, int y, String... tooltips)
    {
        drawTooltip(x, y, Lists.newArrayList(tooltips));
    }

    //TODO update and docs
    public void drawTooltip(int x, int y, List<String> tooltips)
    {
        drawHoveringText(tooltips, x, y, fontRenderer);
    }

    protected void drawFluid(int x, int y, int line, int col, int width, int drawSize, FluidStack fluidStack)
    {

        if (fluidStack != null && fluidStack.getFluid() != null)
        {
            drawSize -= 1; //TODO why?

            ResourceLocation fluidIcon = null;
            Fluid fluid = fluidStack.getFluid();

            if (fluid != null)
            {
                if (fluid.getStill(fluidStack) != null)
                {
                    fluidIcon = fluid.getStill(fluidStack);
                }
                else if (fluid.getFlowing(fluidStack) != null)
                {
                    fluidIcon = fluid.getFlowing(fluidStack);
                }
                else
                {
                    fluidIcon = FluidRegistry.WATER.getStill();
                }
            }

            //Get sprite
            TextureAtlasSprite texture = FMLClientHandler.instance().getClient().getTextureMapBlocks().getAtlasSprite(fluidIcon.toString());

            if (texture != null)
            {
                //bind texture
                FMLClientHandler.instance().getClient().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                final int textureSize = 16;
                int start = 0;
                if (fluidIcon != null)
                {
                    int renderY = textureSize;
                    while (renderY != 0 && drawSize != 0)
                    {
                        if (drawSize > textureSize)
                        {
                            renderY = textureSize;
                            drawSize -= textureSize;
                        }
                        else
                        {
                            renderY = drawSize;
                            drawSize = 0;
                        }

                        this.drawTexturedModalRect(x + col, y + line + 58 - renderY - start, texture, width, textureSize - (textureSize - renderY));
                        start = start + textureSize;
                    }
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
