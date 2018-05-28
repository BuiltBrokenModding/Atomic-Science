package com.builtbroken.atomic.content.machines.processing.boiler;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.client.EffectRefs;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.machines.processing.ProcessorRecipeHandler;
import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;
import com.builtbroken.atomic.content.machines.processing.boiler.gui.ContainerChemBoiler;
import com.builtbroken.atomic.content.machines.processing.boiler.gui.GuiChemBoiler;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipeList;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.lib.network.netty.PacketSystem;
import com.builtbroken.atomic.lib.network.packet.client.PacketSpawnParticle;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class TileEntityChemBoiler extends TileEntityProcessingMachine implements IFluidHandler, IGuiTile, ISidedInventory
{
    public static final int SLOT_FLUID_INPUT = 0;
    public static final int SLOT_ITEM_INPUT = 1;
    public static final int SLOT_ITEM_OUTPUT = 2;
    public static final int SLOT_BATTERY = 3;
    public static final int SLOT_WASTE_FLUID = 4;
    public static final int SLOT_HEX_FLUID = 5;
    public static final int INVENTORY_SIZE = 6;

    public static final int[] INPUT_SLOTS = new int[]{SLOT_ITEM_INPUT};
    public static final int[] OUTPUT_SLOTS = new int[]{SLOT_ITEM_OUTPUT};
    public static final int[] ACCESSIBLE_SLOTS = new int[]{SLOT_ITEM_INPUT, SLOT_ITEM_OUTPUT};

    public static int PROCESSING_TIME = 100;
    public static int ENERGY_PER_TICK = 100;

    private final FluidTank inputTank;
    private final FluidTank wasteTank;
    private final FluidTank hexTank;

    boolean[] outputSideWasteTank = new boolean[6]; //TODO configure
    boolean[] outputSideHexTank = new boolean[6]; //TODO configure

    public TileEntityChemBoiler()
    {
        inputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        wasteTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        hexTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
    }

    @Override
    protected void onProcessed()
    {
        if (isServer())
        {
            PacketSpawnParticle packetSpawnParticle = new PacketSpawnParticle(worldObj.provider.dimensionId,
                    xi() + 0.5, yi() + 0.5, zi() + 0.5,
                    0, 0, 0,
                    EffectRefs.BOILER_COMPLETE);
            PacketSystem.INSTANCE.sendToAllAround(packetSpawnParticle, worldObj, this, 30);
        }
    }

    @Override
    protected void doEffects(int ticks)
    {
        if (worldObj.rand.nextFloat() > 0.3)
        {
            AtomicScience.sideProxy.spawnParticle(EffectRefs.BOILER_RUNNING, xi() + 0.5, yi() + 0.5, zi() + 0.5, 0, 0, 0);
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
        outputFluids(SLOT_WASTE_FLUID, getWasteTank());
        outputFluids(SLOT_HEX_FLUID, getHexTank());
        outputFluidToTiles(getWasteTank(), f -> outputSideWasteTank[f.ordinal()]);
        outputFluidToTiles(getHexTank(), f -> outputSideHexTank[f.ordinal()]);
    }

    @Override
    protected ProcessingRecipeList getRecipeList()
    {
        return ProcessorRecipeHandler.INSTANCE.chemBoilerProcessingRecipe;
    }

    @Override
    protected void onSlotStackChanged(ItemStack prev, ItemStack stack, int slot)
    {
        if (isServer() && slot == SLOT_ITEM_INPUT)
        {
            checkRecipe();
        }
        super.onSlotStackChanged(prev, stack, slot);
    }

    //-----------------------------------------------
    //--------Fluid Tank Handling -------------------
    //-----------------------------------------------

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
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
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (getWasteTank().getFluid() != null && resource.getFluid() == getWasteTank().getFluid().getFluid())
        {
            return getWasteTank().drain(resource.amount, doDrain);
        }
        else if (getHexTank().getFluid() != null && resource.getFluid() == getHexTank().getFluid().getFluid())
        {
            return getHexTank().drain(resource.amount, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        FluidStack stack = getWasteTank().drain(maxDrain, doDrain);
        if (stack == null)
        {
            return getHexTank().drain(maxDrain, doDrain);
        }
        return stack;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return fluid == FluidRegistry.WATER || fluid == ASFluids.LIQUID_MINERAL_WASTE.fluid;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return fluid == null
                || getWasteTank().getFluid() != null && getWasteTank().getFluid().getFluid() == fluid
                || getHexTank().getFluid() != null && getHexTank().getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[]{getInputTank().getInfo(), getWasteTank().getInfo(), getHexTank().getInfo()};
    }

    public FluidTank getInputTank()
    {
        return inputTank;
    }

    public FluidTank getWasteTank()
    {
        return wasteTank;
    }

    public FluidTank getHexTank()
    {
        return hexTank;
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
        return new ContainerChemBoiler(player, this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiChemBoiler(player, this);
    }

    @Override
    protected void writeGuiPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeGuiPacket(dataList, player);
        dataList.add(getInputTank());
        dataList.add(getWasteTank());
        dataList.add(getHexTank());
    }

    @Override
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readGuiPacket(buf, player);
        getInputTank().readFromNBT(ByteBufUtils.readTag(buf));
        getWasteTank().readFromNBT(ByteBufUtils.readTag(buf));
        getHexTank().readFromNBT(ByteBufUtils.readTag(buf));
    }

    //-----------------------------------------------
    //--------Save/Load -----------------------------
    //-----------------------------------------------

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setTag("wasteTank", getWasteTank().writeToNBT(new NBTTagCompound()));
        nbt.setTag("inputTank", getInputTank().writeToNBT(new NBTTagCompound()));
        nbt.setTag("hexTank", getHexTank().writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        getWasteTank().readFromNBT(nbt.getCompoundTag("wasteTank"));
        getInputTank().readFromNBT(nbt.getCompoundTag("inputTank"));
        getHexTank().readFromNBT(nbt.getCompoundTag("hexTank"));
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
        return ACCESSIBLE_SLOTS;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return slot == SLOT_ITEM_INPUT;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        return slot == SLOT_ITEM_OUTPUT;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if (slot == SLOT_ITEM_INPUT)
        {
            return getRecipeList().isComponent(this, stack);
        }
        return false;
    }
}
