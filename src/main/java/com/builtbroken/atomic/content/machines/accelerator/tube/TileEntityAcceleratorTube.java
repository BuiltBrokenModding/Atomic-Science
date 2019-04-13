package com.builtbroken.atomic.content.machines.accelerator.tube;

import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class TileEntityAcceleratorTube extends TileEntityPrefab
{
    public static final String NBT_ROTATION = "rotation";
    public static final String NBT_CONNECTION = "connection";

    protected EnumFacing direction;
    protected AcceleratorConnectionType connectionType = AcceleratorConnectionType.NORMAL;

    public final AcceleratorNode acceleratorNode = new AcceleratorNode(this); //TODO turn into capability

    @Override
    public void markDirty()
    {
        super.markDirty();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (acceleratorNode.getNetwork() != null)
        {
            acceleratorNode.getNetwork().destroy();
        }
    }

    @Override
    public void onChunkUnload()
    {
        //TODO mark node as unloaded, find way to restore node
        if (acceleratorNode.getNetwork() != null)
        {
            acceleratorNode.getNetwork().destroy();
        }
    }

    @Override
    public void onLoad()
    {
        if (!world().isRemote)
        {
            updateState(false, true);
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        if (oldState.getBlock() instanceof BlockAcceleratorTube && oldState.getBlock() == newSate.getBlock())
        {
            return oldState.getValue(BlockAcceleratorTube.TYPE_PROP) != newSate.getValue(BlockAcceleratorTube.TYPE_PROP);
        }
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey(NBT_ROTATION))
        {
            direction = EnumFacing.byIndex(compound.getByte(NBT_ROTATION));
        }
        if (compound.hasKey(NBT_CONNECTION))
        {
            connectionType = AcceleratorConnectionType.byIndex(compound.getByte(NBT_CONNECTION));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        saveStateNBT(compound);
        return super.writeToNBT(compound);
    }

    protected void saveStateNBT(NBTTagCompound compound)
    {
        compound.setByte(NBT_ROTATION, (byte) getDirection().ordinal());
        compound.setByte(NBT_CONNECTION, (byte) getConnectionType().ordinal());
    }

    /**
     * Updates the block state to match the stored values. Not
     * all values are saved to the metadata so this is needed
     * to update the logical and visual states.
     *
     * @param doBlockUpdate - runs a block update and notifies neighbors
     * @param setBlock      - changes the block state in world when true
     */
    public IBlockState updateState(boolean doBlockUpdate, boolean setBlock)
    {
        //Build state
        IBlockState state = world().getBlockState(getPos());
        if (direction != null)
        {
            state = state.withProperty(BlockAcceleratorTube.ROTATION_PROP, direction);
        }
        state = state.withProperty(BlockAcceleratorTube.CONNECTION_PROP, getConnectionType());

        //Update node in network
        acceleratorNode.updateCache();

        //Update actual block
        if (setBlock)
        {
            //Set state
            world.setBlockState(getPos(), state, doBlockUpdate ? 3 : 2);

            if (isServer())
            {
                //Tell the chunk it has changed
                this.world.markChunkDirty(this.pos, this);

                //Sends data to the client
                sendDescPacket();
            }
        }
        return state;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = super.getUpdateTag();
        saveStateNBT(tag);
        return tag;
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        return false;
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {

    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    /**
     * Updates the connection data and updates the block state
     *
     * @param updateBlockState - change the block state in the world when true
     */
    public IBlockState updateConnections(boolean updateBlockState)
    {
        boolean behind = canConnect(direction.getOpposite());
        boolean left = canConnect(direction.rotateY().getOpposite());
        boolean right = canConnect(direction.rotateY());

        if (behind && left && right)
        {
            connectionType = AcceleratorConnectionType.INTERSECTION;
        }
        else if (left && right)
        {
            connectionType = AcceleratorConnectionType.T_JOIN;
        }
        else if (left && behind)
        {
            connectionType = AcceleratorConnectionType.T_JOIN_LEFT;
        }
        else if (right && behind)
        {
            connectionType = AcceleratorConnectionType.T_JOIN_RIGHT;
        }
        else if (left)
        {
            connectionType = AcceleratorConnectionType.CORNER_LEFT;
        }
        else if (right)
        {
            connectionType = AcceleratorConnectionType.CORNER_RIGHT;
        }
        else
        {
            connectionType = AcceleratorConnectionType.NORMAL;
        }
        return updateState(false, updateBlockState);
    }

    public boolean canConnect(EnumFacing side)
    {
        final BlockPos pos = getPos().offset(side);
        TileEntity tile = world().getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube) //TODO use capability
        {
            return true;
        }
        return false;
    }


    public EnumFacing getDirection()
    {
        if (direction == null)
        {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }

    public AcceleratorConnectionType getConnectionType()
    {
        if (connectionType == null)
        {
            connectionType = AcceleratorConnectionType.NORMAL;
        }
        return connectionType;
    }
}
