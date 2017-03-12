package resonantinduction.atomic.particle.quantum;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import resonant.lib.gui.GuiContainerBase;
import resonantinduction.atomic.Atomic;
import resonantinduction.core.Reference;
import universalelectricity.api.energy.UnitDisplay.Unit;

public class GuiQuantumAssembler extends GuiContainerBase
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.DOMAIN, Atomic.GUI_TEXTURE_DIRECTORY + "gui_atomic_assembler.png");

    private TileQuantumAssembler tileEntity;

    private int containerWidth;
    private int containerHeight;

    public GuiQuantumAssembler(InventoryPlayer par1InventoryPlayer, TileQuantumAssembler tileEntity)
    {
        super(new ContainerQuantumAssembler(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
        this.ySize = 230;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(this.tileEntity.getInvName(), 65 - this.tileEntity.getInvName().length(), 6, 4210752);
        String displayText = "";

        if (this.tileEntity.time() > 0)
        {
            displayText = "Process: " + (int) (100 - ((float) this.tileEntity.time() / (float) this.tileEntity.MAX_TIME()) * 100) + "%";
        }
        else if (this.tileEntity.canProcess())
        {
            displayText = "Ready";
        }
        else
        {
            displayText = "Idle";
        }

        this.fontRenderer.drawString(displayText, 9, this.ySize - 106, 4210752);
        this.renderUniversalDisplay(100, this.ySize - 94, this.tileEntity.getVoltageInput(null), mouseX, mouseY, Unit.VOLTAGE);
        this.renderUniversalDisplay(8, this.ySize - 95, tileEntity.MAX_TIME(), mouseX, mouseY, Unit.WATT);
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
    }
}