package com.builtbroken.atomic.content.machines.accelerator.tube;

import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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

    protected EnumFacing direction;
    protected AcceleratorConnectionType connectionType = AcceleratorConnectionType.NORMAL;

    @Override
    public void markDirty()
    {
        super.markDirty();
    }

    @Override
    public void onLoad()
    {
        if (!world().isRemote)
        {
            updateState(false);
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey(NBT_ROTATION))
        {
            direction = EnumFacing.byIndex(compound.getInteger(NBT_ROTATION));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setByte(NBT_ROTATION, (byte) getDirection().ordinal());
        return super.writeToNBT(compound);
    }

    /**
     * Updates the block state to match the stored values. Not
     * all values are saved to the metadata so this is needed
     * to update the logical and visual states.
     *
     * @param update - runs a block update and notifies neighbors
     */
    public void updateState(boolean update)
    {
        //Build state
        IBlockState state = world().getBlockState(getPos());
        if (direction != null)
        {
            state = state.withProperty(BlockAcceleratorTube.ROTATION_PROP, direction);
        }
        state = state.withProperty(BlockAcceleratorTube.CONNECTION_PROP, getConnectionType());

        //Set state
        world.setBlockState(getPos(), state, update ? 3 : 2);

        if (isServer())
        {
            //Tell the chunk it has changed
            this.world.markChunkDirty(this.pos, this);

            //Sends data to the client
            sendDescPacket();
        }
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
        tag.setByte(NBT_ROTATION, (byte) getDirection().ordinal());
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
     */
    public void updateConnections()
    {
        //TODO update connections
        updateState(false);
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
