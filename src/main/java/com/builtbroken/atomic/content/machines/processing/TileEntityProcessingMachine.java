package com.builtbroken.atomic.content.machines.processing;

import com.builtbroken.atomic.content.machines.TileEntityPowerInvMachine;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipe;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipeList;
import com.builtbroken.atomic.lib.power.PowerSystem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;
import java.util.function.Function;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public abstract class TileEntityProcessingMachine extends TileEntityPowerInvMachine
{
    boolean processing = false;
    public int processTimer = 0;

    ForgeDirection _facingDirectionCache;

    float _processingAnimationRotationPrev = 0;
    float _processingAnimationRotation = 0;

    @Override
    public void update(int ticks)
    {
        super.update(ticks);
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
        ProcessingRecipe recipe = getRecipeList().getMatchingRecipe(this);
        if (recipe != null)
        {
            if (recipe.applyRecipe(this))
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
        return getRecipeList().getMatchingRecipe(this) != null; //TODO store recipe
    }

    /**
     * Gets the list of recipes supported by this machine
     *
     * @return
     */
    protected abstract ProcessingRecipeList getRecipeList();

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

    protected void drainBattery(int slot)
    {
        ItemStack itemStack = getStackInSlot(slot);
        int power = PowerSystem.getEnergyStored(itemStack);
        if (power > 0)
        {
            power = PowerSystem.removePower(itemStack, power, false);
            int added = addEnergy(power, true);
            PowerSystem.removePower(itemStack, added, true);
            setInventorySlotContents(slot, itemStack);
        }
    }

    public boolean hasSpaceInOutput(ItemStack insertStack, int slot)
    {
        ItemStack stackInSlot = getStackInSlot(slot);
        if (stackInSlot == null)
        {
            return true;
        }
        else if (stackInSlot.getItem() == insertStack.getItem() && stackInSlot.getItemDamage() == insertStack.getItemDamage())
        {
            return getInventoryStackLimit() - stackInSlot.stackSize >= insertStack.stackSize;
        }
        return false;
    }

    public void addToOutput(ItemStack insertStack, int slot)
    {
        ItemStack stackInSlot = getStackInSlot(slot);
        if (stackInSlot == null)
        {
            setInventorySlotContents(slot, insertStack);
        }
        else if (stackInSlot.getItem() == insertStack.getItem() && stackInSlot.getItemDamage() == insertStack.getItemDamage())
        {
            stackInSlot.stackSize += insertStack.stackSize;
            stackInSlot.stackSize = Math.min(stackInSlot.stackSize, stackInSlot.getMaxStackSize());
            stackInSlot.stackSize = Math.min(stackInSlot.stackSize, getInventoryStackLimit());
        }
    }

    //-----------------------------------------------
    //--------Fluid Handling ------------------------
    //-----------------------------------------------


    /**
     * Pulls fluids from container and insert into tank
     */
    protected void fillTank(final int slot, final IFluidTank inputTank)
    {
        final ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof IFluidContainerItem)
            {
                IFluidContainerItem fluidContainerItem = (IFluidContainerItem) itemStack.getItem();

                FluidStack fluidStack = fluidContainerItem.getFluid(itemStack);
                if (fluidStack != null && getRecipeList().isComponent(this, fluidStack.getFluid()))
                {
                    fluidStack = fluidContainerItem.drain(itemStack, inputTank.getCapacity() - inputTank.getFluidAmount(), false);
                    int amount = inputTank.fill(fluidStack, true);
                    fluidContainerItem.drain(itemStack, amount, true);
                    setInventorySlotContents(slot, itemStack);
                }
            }
            else if (FluidContainerRegistry.isFilledContainer(itemStack))
            {
                FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                if (stack != null && getRecipeList().isComponent(this, stack.getFluid()))
                {
                    inputTank.fill(stack, true);
                    decrStackSize(slot, 1);

                    ItemStack container = itemStack.getItem().getContainerItem(itemStack);
                    if (container != null)
                    {
                        if (getStackInSlot(slot) == null)
                        {
                            setInventorySlotContents(slot, container);
                        }
                        else
                        {
                            //TODO add fluid container output slot
                            EntityItem item = new EntityItem(worldObj);
                            item.setPosition(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                            item.setEntityItemStack(container);
                            worldObj.spawnEntityInWorld(item);
                        }
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
        final ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null && outputTank.getFluid() != null)
        {
            if (itemStack.getItem() instanceof IFluidContainerItem)
            {
                //Copy stack (fix for containers that can stack when empty)
                final ItemStack fluidContainer = itemStack.copy();
                fluidContainer.stackSize = 1;

                IFluidContainerItem fluidContainerItem = (IFluidContainerItem) fluidContainer.getItem();
                FluidStack fluidStack = fluidContainerItem.getFluid(fluidContainer);
                if (fluidStack == null || fluidStack.getFluid() == outputTank.getFluid().getFluid())
                {
                    int filled = fluidContainerItem.fill(fluidContainer, outputTank.getFluid(), true);
                    outputTank.drain(filled, true);

                    if (itemStack.stackSize == 1)
                    {
                        setInventorySlotContents(slot, fluidContainer);
                    }
                    else
                    {
                        decrStackSize(slot, 1);

                        //TODO add fluid container output slot
                        EntityItem item = new EntityItem(worldObj);
                        item.setPosition(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                        item.setEntityItemStack(fluidContainer);
                        worldObj.spawnEntityInWorld(item);
                    }
                }
            }
            else if (FluidContainerRegistry.isEmptyContainer(itemStack))
            {
                ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(outputTank.getFluid(), itemStack);
                if (filledContainer != null)
                {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(filledContainer);
                    if (fluidStack.getFluid() == outputTank.getFluid().getFluid() && fluidStack.amount <= outputTank.getFluidAmount())
                    {
                        outputTank.drain(fluidStack.amount, true);
                        decrStackSize(slot, 1);

                        if (getStackInSlot(slot) == null)
                        {
                            setInventorySlotContents(slot, filledContainer);
                        }
                        else
                        {
                            //TODO add fluid container output slot
                            EntityItem item = new EntityItem(worldObj);
                            item.setPosition(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                            item.setEntityItemStack(filledContainer);
                            worldObj.spawnEntityInWorld(item);
                        }
                    }
                }
            }
        }
    }

    /**
     * Outputs fluids to connected tiles
     *
     * @param outputTank - tank to drain
     */
    protected void outputFluidToTiles(IFluidTank outputTank, Function<ForgeDirection, Boolean> canUseSideFunction)
    {
        if (outputTank.getFluid() != null)
        {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                if (canUseSideFunction == null || canUseSideFunction.apply(direction))
                {
                    int x = xCoord + direction.offsetX;
                    int y = yCoord + direction.offsetY;
                    int z = zCoord + direction.offsetZ;

                    if (worldObj.blockExists(x, y, z))
                    {
                        TileEntity tile = worldObj.getTileEntity(x, y, z);
                        if (tile instanceof IFluidHandler && outputTank.getFluid() != null && ((IFluidHandler) tile).canFill(direction.getOpposite(), outputTank.getFluid().getFluid()))
                        {
                            int fill = ((IFluidHandler) tile).fill(direction.getOpposite(), outputTank.getFluid(), true);
                            outputTank.drain(fill, true);
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

    //-----------------------------------------------
    //--------Props ---------------------------------
    //-----------------------------------------------


    public ForgeDirection getFacingDirection()
    {
        if (_facingDirectionCache == null)
        {
            _facingDirectionCache = ForgeDirection.getOrientation(getBlockMetadata());
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
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("processingProgress", processTimer);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        processTimer = nbt.getInteger("processingProgress");
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
