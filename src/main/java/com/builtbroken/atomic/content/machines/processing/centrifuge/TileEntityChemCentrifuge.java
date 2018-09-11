package com.builtbroken.atomic.content.machines.processing.centrifuge;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.client.EffectRefs;
import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.items.wrench.WrenchMode;
import com.builtbroken.atomic.content.machines.processing.ProcessorRecipeHandler;
import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;
import com.builtbroken.atomic.content.machines.processing.centrifuge.gui.ContainerChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.centrifuge.gui.GuiChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipeList;
import com.builtbroken.atomic.lib.SideSettings;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.client.PacketSpawnParticle;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.*;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/23/2018.
 */
public class TileEntityChemCentrifuge extends TileEntityProcessingMachine implements IFluidHandler, IGuiTile, ISidedInventory
{
    public static final int SLOT_FLUID_INPUT = 0;
    public static final int SLOT_ITEM_OUTPUT = 1;
    public static final int SLOT_BATTERY = 2;
    public static final int SLOT_FLUID_OUTPUT = 3;
    public static final int INVENTORY_SIZE = 4;

    public static final int[] ACCESSIBLE_SIDES = new int[]{SLOT_ITEM_OUTPUT};

    public static int PROCESSING_TIME = 100;
    public static int ENERGY_PER_TICK = 100;

    private final FluidTank inputTank;
    private final FluidTank outputTank;

    private final SideSettings inputTankSideSettings = new SideSettings(true);
    private final SideSettings outputTankSideSettings = new SideSettings(false);

    public TileEntityChemCentrifuge()
    {
        inputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
    }

    @Override
    protected void onProcessed()
    {
        if (isServer())
        {
            PacketSpawnParticle packetSpawnParticle = new PacketSpawnParticle(worldObj.provider.dimensionId,
                    xi() + 0.5, yi() + 0.5, zi() + 0.5,
                    0, 0, 0,
                    EffectRefs.CENTRIFUGE_COMPLETE);
            PacketSystem.INSTANCE.sendToAllAround(packetSpawnParticle, worldObj, this, 30);
        }
    }

    @Override
    protected void doEffects(int ticks)
    {
        if (worldObj.rand.nextFloat() > 0.4)
        {
            AtomicScience.sideProxy.spawnParticle(EffectRefs.CENTRIFUGE_RUNNING, xi() + 0.5, yi() + 0.5, zi() + 0.5, 0, 0, 0);
        }
    }

    @Override
    protected void preProcess(int ticks)
    {
        fillTank(SLOT_FLUID_INPUT, getInputTank());
        drainBattery(SLOT_BATTERY);
    }

    @Override
    protected int getProcessingTime()
    {
        return PROCESSING_TIME;
    }

    @Override
    protected void postProcess(int ticks)
    {
        outputFluids(SLOT_FLUID_OUTPUT, getOutputTank());
        outputFluidToTiles(getOutputTank(), f -> outputTankSideSettings.get(f));
    }

    @Override
    protected ProcessingRecipeList getRecipeList()
    {
        return ProcessorRecipeHandler.INSTANCE.chemCentrifugeProcessingRecipe;
    }

    @Override
    public void onWrench(WrenchMode type, WrenchColor color, EnumFacing side, EntityPlayer player)
    {
        if (type == WrenchMode.FLUID && side != EnumFacing.UNKNOWN)
        {
            if (color == WrenchColor.GREEN)
            {
                outputTankSideSettings.toggle(side);
                player.addChatComponentMessage(new ChatComponentText(outputTankSideSettings.get(side) ? "Green tank set to output on side" : "Green tank set to ignore side"));
            }
            else if (color == WrenchColor.BLUE)
            {
                inputTankSideSettings.toggle(side);
                player.addChatComponentMessage(new ChatComponentText(inputTankSideSettings.get(side) ? "Blue tank set to input on side" : "Blue tank set to ignore side"));
            }
        }
    }

    //-----------------------------------------------
    //--------Fluid Tank Handling -------------------
    //-----------------------------------------------

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        if (resource != null && canFill(from, resource.getFluid()))
        {
            Fluid fluid = getInputTank().getFluid() != null ? getInputTank().getFluid().getFluid() : null;
            int amount = getInputTank().getFluidAmount();

            int fill = getInputTank().fill(resource, doFill);

            if (doFill && getInputTank().getFluid() != null && (fluid != getInputTank().getFluid().getFluid()) || getInputTank().getFluidAmount() != amount)
            {
                checkRecipe();
            }

            return fill;
        }
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        if (outputTankSideSettings.get(from) && getOutputTank().getFluid() != null && resource.getFluid() == getOutputTank().getFluid().getFluid())
        {
            return getOutputTank().drain(resource.amount, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return outputTankSideSettings.get(from) ? getOutputTank().drain(maxDrain, doDrain) : null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return inputTankSideSettings.get(from) && getRecipeList().isComponent(this, fluid);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return fluid == null || getOutputTank().getFluid() != null && getOutputTank().getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[]{getInputTank().getInfo(), getOutputTank().getInfo()};
    }

    public FluidTank getInputTank()
    {
        return inputTank;
    }

    public FluidTank getOutputTank()
    {
        return outputTank;
    }

    //-----------------------------------------------
    //--------Props ---------------------------------
    //-----------------------------------------------

    @Override
    public int getEnergyUsage()
    {
        return ENERGY_PER_TICK;
    }

    //-----------------------------------------------
    //--------GUI Handler ---------------------------
    //-----------------------------------------------

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player)
    {
        return new ContainerChemCentrifuge(player, this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiChemCentrifuge(player, this);
    }

    @Override
    protected void writeGuiPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeGuiPacket(dataList, player);
        dataList.add(getInputTank());
        dataList.add(getOutputTank());
    }

    @Override
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readGuiPacket(buf, player);
        getInputTank().readFromNBT(ByteBufUtils.readTag(buf));
        getOutputTank().readFromNBT(ByteBufUtils.readTag(buf));
    }

    //-----------------------------------------------
    //--------Save/Load -----------------------------
    //-----------------------------------------------

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setTag("outputTank", getOutputTank().writeToNBT(new NBTTagCompound()));
        nbt.setTag("inputTank", getInputTank().writeToNBT(new NBTTagCompound()));

        nbt.setTag("outputTankSides", outputTankSideSettings.save(new NBTTagCompound()));
        nbt.setTag("inputTankSides", inputTankSideSettings.save(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        getOutputTank().readFromNBT(nbt.getCompoundTag("outputTank"));
        getInputTank().readFromNBT(nbt.getCompoundTag("inputTank"));

        outputTankSideSettings.load(nbt.getCompoundTag("outputTankSides"));
        inputTankSideSettings.load(nbt.getCompoundTag("inputTankSides"));
    }

    //-----------------------------------------------
    //--------Inventory Code ------------------------
    //-----------------------------------------------

    @Override
    public int getSizeInventory()
    {
        return INVENTORY_SIZE;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return ACCESSIBLE_SIDES;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        return slot == SLOT_ITEM_OUTPUT;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if (slot == SLOT_FLUID_INPUT)
        {
            return isInputFluid(stack);
        }
        else if (slot == SLOT_FLUID_OUTPUT)
        {
            return isEmptyFluidContainer(stack);
        }
        return false;
    }
}
