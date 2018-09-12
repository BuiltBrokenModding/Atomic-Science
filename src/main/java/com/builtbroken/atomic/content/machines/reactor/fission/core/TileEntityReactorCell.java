package com.builtbroken.atomic.content.machines.reactor.fission.core;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.item.IFuelRodItem;
import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.api.reactor.IFissionReactor;
import com.builtbroken.atomic.client.EffectRefs;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.machines.TileEntityInventoryMachine;
import com.builtbroken.atomic.content.machines.reactor.fission.controller.TileEntityReactorController;
import com.builtbroken.atomic.map.MapHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class TileEntityReactorCell extends TileEntityInventoryMachine implements IFissionReactor, IRadiationSource
{
    public static final int SLOT_FUEL_ROD = 0;
    public static final int[] ACCESSIBLE_SIDES = new int[]{SLOT_FUEL_ROD};
    /** Client side */
    private boolean _running = false;
    private boolean _renderFuel = false;
    private float _renderFuelLevel = 0f;

    public StructureType structureType;

    public boolean enabled = true; ///TODO add a spin up and down time, prevent instant enable/disable of reactors

    @Override
    protected void firstTick()
    {
        super.firstTick();
        updateStructureType();
        MapHandler.RADIATION_MAP.addSource(this);
        MapHandler.THERMAL_MAP.addSource(this);
    }

    @Override
    protected IItemHandlerModifiable createInventory()
    {
        return new ItemStackHandler(1);
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
            final boolean prev_running = _running;

            if (canOperate())
            {
                _running = true;
                consumeFuel(ticks);
                if (ticks % 20 == 0)
                {
                    doOperationTick();
                }
            }
            else
            {
                _running = false;
            }

            if (prev_running != _running || ticks % 20 == 0)
            {
                syncClientNextTick();
            }

            //Every 5 seconds, Check if we need to move rods (works like a hopper)
            if (ticks % 100 == 0 && getFuelRod() != null)
            {
                TileEntity tile = world.getTileEntity(getPos().down());
                if (tile instanceof TileEntityReactorCell)
                {
                    tryToMoveRod((TileEntityReactorCell) tile);
                }
                else if(tile instanceof TileEntityReactorController)
                {
                    tile = world.getTileEntity(getPos().down(2));
                    if (tile instanceof TileEntityReactorCell)
                    {
                        tryToMoveRod((TileEntityReactorCell) tile);
                    }
                }
            }
        }
        else if (_running)
        {
            AtomicScience.sideProxy.spawnParticle(EffectRefs.REACTOR_RUNNING, xi() + 0.5, yi() + 0.5, zi() + 0.5, 0, 0, 0);
        }
    }

    protected void tryToMoveRod(TileEntityReactorCell cell)
    {
        //always move lowest rod to bottom of stack (ensures dead rods exit core)
        if (cell.getFuelRod() != null)
        {
            int runTime = getFuelRuntime();
            int otherRunTime = cell.getFuelRuntime();

            if (runTime < otherRunTime)
            {
                ItemStack stack = cell.getFuelRodStack();
                cell.setFuelRod(getFuelRodStack());
                setFuelRod(stack);
            }
        }
        //If not rod in lower core, move cell
        else
        {
            cell.setFuelRod(getFuelRodStack());
            setFuelRod(null);
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
            getInventory().setStackInSlot(0, fuelRodItem.onReactorTick(this, getFuelRodStack(), ticks, getFuelRuntime()));
        }
    }

    protected boolean canOperate()
    {
        //TODO check for safety (water, temp, etc)
        //TODO check if can generate neutrons (controls rods can force off)
        return enabled && hasFuel() && getFuelRuntime() > 0;
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
        getInventory().setStackInSlot(SLOT_FUEL_ROD, stack);
    }

    @Override
    public ItemStack getFuelRodStack()
    {
        return getInventory().getStackInSlot(SLOT_FUEL_ROD);
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

    //@Override TODO
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return slot == SLOT_FUEL_ROD && stack.getItem() instanceof IFuelRodItem;
    }

    //@Override
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
        dataList.add(_running ||canOperate());
        dataList.add(hasFuel());
        dataList.add(getFuelRenderLevel());
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readDescPacket(buf, player);
        _running = buf.readBoolean();
        _renderFuel = buf.readBoolean();
        _renderFuelLevel = buf.readFloat();
    }

    //-----------------------------------------------
    //--------Structure code -------------------------
    //-----------------------------------------------

    public void updateStructureType()
    {
        IBlockState blockAbove = world.getBlockState(getPos().up());
        IBlockState blockBelow = world.getBlockState(getPos().down());

        if (canConnect(blockAbove) && canConnect(blockBelow))
        {
            structureType = StructureType.MIDDLE;
        }
        else if (canConnect(blockBelow))
        {
            structureType = StructureType.TOP;
        }
        else if (canConnect(blockAbove))
        {
            structureType = StructureType.BOTTOM;
        }
        else
        {
            structureType = StructureType.NORMAL;
        }
    }

    private boolean canConnect(IBlockState block)
    {
        return block == ASBlocks.blockReactorCell || block == ASBlocks.blockReactorController;
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
