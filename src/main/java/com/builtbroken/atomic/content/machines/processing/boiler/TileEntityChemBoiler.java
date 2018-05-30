package com.builtbroken.atomic.content.machines.processing.boiler;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.client.EffectRefs;
import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.items.wrench.WrenchMode;
import com.builtbroken.atomic.content.machines.processing.ProcessorRecipeHandler;
import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;
import com.builtbroken.atomic.content.machines.processing.boiler.gui.ContainerChemBoiler;
import com.builtbroken.atomic.content.machines.processing.boiler.gui.GuiChemBoiler;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipeList;
import com.builtbroken.atomic.lib.SideSettings;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.lib.network.netty.PacketSystem;
import com.builtbroken.atomic.lib.network.packet.client.PacketSpawnParticle;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
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

    SideSettings wasteTankSideSettings = new SideSettings();
    SideSettings hexTankSideSettings = new SideSettings();
    SideSettings waterTankSideSettings = new SideSettings();

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
        outputFluidToTiles(getWasteTank(), f -> waterTankSideSettings.get(f));
        outputFluidToTiles(getHexTank(), f -> hexTankSideSettings.get(f));
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

    @Override
    public void onWrench(WrenchMode type, WrenchColor color, ForgeDirection side, EntityPlayer player)
    {
        if (type == WrenchMode.FLUID && side != ForgeDirection.UNKNOWN)
        {
            if (color == WrenchColor.GREEN)
            {
                waterTankSideSettings.toggle(side);
                player.addChatComponentMessage(new ChatComponentText(waterTankSideSettings.get(side) ? "Green tank set to output on side" : "Green tank set to ignore side"));
            }
            else if (color == WrenchColor.YELLOW)
            {
                hexTankSideSettings.toggle(side);
                player.addChatComponentMessage(new ChatComponentText(hexTankSideSettings.get(side) ? "Yellow tank set to output on side" : "Yellow tank set to ignore side"));
            }
            else if (color == WrenchColor.BLUE)
            {
                waterTankSideSettings.toggle(side);
                player.addChatComponentMessage(new ChatComponentText(waterTankSideSettings.get(side) ? "Blue tank set to input on side" : "Blue tank set to ignore side"));
            }
        }
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

            if (doFill && getInputTank().getFluid() != null && (tankMatch(getInputTank(), fluid) || getInputTank().getFluidAmount() != amount))
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
        if (tankMatch(getWasteTank(), resource))
        {
            return getWasteTank().drain(resource.amount, doDrain);
        }
        else if (tankMatch(getHexTank(), resource))
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
        return waterTankSideSettings.get(from) && getRecipeList().isComponent(this, fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return fluid == null && wasteTankSideSettings.get(from) && hexTankSideSettings.get(from)
                || wasteTankSideSettings.get(from) && tankMatch(getWasteTank(), fluid)
                || hexTankSideSettings.get(from) && tankMatch(getHexTank(), fluid);
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

        nbt.setTag("wasteTankSides", wasteTankSideSettings.save(new NBTTagCompound()));
        nbt.setTag("hexTankSides", hexTankSideSettings.save(new NBTTagCompound()));
        nbt.setTag("waterTankSides", waterTankSideSettings.save(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        getWasteTank().readFromNBT(nbt.getCompoundTag("wasteTank"));
        getInputTank().readFromNBT(nbt.getCompoundTag("inputTank"));
        getHexTank().readFromNBT(nbt.getCompoundTag("hexTank"));

        wasteTankSideSettings.load(nbt.getCompoundTag("wasteTankSides"));
        hexTankSideSettings.load(nbt.getCompoundTag("hexTankSides"));
        waterTankSideSettings.load(nbt.getCompoundTag("waterTankSides"));
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
