package com.builtbroken.atomic.content.machines.accelerator.tube.normal;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.content.machines.accelerator.tube.BlockAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.tube.imp.TileEntityAcceleratorTubePrefab;
import com.builtbroken.jlib.data.science.units.UnitDisplay;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class TileEntityAcceleratorTube extends TileEntityAcceleratorTubePrefab
{

    public static final String NBT_ROTATION = "rotation";
    public static final String NBT_CONNECTION = "connection";

    protected EnumFacing direction;
    private TubeConnectionType _connectionType = TubeConnectionType.NORMAL;

    private static final UnitDisplay.Unit SPEED = new UnitDisplay.Unit("Meters per Tick", "m/t");
    private static final UnitDisplay SPEED_DISPLAY = new UnitDisplay(SPEED, 0, false).symbol(true);


    @Override
    public void markDirty()
    {
        super.markDirty();
    }

    @Override
    public void onLoad()
    {
        acceleratorNode.setData(getPos(), getDirection(), getConnectionType());
        acceleratorNode.onMoveCallback = (particle) -> debugSpeed(particle);
        if (isServer())
        {
            updateState(false, true);
        }
    }

    public void debugSpeed(AcceleratorParticle particle)
    {
        final float vel = particle.getVelocity();
        SPEED_DISPLAY.value = vel;

        final String speed = SPEED_DISPLAY.toString();
        setSign(getPos().offset(getDirection().rotateY()), speed);
        setSign(getPos().offset(getDirection().rotateY().getOpposite()), speed);
    }

    public void setSign(BlockPos pos, String speed)
    {
        if(world.isBlockLoaded(pos))
        {
            TileEntity tile = getTileEntityIfLoaded(pos);
            IBlockState iblockstate = world.getBlockState(pos);
            if(tile instanceof TileEntitySign)
            {
                TileEntitySign sign = (TileEntitySign) tile;
                String signText1 = sign.signText[0] != null ? sign.signText[0].getUnformattedText() : null;
                if(signText1 != null && signText1.trim().equalsIgnoreCase("[SPEED]"))
                {
                    sign.signText[1] = new TextComponentString(speed);
                    sign.markDirty();
                    world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
                }
            }
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
    public IBlockState updateConnections(IBlockAccess access, boolean updateBlockState, boolean updateState)
    {
        //Calculate layout
        TubeConnectionType type = calcConnectionType(access);
        if (type != TubeConnectionType.INVALID)
        {
            setConnectionType(type);
        }
        else
        {
            type = guessConnectionType(access);
            if (type != TubeConnectionType.INVALID)
            {
                setConnectionType(type);
            }
        }

        //Update connections on node
        acceleratorNode.updateConnections(access);

        //Update block state
        return updateState ? updateState(false, updateBlockState) : null;
    }

    public TubeConnectionType calcConnectionType(IBlockAccess access)
    {
        final TubeSideType front = acceleratorNode.getConnectedTubeState(access, TubeSide.FRONT);
        final TubeSideType left = acceleratorNode.getConnectedTubeState(access, TubeSide.LEFT);
        final TubeSideType right = acceleratorNode.getConnectedTubeState(access, TubeSide.RIGHT);
        final TubeSideType back = acceleratorNode.getConnectedTubeState(access, TubeSide.BACK);

        //Get connection type
        return TubeConnectionType.getTypeForLayout(front, left, right, back, true);
    }

    public TubeConnectionType guessConnectionType(IBlockAccess access)
    {
        final TubeSideType left = acceleratorNode.getConnectedTubeState(access, TubeSide.LEFT);
        final TubeSideType right = acceleratorNode.getConnectedTubeState(access, TubeSide.RIGHT);
        final TubeSideType back = acceleratorNode.getConnectedTubeState(access, TubeSide.BACK);

        //Get connection type
        return TubeConnectionType.getTypeForLayout(TubeSideType.EXIT, left, right, back, true);
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
