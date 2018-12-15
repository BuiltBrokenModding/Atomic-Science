package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.content.machines.accelerator.tube.AcceleratorConnectionType;
import net.minecraft.util.EnumFacing;

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
    public AcceleratorConnectionType connectionType;

    public AcceleratorNode(EnumFacing direction, AcceleratorConnectionType connectionType)
    {
        this.direction = direction;
        this.connectionType = connectionType;
    }

    public void connect(AcceleratorNode acceleratorNode)
    {
        if(acceleratorNode != null)
        {
            nodes.add(acceleratorNode);
            acceleratorNode.nodes.add(this);
        }
    }
}
