package com.builtbroken.atomic.content.boiler;

import com.builtbroken.atomic.Atomic;
import com.builtbroken.atomic.Settings;
import com.builtbroken.mc.api.tile.IGuiTile;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.prefab.inventory.ExternalInventory;
import com.builtbroken.mc.prefab.tile.TileModuleMachine;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/** Nuclear boiler TileEntity */

public class TileNuclearBoiler extends TileModuleMachine<ExternalInventory> implements ISidedInventory, IPacketIDReceiver, IFluidHandler, IGuiTile
{
    public final static int DIAN = 50000;
    public final int SHI_JIAN = 20 * 15;
    //@Synced
    public final FluidTank waterTank = new FluidTank(Atomic.FLUIDSTACK_WATER.copy(), FluidContainerRegistry.BUCKET_VOLUME * 5);
    //@Synced
    public final FluidTank gasTank = new FluidTank(Atomic.FLUIDSTACK_URANIUM_HEXAFLOURIDE.copy(), FluidContainerRegistry.BUCKET_VOLUME * 5);
    // How many ticks has this item been extracting for?
    //@Synced
    public int timer = 0;
    public float rotation = 0;

    public TileNuclearBoiler()
    {
        super("nuclearBoiler", Material.iron);
    }

    @Override
    protected ExternalInventory createInventory()
    {
        return new ExternalInventory(this, 4);
    }

    @Override
    public int getEnergyBufferSize()
    {
        return DIAN * 2;
    }

    @Override
    public void update()
    {
        super.update();

        if (timer > 0)
        {
            rotation += 0.1f;
        }

        if (!this.worldObj.isRemote)
        {
            // Put water as liquid
            if (getStackInSlot(1) != null)
            {
                if (FluidContainerRegistry.isFilledContainer(getStackInSlot(1)))
                {
                    FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(getStackInSlot(1));

                    if (liquid.isFluidEqual(Atomic.FLUIDSTACK_WATER))
                    {
                        if (this.fill(ForgeDirection.UNKNOWN, liquid, false) > 0)
                        {
                            ItemStack resultingContainer = getStackInSlot(1).getItem().getContainerItem(getStackInSlot(1));

                            if (resultingContainer == null && getStackInSlot(1).stackSize > 1)
                            {
                                getStackInSlot(1).stackSize--;
                            }
                            else
                            {
                                setInventorySlotContents(1, resultingContainer);
                            }

                            this.waterTank.fill(liquid, true);
                        }
                    }
                }
            }

            if (this.nengYong())
            {
                this.getEnergyBuffer(ForgeDirection.UNKNOWN).addEmeryFromItem(getStackInSlot(0));

                if (this.getEnergyBuffer(ForgeDirection.UNKNOWN).removeEnergyFromStorage(DIAN, false) >= TileNuclearBoiler.DIAN)
                {
                    if (this.timer == 0)
                    {
                        this.timer = SHI_JIAN;
                    }

                    if (this.timer > 0)
                    {
                        this.timer--;

                        if (this.timer < 1)
                        {
                            this.yong();
                            this.timer = 0;
                        }
                    }
                    else
                    {
                        this.timer = 0;
                    }

                    this.getEnergyBuffer(ForgeDirection.UNKNOWN).removeEnergyFromStorage(DIAN, true);
                }
            }
            else
            {
                this.timer = 0;
            }

            if (this.ticks % 10 == 0)
            {
                //this.sendDescPack();
            }
        }
    }

    // Check all conditions and see if we can start smelting
    public boolean nengYong()
    {
        if (this.waterTank.getFluid() != null)
        {
            if (this.waterTank.getFluid().amount >= FluidContainerRegistry.BUCKET_VOLUME)
            {
                if (getStackInSlot(3) != null)
                {
                    if (Atomic.itemYellowCake == getStackInSlot(3).getItem() || Atomic.isItemStackUraniumOre(getStackInSlot(3)))
                    {
                        if (Atomic.getFluidAmount(this.gasTank.getFluid()) < this.gasTank.getCapacity())
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /** Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack. */
    public void yong()
    {
        if (this.nengYong())
        {
            this.waterTank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
            FluidStack liquid = Atomic.FLUIDSTACK_URANIUM_HEXAFLOURIDE.copy();
            liquid.amount = Settings.uraniumHexaflourideRatio * 2;
            this.gasTank.fill(liquid, true);
            this.decrStackSize(3, 1);
        }
    }

    /** Reads a tile entity from NBT. */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.timer = nbt.getInteger("shiJian");

        NBTTagCompound waterCompound = nbt.getCompoundTag("water");
        this.waterTank.setFluid(FluidStack.loadFluidStackFromNBT(waterCompound));

        NBTTagCompound gasCompound = nbt.getCompoundTag("gas");
        this.gasTank.setFluid(FluidStack.loadFluidStackFromNBT(gasCompound));
    }

    /** Writes a tile entity to NBT. */
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("shiJian", this.timer);

        if (this.waterTank.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.waterTank.getFluid().writeToNBT(compound);
            nbt.setTag("water", compound);
        }

        if (this.gasTank.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.gasTank.getFluid().writeToNBT(compound);
            nbt.setTag("gas", compound);
        }
    }

    /** Tank Methods */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (Atomic.FLUIDSTACK_WATER.isFluidEqual(resource))
        {
            return this.waterTank.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (Atomic.FLUIDSTACK_URANIUM_HEXAFLOURIDE.isFluidEqual(resource))
        {
            return this.gasTank.drain(resource.amount, doDrain);
        }

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.gasTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return Atomic.FLUIDSTACK_WATER.getFluidID() == fluid.getID();
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return Atomic.FLUIDSTACK_URANIUM_HEXAFLOURIDE.getFluidID() == fluid.getID();
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[]
                {this.waterTank.getInfo(), this.gasTank.getInfo()};
    }

    /** Inventory */
    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        if (slotID == 1)
        {
            return Atomic.isItemStackWaterCell(itemStack);
        }
        else if (slotID == 3)
        {
            return itemStack.getItem() == Atomic.itemYellowCake;
        }

        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return side == 0 ? new int[]
                {2} : new int[]
                {1, 3};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemStack, int side)
    {
        return this.isItemValidForSlot(slotID, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int j)
    {
        return slotID == 2;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player)
    {
        return new ContainerNuclearBoiler(player, this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return null;
    }
}
