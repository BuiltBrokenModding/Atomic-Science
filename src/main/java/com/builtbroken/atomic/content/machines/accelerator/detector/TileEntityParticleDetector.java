package com.builtbroken.atomic.content.machines.accelerator.detector;

import com.builtbroken.atomic.api.accelerator.AcceleratorHelpers;
import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.particle.AcceleratorParticle;
import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import com.builtbroken.atomic.lib.timer.TickTimerConditional;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2019.
 */
public class TileEntityParticleDetector extends TileEntityMachine
{
    public static final String NBT_TUBE_TYPE = "tube_type";
    public static final String NBT_SIDE_SETTINGS = "tube_settings";
    public static final String NBT_SIDE = "side";
    public static final String NBT_SPEED = "speed";

    protected final HashMap<TubeSide, Float> speedSettings = new HashMap();
    protected TubeConnectionType tubeConnectionType;
    protected IAcceleratorNode node;

    public TileEntityParticleDetector()
    {
        tickServer.add(TickTimerConditional.newTrigger((tick) -> detectTube(), () -> node == null));
    }

    /**
     * Tries to detect the tube under the detector
     */
    public void detectTube()
    {
        node = getNode(world, EnumFacing.DOWN);
        if(node != null)
        {
            if (tubeConnectionType != node.getConnectionType())
            {
                tubeConnectionType = node.getConnectionType();
                speedSettings.clear();
                tubeConnectionType.outputSides.forEach(side -> speedSettings.put(side, 0f));
            }
            if (node instanceof AcceleratorNode)
            {
                ((AcceleratorNode) node).turnController = (p, sides) -> getTurn(sides, p);
            }
        }
    }

    public void detectLaser()
    {
        //TODO implement
    }

    private IAcceleratorNode getNode(IBlockAccess access, EnumFacing facing) //TODO move to helper
    {
        final TileEntity tileEntity = access.getTileEntity(getPos().offset(facing));
        final IAcceleratorTube tube = AcceleratorHelpers.getAcceleratorTube(tileEntity, facing.getOpposite());
        if (tube != null)
        {
            return tube.getNode();
        }
        return null;
    }

    /**
     * Called to get the turn output of the tube under the detector
     *
     * @param possibleOutputs
     * @param particle
     * @return best turn possible or the particle
     */
    public TubeSide getTurn(ImmutableList<TubeSide> possibleOutputs, AcceleratorParticle particle)
    {
        TubeSide out = null;
        float highestSpeedMatch = -1;
        for (TubeSide side : possibleOutputs)
        {
            if (speedSettings.containsKey(side))
            {
                float speed = speedSettings.get(side);
                if (speed > highestSpeedMatch && particle.getVelocity() >= speed)
                {
                    out = side;
                    highestSpeedMatch = speed;
                }
            }
            else if (out == null)
            {
                out = side;
            }
        }
        return out;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey(NBT_TUBE_TYPE))
        {
            tubeConnectionType = TubeConnectionType.byIndex(compound.getInteger(NBT_TUBE_TYPE));
        }
        if (compound.hasKey(NBT_SIDE_SETTINGS))
        {
            speedSettings.clear();

            NBTTagList tubeSettingsSave = compound.getTagList(NBT_SIDE_SETTINGS, 10);
            for(int i = 0; i < tubeSettingsSave.tagCount(); i++)
            {
                NBTTagCompound tubeData = tubeSettingsSave.getCompoundTagAt(i);
                TubeSide side = TubeSide.byIndex(tubeData.getInteger(NBT_SIDE));
                float speed = tubeData.getFloat(NBT_SPEED);
                speedSettings.put(side, speed);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if(tubeConnectionType != null)
        {
            compound.setInteger(NBT_TUBE_TYPE, tubeConnectionType.ordinal());
        }
        if(!speedSettings.isEmpty())
        {
            NBTTagList tubeSettingsSave = new NBTTagList();
            for(Map.Entry<TubeSide, Float> entry : speedSettings.entrySet())
            {
                NBTTagCompound tubeData = new NBTTagCompound();
                tubeData.setInteger(NBT_SIDE, entry.getKey().ordinal());
                tubeData.setFloat(NBT_SPEED, entry.getValue());
                tubeSettingsSave.appendTag(tubeData);
            }
            compound.setTag(NBT_SIDE_SETTINGS, tubeSettingsSave);
        }
        return super.writeToNBT(compound);
    }
}
