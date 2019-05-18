package com.builtbroken.atomic.content.machines.reactor.fission.controller;

import com.builtbroken.atomic.content.machines.reactor.fission.core.TileEntityReactorCell;
import com.builtbroken.atomic.content.machines.pipe.reactor.pass.TileEntityRodPipe;
import com.builtbroken.atomic.content.machines.pipe.reactor.inv.TileEntityRodPipeInv;
import com.builtbroken.atomic.content.prefab.TileEntityActive;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Block used to relay data and control to a reactor stack
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
public class TileEntityReactorController extends TileEntityActive
{
    //TODO implement wrapper inventory to allow removing rods from cell above machine

    /** Unique id of the reactor stack, used by display systems */
    public String stackID;
    /** Display name of the reactor stack, used by display systems */
    public String stackName;

    // Array of reactor cells in the connected stack
    private TileEntityReactorCell[] cells; //TODO switch to array list

    // Is the CPU in an error state, can be caused by too many CPUs in a stack
    private boolean inErrorState = false;

    //Trigger to refresh the cells array
    private boolean refreshStack = true;

    //Trigger to enable reactor cores
    private boolean enableReactors = true;

    public ControllerState controllerState = ControllerState.OFF;

    @Override
    protected void update(int ticks, boolean isClient)
    {
        super.update(ticks, isClient);
        if (!isClient)
        {
            if (cells == null || refreshStack || ticks % 20 == 0) //TODO remove tick refresh after testing
            {
                doRefreshStack();
            }

            if (ticks % 3 == 0)
            {
                //Check if we should be running
                final boolean enabled = shouldEnableReactors();

                //Enable/Disable reactors
                TileEntityReactorCell[] cells = getReactorCells();
                if (cells != null)
                {
                    for (TileEntityReactorCell cell : cells)
                    {
                        cell.enabled = enabled;
                    }
                }

                //Store prev state to compare
                final ControllerState prev = controllerState;

                //Update state
                controllerState = enabled ? ControllerState.ON : ControllerState.OFF;

                //send packet next tick if state changed
                if (prev != controllerState)
                {
                    sendDescPacket();
                }
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            TileEntity tile = getTileBelow();
            if (tile != null)
            {
                return tile.hasCapability(capability, facing);
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            TileEntity tile = getTileBelow();
            if (tile != null)
            {
                return tile.getCapability(capability, facing);
            }
        }
        return super.getCapability(capability, facing);
    }

    private final TileEntity getTileBelow()
    {
        return world.getTileEntity(getPos().down());
    }

    public void setReactorsEnabled(boolean enabled)
    {
        this.enableReactors = enabled;
    }

    public boolean areReactorsEnabled()
    {
        return enableReactors;
    }

    /**
     * Should reactors be enabled
     * <p>
     * Only call once per tick as it checks redstone state
     *
     * @return true if enable
     */
    protected boolean shouldEnableReactors()
    {
        return enableReactors && !world().isBlockPowered(getPos()); //TODO check if works for indirect power (power on other side of block)
    }

    /**
     * Checks cells in reactor stack and builds a list to access
     */
    protected void doRefreshStack() //TODO have reactors trigger this when placed
    {
        inErrorState = false;
        refreshStack = false;
        cells = null;

        ArrayList<TileEntityReactorCell> reactorCellList = new ArrayList();

        //Get stack above cell
        BlockPos pos = getPos().up();
        while (pos.getY() < world.getHeight())
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityReactorCell)
            {
                reactorCellList.add((TileEntityReactorCell) tileEntity);
            }
            else if (tileEntity instanceof TileEntityReactorController)
            {
                inErrorState = true; //TODO maybe use a message instead of a boolean for more detail?
                return;
            }
            else if(tileEntity instanceof TileEntityRodPipe || tileEntity instanceof TileEntityRodPipeInv)
            {
                //Ignore TODO add a is valid check
            }
            else
            {
                break;
            }

            pos = pos.up();
        }

        //Get stack bellow cell
        pos = getPos().down();
        while (pos.getY() > 0)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityReactorCell)
            {
                reactorCellList.add((TileEntityReactorCell) tileEntity);
            }
            else if (tileEntity instanceof TileEntityReactorController)
            {
                inErrorState = true; //TODO maybe use a message instead of a boolean for more detail?
                return;
            }
            else if(tileEntity instanceof TileEntityRodPipe || tileEntity instanceof TileEntityRodPipeInv)
            {
                //Ignore TODO add a is valid check
            }
            else
            {
                break;
            }

            pos = pos.down();
        }

        cells = reactorCellList.toArray(new TileEntityReactorCell[reactorCellList.size()]);
    }

    public boolean isInErrorState()
    {
        return inErrorState;
    }

    public TileEntityReactorCell[] getReactorCells()
    {
        return cells;
    }

    public void markForRefresh()
    {
        refreshStack = true;
    }

    /**
     * Number of reactors currently controlled by this controller
     *
     * @return
     */
    public int getCellCount()
    {
        return getReactorCells() != null ? getReactorCells().length : 0;
    }

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeDescPacket(dataList, player);
        dataList.add(controllerState.ordinal());
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readDescPacket(buf, player);
        final ControllerState prev = controllerState;
        controllerState = ControllerState.get(buf.readInt());
        if (prev != controllerState)
        {
            world.markBlockRangeForRenderUpdate(getPos(), getPos());
        }
    }

    @Override
    public String toString()
    {
        return "ReactorController[W: " + worldName() + " | D: " + dim() + " | Pos(" + xi() + ", " + yi() + ", " + zi() + ")]@" + hashCode();
    }
}
