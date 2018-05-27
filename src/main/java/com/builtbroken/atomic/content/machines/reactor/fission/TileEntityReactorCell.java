package com.builtbroken.atomic.content.machines.reactor.fission;

import com.builtbroken.atomic.api.item.IFuelRodItem;
import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.api.reactor.IFissionReactor;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.machines.TileEntityInventoryMachine;
import com.builtbroken.atomic.map.MapHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class TileEntityReactorCell extends TileEntityInventoryMachine implements IFissionReactor, IRadiationSource, ISidedInventory
{
    public static final int SLOT_FUEL_ROD = 0;
    public static final int[] ACCESSIBLE_SIDES = new int[]{SLOT_FUEL_ROD};
    /** Client side */
    private boolean _running = false;
    private boolean _renderFuel = false;
    private float _renderFuelLevel = 0f;

    public StructureType structureType;

    @Override
    protected void firstTick()
    {
        super.firstTick();
        updateStructureType();
        MapHandler.RADIATION_MAP.addSource(this);
        MapHandler.THERMAL_MAP.addSource(this);
    }

    //-----------------------------------------------
    //--------Runtime logic -------------------------
    //-----------------------------------------------

    @Override
    public void update(int ticks)
    {
        super.update(ticks);
        if (isServer())
        {
            if (canOperate())
            {
                consumeFuel(ticks);
                if (ticks % 20 == 0)
                {
                    doOperationTick();
                }
            }

            //Every 5 seconds, Check if we need to move rods (works like a hopper)
            if (ticks % 100 == 0 && getFuelRod() != null)
            {
                TileEntity tile = worldObj.getTileEntity(xi(), yi() - 1, zi());
                if (tile instanceof TileEntityReactorCell)
                {
                    //always move lowest rod to bottom of stack (ensures dead rods exit core)
                    if (((TileEntityReactorCell) tile).getFuelRod() != null)
                    {
                        int runTime = getFuelRuntime();
                        int otherRunTime = ((TileEntityReactorCell) tile).getFuelRuntime();

                        if (runTime < otherRunTime)
                        {
                            ItemStack stack = ((TileEntityReactorCell) tile).getFuelRodStack();
                            ((TileEntityReactorCell) tile).setFuelRod(getFuelRodStack());
                            setFuelRod(stack);
                        }
                    }
                    //If not rod in lower core, move cell
                    else
                    {
                        ((TileEntityReactorCell) tile).setFuelRod(getFuelRodStack());
                        setFuelRod(null);
                    }
                }
            }
        }
        else if (_running)
        {
            //TODO run client side effects
        }
    }

    protected void doOperationTick()
    {
        //TODO calculate radioactive material leaking
        //TODO dump radioactive material to area or drains
    }

    protected int getActualHeat(int heat)
    {
        //TODO figure out bonus and negative to heat generation (control rods decrease, reactors nearby increase)
        return heat;
    }

    protected void consumeFuel(int ticks)
    {
        IFuelRodItem fuelRodItem = getFuelRod();
        if (fuelRodItem != null)
        {
            setInventorySlotContents(0, fuelRodItem.onReactorTick(this, getFuelRodStack(), ticks, getFuelRuntime()));
        }
    }

    protected boolean canOperate()
    {
        //TODO check for safety (water, temp, etc)
        //TODO check if can generate neutrons (controls rods can force off)
        //TODO check for redstone disable
        return hasFuel() && getFuelRuntime() > 0;
    }

    @Override
    protected void onSlotStackChanged(ItemStack prev, ItemStack stack, int slot)
    {
        super.onSlotStackChanged(prev, stack, slot);
        if (isServer())
        {
            syncClientNextTick();
            MapHandler.RADIATION_MAP.addSource(this);
            MapHandler.THERMAL_MAP.addSource(this);
        }
    }

    //-----------------------------------------------
    //--------Accessors -----------------------------
    //-----------------------------------------------

    public boolean hasFuel()
    {
        return getFuelRod() != null;
    }

    public int getFuelRuntime()
    {
        IFuelRodItem fuelRod = getFuelRod();
        if (fuelRod != null)
        {
            return fuelRod.getFuelRodRuntime(getFuelRodStack(), this);
        }
        return 0;
    }

    public int getMaxFuelRuntime()
    {
        IFuelRodItem fuelRod = getFuelRod();
        if (fuelRod != null)
        {
            return fuelRod.getMaxFuelRodRuntime(getFuelRodStack(), this);
        }
        return 0;
    }

    public IFuelRodItem getFuelRod()
    {
        ItemStack stack = getFuelRodStack();
        return stack != null && stack.getItem() instanceof IFuelRodItem ? (IFuelRodItem) stack.getItem() : null;
    }

    public void setFuelRod(ItemStack stack)
    {
        setInventorySlotContents(SLOT_FUEL_ROD, stack);
    }

    @Override
    public ItemStack getFuelRodStack()
    {
        return getStackInSlot(SLOT_FUEL_ROD);
    }

    @Override
    public int getRadioactiveMaterial()
    {
        IFuelRodItem fuelRod = getFuelRod();
        if (fuelRod != null)
        {
            return fuelRod.getRadioactiveMaterial(getFuelRodStack(), this);
        }
        return 0;
    }

    @Override
    public boolean isRadioactive()
    {
        return !isInvalid() && getRadioactiveMaterial() > 0;
    }

    @Override
    public boolean canGeneratingHeat()
    {
        return !isInvalid() && getHeatGenerated() > 0;
    }

    @Override
    public int getHeatGenerated()
    {
        IFuelRodItem fuelRodItem = getFuelRod();
        if (fuelRodItem != null)
        {
            return getActualHeat(fuelRodItem.getHeatOutput(getFuelRodStack(), this));
        }
        return 0;
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return slot == SLOT_FUEL_ROD && stack.getItem() instanceof IFuelRodItem;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_)
    {
        return ACCESSIBLE_SIDES;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return SLOT_FUEL_ROD == slot;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        if (SLOT_FUEL_ROD == slot)
        {
            if (stack.getItem() instanceof IFuelRodItem)
            {
                return ((IFuelRodItem) stack.getItem()).getFuelRodRuntime(stack, this) <= 0;
            }
            return true;
        }
        return false;
    }

    //-----------------------------------------------
    //--------Rendering props -----------------------
    //-----------------------------------------------

    public boolean shouldRenderFuel()
    {
        return _renderFuel;
    }

    public float getFuelRenderLevel()
    {
        if (isServer())
        {
            return (float) getFuelRuntime() / (float) getMaxFuelRuntime();
        }
        return _renderFuelLevel;
    }

    //-----------------------------------------------
    //--------Network code -------------------------
    //-----------------------------------------------

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeDescPacket(dataList, player);
        dataList.add(hasFuel());
        dataList.add(getFuelRenderLevel());
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readDescPacket(buf, player);
        _renderFuel = buf.readBoolean();
        _renderFuelLevel = buf.readFloat();
    }

    //-----------------------------------------------
    //--------Structure code -------------------------
    //-----------------------------------------------

    public void updateStructureType()
    {
        Block blockAbove = worldObj.getBlock(xCoord, yCoord + 1, zCoord);
        Block blockBelow = worldObj.getBlock(xCoord, yCoord - 1, zCoord);

        if (blockAbove == ASBlocks.blockReactorCell && blockBelow == ASBlocks.blockReactorCell)
        {
            structureType = StructureType.MIDDLE;
        }
        else if (blockBelow == ASBlocks.blockReactorCell)
        {
            structureType = StructureType.TOP;
        }
        else if (blockAbove == ASBlocks.blockReactorCell)
        {
            structureType = StructureType.BOTTOM;
        }
        else
        {
            structureType = StructureType.NORMAL;
        }
    }

    public boolean isTop()
    {
        return structureType == StructureType.TOP;
    }

    public boolean isMiddle()
    {
        return structureType == StructureType.MIDDLE;
    }

    public boolean isBottom()
    {
        return structureType == StructureType.BOTTOM;
    }

    public static enum StructureType
    {
        NORMAL,
        TOP,
        MIDDLE,
        BOTTOM;
    }
}
