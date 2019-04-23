package com.builtbroken.atomic.content.machines.processing;

import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.items.wrench.WrenchMode;
import com.builtbroken.atomic.content.machines.TileEntityPowerInvMachine;
import com.builtbroken.atomic.content.recipes.ProcessingRecipeList;
import com.builtbroken.atomic.content.recipes.RecipeProcessing;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;
import java.util.function.Function;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public abstract class TileEntityProcessingMachine<I extends IItemHandlerModifiable, H extends TileEntityProcessingMachine, R extends RecipeProcessing<H>> extends TileEntityPowerInvMachine<I>
{

    public static final String NBT_PROCESSING_RPOGRESS = "processingProgress";

    boolean processing = false;
    public int processTimer = 0;

    EnumFacing _facingDirectionCache;

    float _processingAnimationRotationPrev = 0;
    float _processingAnimationRotation = 0;

    @Override
    public void update(int ticks, boolean isClient)
    {
        super.update(ticks, isClient);

        //Check if has energy in order to run
        if (checkEnergyExtract())
        {
            if (isServer())
            {
                preProcess(ticks);
                process(ticks);
                postProcess(ticks);
            }
            else if (processTimer > 0)
            {
                doAnimation(ticks);
                doEffects(ticks);
            }
        }
    }

    protected void doAnimation(int ticks)
    {
        _processingAnimationRotation += 5f; //TODO move to val
        if (_processingAnimationRotation > 360)
        {
            _processingAnimationRotation -= 360;
            _processingAnimationRotationPrev -= 360;
        }
    }

    protected void doEffects(int ticks)
    {

    }

    public void onWrench(WrenchMode type, WrenchColor color, EnumFacing side, EntityPlayer player)
    {

    }

    //-----------------------------------------------
    //--------Recipe Handling -----------------------
    //-----------------------------------------------

    /**
     * Called before recipe/process is run
     * <p>
     * Good chance to input fluids and items
     *
     * @param ticks - time in ticks since alive
     */
    protected void preProcess(int ticks)
    {

    }

    /**
     * Called to do the process checks
     *
     * @param ticks - time in ticks since alive
     */
    protected void process(int ticks)
    {
        if (processing)
        {
            extractEnergy();
            if (processTimer++ >= getProcessingTime())
            {
                processTimer = 0;
                doProcess();
                checkRecipe();
            }
        }
        else if (ticks % 20 == 0)
        {
            checkRecipe();
        }
    }

    /**
     * Called when we finish a recipe
     */
    protected void onProcessed()
    {

    }

    /**
     * How long each process cycle takes before running
     */
    protected abstract int getProcessingTime();

    /**
     * Called to do the process
     * <p>
     * E.g. consume items and resources
     */
    protected void doProcess()
    {
        R recipe = (R) getRecipeList().getMatchingRecipe((H) this);
        if (recipe != null)
        {
            if (recipe.applyRecipe((H) this))
            {
                onProcessed();
            }
        }
    }

    /**
     * Called to check if the recipe works
     * <p>
     * Sets {@link #processing} to true to
     * avoid checking recipe every tick
     */
    protected void checkRecipe()
    {
        processing = canProcess();
        if (!processing)
        {
            processTimer = 0;
        }
        //Set to 1 for client sync
        else if (processTimer == 0)
        {
            processTimer = 1;
        }
        syncClientNextTick();
    }

    /**
     * Checks if the process and recipe work
     *
     * @return true if machine has a valid recipe and can function
     */
    protected boolean canProcess()
    {
        return getRecipeList().getMatchingRecipe((H) this) != null; //TODO store recipe
    }

    /**
     * Gets the list of recipes supported by this machine
     *
     * @return
     */
    protected abstract ProcessingRecipeList<H, R> getRecipeList();

    /**
     * Called after the process has run
     * <p>
     * Good chance to output fluids and items
     *
     * @param ticks
     */
    protected void postProcess(int ticks)
    {

    }

    //-----------------------------------------------
    //--------Inventory handling ---------------------------
    //-----------------------------------------------

    public boolean hasSpaceInOutput(ItemStack insertStack, int slot)
    {
        return getInventory().insertItem(slot, insertStack, true).isEmpty();
    }

    public void addToOutput(ItemStack insertStack, int slot)
    {
        getInventory().insertItem(slot, insertStack, false);
    }

    //-----------------------------------------------
    //--------Fluid Handling ------------------------
    //-----------------------------------------------

    public boolean containsFluid(final int slot)
    {
        return getFluid(slot) != null;
    }

    public boolean containsFluid(final int slot, Fluid fluid)
    {
        FluidStack fluidStack = getFluid(slot);
        if (fluidStack != null)
        {
            return fluidStack.getFluid() == fluid;
        }
        return false;
    }

    public boolean isInputFluid(final int slot)
    {
        return isInputFluid(getInventory().getStackInSlot(slot));
    }

    public boolean isInputFluid(ItemStack stack)
    {
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack != null)
        {
            return getRecipeList().isComponent((H) this, fluidStack.getFluid());
        }
        return false;
    }

    public FluidStack getFluid(final int slot)
    {
        return getFluid(getInventory().getStackInSlot(slot));
    }

    public FluidStack getFluid(ItemStack itemStack)
    {
        if (!itemStack.isEmpty() && itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
        {
            IFluidHandler handler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            if (handler != null)
            {
                return handler.drain(Integer.MAX_VALUE, false);
            }
        }
        return null;
    }

    public boolean isEmptyFluidContainer(final int slot)
    {
        return isEmptyFluidContainer(getInventory().getStackInSlot(slot));
    }

    public boolean isEmptyFluidContainer(ItemStack itemStack)
    {
        if (itemStack != null && !itemStack.isEmpty())
        {
            final IFluidHandler handler = FluidUtil.getFluidHandler(itemStack);
            if (handler != null)
            {
                final FluidStack fluidStack = handler.drain(1, false);
                return fluidStack == null || fluidStack.amount <= 0;
            }
            return itemStack.getItem() == Items.BUCKET;
        }
        return false;
    }

    /**
     * Pulls fluids from container and insert into tank
     */
    protected void fillTank(final int slot, final IFluidTank inputTank)
    {
        final ItemStack itemStack = getInventory().getStackInSlot(slot);
        if (!itemStack.isEmpty())
        {
            if (itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
            {
                IFluidHandler handler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (handler != null)
                {
                    //Get fluid and check if its part of the recipe
                    FluidStack fluidStack = handler.drain(inputTank.getCapacity() - inputTank.getFluidAmount(), false);
                    if (fluidStack != null && getRecipeList().isComponent((H) this, fluidStack.getFluid()))
                    {
                        //Fill
                        int amount = inputTank.fill(fluidStack, true);

                        //Drain based on fill
                        handler.drain(amount, true);

                        //Update inventory
                        getInventory().setStackInSlot(slot, handler instanceof IFluidHandlerItem ? ((IFluidHandlerItem) handler).getContainer() : itemStack);
                    }
                }
            }
        }
    }


    /**
     * Outputs fluids to container in slot
     *
     * @param slot       - slot with container
     * @param outputTank - tank to drain
     */

    protected void outputFluids(final int slot, final IFluidTank outputTank)
    {
        final ItemStack itemStack = getInventory().getStackInSlot(slot);
        if (!itemStack.isEmpty() && outputTank.getFluid() != null)
        {
            if (itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
            {
                IFluidHandler handler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (handler != null)
                {
                    int fill = handler.fill(outputTank.getFluid(), true);
                    outputTank.drain(fill, true);

                    //Update inventory
                    getInventory().setStackInSlot(slot, handler instanceof IFluidHandlerItem ? ((IFluidHandlerItem) handler).getContainer() : itemStack);
                }
            }
        }
    }

    /**
     * Outputs fluids to connected tiles
     *
     * @param outputTank - tank to drain
     */
    protected void outputFluidToTiles(IFluidTank outputTank, Function<EnumFacing, Boolean> canUseSideFunction)
    {
        if (outputTank.getFluid() != null)
        {
            for (EnumFacing direction : EnumFacing.VALUES)
            {
                if (canUseSideFunction == null || canUseSideFunction.apply(direction))
                {
                    BlockPos pos = getPos().add(direction.getDirectionVec());

                    if (world.isBlockLoaded(pos))
                    {
                        TileEntity tile = world.getTileEntity(pos);
                        if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction))
                        {
                            IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction);
                            if (handler != null)
                            {
                                int fill = handler.fill(outputTank.getFluid(), true);
                                outputTank.drain(fill, true);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if the tank has fluids
     *
     * @param tank   - tank to check
     * @param fluid  - fluid to match
     * @param amount - fluid volume to match >=
     * @return true if enough fluid exists
     */
    public boolean hasInputFluid(IFluidTank tank, Fluid fluid, int amount)
    {
        FluidStack inputFluidStack = tank.getFluid();
        return inputFluidStack != null
                && inputFluidStack.getFluid() == fluid
                && inputFluidStack.amount >= amount;
    }

    /**
     * Checks if there is enough fluid to output
     *
     * @param tank   - tank to drain
     * @param fluid  - fluid to drain
     * @param amount - amount to drain
     * @return true if enough fluid
     */
    public boolean canOutputFluid(IFluidTank tank, Fluid fluid, int amount)
    {
        if (fluid != null && amount > 0)
        {
            if (tank.getFluid() != null)
            {
                //Space left in tank
                final int room = tank.getCapacity() - tank.getFluid().amount;
                return room >= amount && fluid == tank.getFluid().getFluid();
            }
            else
            {
                return tank.getCapacity() >= amount;
            }
        }
        return false;
    }

    public boolean tankMatch(IFluidTank tank, FluidStack fluidStack)
    {
        if (fluidStack != null)
        {
            return tank.getFluid() != null && tank.getFluid().getFluid() == fluidStack.getFluid();
        }
        return false;
    }

    public boolean tankMatch(IFluidTank tank, Fluid fluid)
    {
        return tank.getFluid() != null && tank.getFluid().getFluid() == fluid;
    }

    //-----------------------------------------------
    //--------Props ---------------------------------
    //-----------------------------------------------


    public EnumFacing getFacingDirection()
    {
        if (_facingDirectionCache == null)
        {
            _facingDirectionCache = EnumFacing.byIndex(getBlockMetadata());
        }
        return _facingDirectionCache;
    }

    @Override
    public void markDirty()
    {
        if (isServer())
        {
            _facingDirectionCache = null;
        }
    }

    @SideOnly(Side.CLIENT)
    public float rotate(float delta)
    {
        _processingAnimationRotationPrev = _processingAnimationRotation + (_processingAnimationRotation - _processingAnimationRotationPrev) * delta;
        return _processingAnimationRotationPrev;
    }


    //-----------------------------------------------
    //--------GUI Handler ---------------------------
    //-----------------------------------------------

    @Override
    protected void writeGuiPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeGuiPacket(dataList, player);
        dataList.add(processTimer);
    }

    @Override
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readGuiPacket(buf, player);
        processTimer = buf.readInt();
    }

    //-----------------------------------------------
    //--------Save/Load -----------------------------
    //-----------------------------------------------

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger(NBT_PROCESSING_RPOGRESS, processTimer);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        processTimer = nbt.getInteger(NBT_PROCESSING_RPOGRESS);
    }

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeGuiPacket(dataList, player);
        dataList.add(processTimer);
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readGuiPacket(buf, player);
        processTimer = buf.readInt();
    }
}
