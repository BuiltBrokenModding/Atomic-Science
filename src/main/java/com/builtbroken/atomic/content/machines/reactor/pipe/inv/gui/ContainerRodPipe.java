package com.builtbroken.atomic.content.machines.reactor.pipe.inv.gui;

import com.builtbroken.atomic.content.machines.reactor.pipe.inv.TileEntityRodPipeInv;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.SlotMachine;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class ContainerRodPipe extends ContainerBase<TileEntityRodPipeInv>
{
    public ContainerRodPipe(EntityPlayer player, TileEntityRodPipeInv node)
    {
        super(player, node);
        addSlotToContainer(new SlotMachine(node.getInventory(), 0, 80, 40));
        addPlayerInventory(player);
    }
}
