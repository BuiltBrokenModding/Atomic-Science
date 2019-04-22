package com.builtbroken.atomic.content.machines.accelerator.tube;

import com.builtbroken.atomic.api.accelerator.AcceleratorHelpers;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class TileEntityAcceleratorTube extends TileEntityAcceleratorTubePrefab
{

    public static final String NBT_ROTATION = "rotation";
    public static final String NBT_CONNECTION = "connection";

    protected EnumFacing direction;
    private TubeConnectionType _connectionType = TubeConnectionType.NORMAL;


    @Override
    public void markDirty()
    {
        super.markDirty();
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
            setConnectionType(TubeConnectionType.byIndex(compound.getByte(NBT_CONNECTION)));
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
        acceleratorNode.setData(getPos(), direction, getConnectionType());

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

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        dataList.add((byte) getDirection().ordinal());
        dataList.add((byte) getConnectionType().ordinal());
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        direction = EnumFacing.byIndex(buf.readByte());
        setConnectionType(TubeConnectionType.byIndex(buf.readByte()));
    }

    /**
     * Updates the connection data and updates the block state
     *
     * @param updateBlockState - change the block state in the world when true
     */
    public IBlockState updateConnections(boolean updateBlockState)
    {
        setConnectionType(calcConnectionType());
        return updateState(false, updateBlockState);
    }

    public TubeConnectionType calcConnectionType()
    {
        //TODO if we have no connections that are valid, default to current type
        final TubeSideType front = getConnectState(TubeSide.FRONT);
        final TubeSideType left = getConnectState(TubeSide.LEFT);
        final TubeSideType right = getConnectState(TubeSide.RIGHT);
        final TubeSideType back = getConnectState(TubeSide.BACK);

        //Get connection type
        return TubeConnectionType.getTypeForLayout(front, left, right, back, true);
    }

    /**
     * Gets the expect connection state based on our relation
     * to the tube connected to our side.
     * <p>
     * If we have a tube entering from our left. It will
     * tell us that its side is an exit. To our tube this
     * means we expect to see particles enter from that
     * left.
     *
     * @param localSide - side of our tube
     * @return expected state for our side
     */
    public TubeSideType getConnectState(TubeSide localSide)
    {
        //Get tube on side
        final EnumFacing facingSide = localSide.getFacing(getDirection());
        final IAcceleratorTube tube = getTubeSide(facingSide); //TODO use capability
        if (tube != null)
        {
            //Get target tube's side, this will not match out side
            //  Ex: We are facing north, checking  our left connection
            //      target is on our west facing east, we need to check it's front connection status
            final TubeSide targetSide = TubeSide.getSideFacingOut(tube.getNode().getDirection(), facingSide.getOpposite());

            //status for the tube's connection type on its side connecting to our side
            final TubeSideType targetState = tube.getNode().getConnectionType().getTypeForSide(targetSide);

            //We want the opposite state, if a target is an exit then its an enter for our tube
            return targetState.getOpposite();
        }
        return TubeSideType.NONE;
    }

    /**
     * Checks if our current {@link #getConnectionType()} can support a
     * connection on the given side
     *
     * @param localSide - localized side based on facing of the tube
     * @return true if can connect, false if can't
     */
    public boolean canConnect(TubeSide localSide)
    {
        final TubeSideType state = getConnectState(localSide);
        return state != TubeSideType.NONE && getConnectionType().getTypeForSide(localSide) == state;
    }

    protected IAcceleratorTube getTubeSide(EnumFacing side)
    {
        final TileEntity tile = getTileEntity(side);
        return AcceleratorHelpers.getAcceleratorTube(tile, side.getOpposite());
    }

    public EnumFacing getDirection()
    {
        if (direction == null)
        {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }

    @Override
    public void setDirection(EnumFacing direction)
    {
        this.direction = direction;
        super.setDirection(direction);
    }

    @Override
    public void setPos(BlockPos posIn)
    {
        super.setPos(posIn);
        acceleratorNode.setPos(pos);
    }

    public TubeConnectionType getConnectionType()
    {
        if (_connectionType == null)
        {
            setConnectionType(TubeConnectionType.NORMAL);
        }
        return _connectionType;
    }

    public void setConnectionType(TubeConnectionType type)
    {
        TubeConnectionType prev = getConnectionType();
        if (prev != type)
        {
            this._connectionType = type;
            acceleratorNode.setConnectionType(_connectionType);
        }
    }
}
