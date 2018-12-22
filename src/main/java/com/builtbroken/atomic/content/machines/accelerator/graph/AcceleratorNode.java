package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.content.machines.accelerator.tube.AcceleratorConnectionType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2018.
 */
public class AcceleratorNode
{
    public final List<AcceleratorNode> nodes = new ArrayList(4);

    public final EnumFacing direction;
    public final BlockPos pos;
    public AcceleratorConnectionType connectionType;

    public AcceleratorNode(BlockPos pos, EnumFacing direction, AcceleratorConnectionType connectionType)
    {
        this.pos = pos;
        this.direction = direction;
        this.connectionType = connectionType;
    }

    public void connect(AcceleratorNode acceleratorNode)
    {
        if (acceleratorNode != null)
        {
            if (!nodes.contains(acceleratorNode))
            {
                nodes.add(acceleratorNode);
            }
            if (!acceleratorNode.nodes.contains(this))
            {
                acceleratorNode.nodes.add(this);
            }
        }
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (object instanceof AcceleratorNode)
        {
            return pos.equals(((AcceleratorNode) object).pos);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return pos.hashCode();
    }
}

