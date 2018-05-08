package com.builtbroken.atomic.content.tiles.reactor.fission;

import com.builtbroken.atomic.api.item.IFuelRodItem;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.tiles.TileEntityInventoryMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class TileEntityReactorCell extends TileEntityInventoryMachine
{
    /** Client side */
    private boolean _running = false;
    private boolean _renderFuel = false;
    private float _renderFuelLevel = 0f;

    public StructureType structureType;

    @Override
    protected void firstTick()
    {
        super.firstTick();
        updateStructureType();
    }

    //-----------------------------------------------
    //--------Runtime logic -------------------------
    //-----------------------------------------------

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
        //TODO check for safety (water, temp, etc)
        //TODO check if can generate neutrons (controls rods can force off)
        //TODO check for redstone disable
        return hasFuel();
    }

    //-----------------------------------------------
    //--------Accessors -----------------------------
    //-----------------------------------------------

    public boolean hasFuel()
    {
        return getFuelRod() != null;
    }

    public int getFuelRuntime()
    {
        IFuelRodItem fuelRod = getFuelRod();
        if (fuelRod != null)
        {
            return fuelRod.getFuelRodRuntime(getStackInSlot(0));
        }
        return 0;
    }

    public int getMaxFuelRuntime()
    {
        IFuelRodItem fuelRod = getFuelRod();
        if (fuelRod != null)
        {
            return fuelRod.getMaxFuelRodRuntime(getStackInSlot(0));
        }
        return 0;
    }

    public IFuelRodItem getFuelRod()
    {
        ItemStack stack = getStackInSlot(0);
        return stack != null && stack.getItem() instanceof IFuelRodItem ? (IFuelRodItem) stack.getItem() : null;
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return slot == 0 && stack.getItem() instanceof IFuelRodItem;
    }

    //-----------------------------------------------
    //--------Rendering props -----------------------
    //-----------------------------------------------

    public boolean shouldRenderFuel()
    {
        return _renderFuel;
    }

    public float getFuelRenderLevel()
    {
        if (isServer())
        {
            return (float) getFuelRuntime() / (float) getMaxFuelRuntime();
        }
        return _renderFuelLevel;
    }

    //-----------------------------------------------
    //--------Network code -------------------------
    //-----------------------------------------------

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeDescPacket(dataList, player);
        dataList.add(hasFuel());
        dataList.add(getFuelRenderLevel());
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readDescPacket(buf, player);
        _renderFuel = buf.readBoolean();
        _renderFuelLevel = buf.readFloat();
    }

    //-----------------------------------------------
    //--------Structure code -------------------------
    //-----------------------------------------------

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
