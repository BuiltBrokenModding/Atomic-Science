package com.builtbroken.atomic.content.tiles.reactor.fission;

import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.tiles.TileEntityMachine;
import net.minecraft.block.Block;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class TileEntityReactorCell extends TileEntityMachine
{
    /** Client side */
    public boolean _running = false;

    public StructureType structureType;

    @Override
    protected void firstTick()
    {
        super.firstTick();
        updateStructureType();
    }

    @Override
    public void update(int ticks)
    {
        super.update(ticks);
        if (isServer())
        {
            if (canOperate())
            {
                consumeFuel();
                generate();
            }
        }
        else if (_running)
        {
            //TODO run client side effects
        }
    }

    protected void generate()
    {
        //TODO calculate heat generated
        //TODO generate heat to thermal map

        //TODO calculate radioactive effects
        //TODO dump radiation to map

        //TODO calculate radioactive material leaking
        //TODO dump radioactive material to area or drains
    }

    protected void consumeFuel()
    {
        //TODO damage fuel rods
    }

    protected boolean canOperate()
    {
        //TODO check for fuel rod
        //TODO check for safety (water, temp, etc)
        //TODO check if can generate neutrons (controls rods can force off)
        //TODO check for redstone disable
        return true;
    }

    public void updateStructureType()
    {
        Block blockAbove = worldObj.getBlock(xCoord, yCoord + 1, zCoord);
        Block blockBelow = worldObj.getBlock(xCoord, yCoord - 1, zCoord);

        if (blockAbove == ASBlocks.blockReactorCell && blockBelow == ASBlocks.blockReactorCell)
        {
            structureType = StructureType.MIDDLE;
        }
        else if (blockBelow == ASBlocks.blockReactorCell)
        {
            structureType = StructureType.TOP;
        }
        else if (blockAbove == ASBlocks.blockReactorCell)
        {
            structureType = StructureType.BOTTOM;
        }
        else
        {
            structureType = StructureType.NORMAL;
        }
    }

    public boolean isTop()
    {
        return structureType == StructureType.TOP;
    }

    public boolean isMiddle()
    {
        return structureType == StructureType.MIDDLE;
    }

    public boolean isBottom()
    {
        return structureType == StructureType.BOTTOM;
    }

    public static enum StructureType
    {
        NORMAL,
        TOP,
        MIDDLE,
        BOTTOM;
    }
}
