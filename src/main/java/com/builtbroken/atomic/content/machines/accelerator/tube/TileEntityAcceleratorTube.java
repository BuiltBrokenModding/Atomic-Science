package com.builtbroken.atomic.content.machines.accelerator.tube;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
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
    protected TubeConnectionType connectionType = TubeConnectionType.NORMAL;

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
        if (isServer())
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
            connectionType = TubeConnectionType.byIndex(compound.getByte(NBT_CONNECTION));
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
        IBlockState state = getState();
        if (direction != null)
        {
            state = state.withProperty(BlockAcceleratorTube.ROTATION_PROP, direction);
        }
        state = state.withProperty(BlockAcceleratorTube.CONNECTION_PROP, getConnectionType());

        //Update node in network
        acceleratorNode.updateCache();

        //Update actual block
        if (setBlock && world != null) //JUnit world may be null
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
        connectionType = calcConnectionType();
        return updateState(false, updateBlockState);
    }

    public TubeConnectionType calcConnectionType()
    {
        //TODO if we have no connections that are valid, default to current type
        final TubeSideType front = canConnect(TubeSide.FRONT);
        final TubeSideType left = canConnect(TubeSide.LEFT);
        final TubeSideType right = canConnect(TubeSide.RIGHT);
        final TubeSideType back = canConnect(TubeSide.BACK);

        //Get connection type
        return TubeConnectionType.getTypeForLayout(front, left, right, back);
    }

    public TubeSideType canConnect(TubeSide tubeSide)
    {
        final EnumFacing side = tubeSide.getFacing(getDirection());
        final TileEntityAcceleratorTube tube = getTubeSide(side); //TODO use capability
        if (tube != null)
        {
            return tube.getConnectionType().getTypeForSide(tubeSide.getOpposite());
        }
        return TubeSideType.NONE;
    }

    protected TileEntityAcceleratorTube getTubeSide(EnumFacing side)
    {
        final TileEntity tile = getTileEntity(side);
        if (tile instanceof TileEntityAcceleratorTube) //TODO use capability
        {
            return (TileEntityAcceleratorTube) tile;
        }
        return null;
    }

    public EnumFacing getDirection()
    {
        if (direction == null)
        {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }

    public TubeConnectionType getConnectionType()
    {
        if (connectionType == null)
        {
            connectionType = TubeConnectionType.NORMAL;
        }
        return connectionType;
    }
}
