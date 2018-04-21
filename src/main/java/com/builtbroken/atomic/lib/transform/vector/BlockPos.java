package com.builtbroken.atomic.lib.transform.vector;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Pos that uses ints rather than doubles
 * <p>
 * Main use is for block location data but can be used for anything.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2017.
 */
public class BlockPos implements IPos3D, Comparable<BlockPos>
{
    public final int x, y, z;

    public BlockPos(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(IPos3D pos3D)
    {
        this.x = pos3D.xi();
        this.y = pos3D.yi();
        this.z = pos3D.zi();
    }

    public BlockPos(IPos3D pos3D, ForgeDirection dir)
    {
        this.x = pos3D.xi() + dir.offsetX;
        this.y = pos3D.yi() + dir.offsetY;
        this.z = pos3D.zi() + dir.offsetZ;
    }

    public BlockPos(IPos3D pos3D, EnumFacing dir)
    {
        this.x = pos3D.xi() + dir.getFrontOffsetX();
        this.y = pos3D.yi() + dir.getFrontOffsetY();
        this.z = pos3D.zi() + dir.getFrontOffsetZ();
    }

    public BlockPos(NBTTagCompound tag)
    {
        this.x = tag.getInteger("x");
        this.y = tag.getInteger("y");
        this.z = tag.getInteger("z");
    }

    public NBTTagCompound save()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);
        return tag;
    }

    @Override
    public double x()
    {
        return x;
    }

    @Override
    public int xi()
    {
        return x;
    }

    @Override
    public double y()
    {
        return y;
    }

    @Override
    public int yi()
    {
        return y;
    }

    @Override
    public double z()
    {
        return z;
    }

    @Override
    public int zi()
    {
        return z;
    }

    public boolean isAirBlock(World world)
    {
        Block block = getBlock(world);
        if (block != null)
        {
            return block.isAir(world, xi(), yi(), zi());
        }
        return false;
    }

    public Block getBlock(World world)
    {
        if (world != null)
        {
            return world.getBlock(xi(), yi(), zi()); //TODO check if chunk is loaded
        }
        return null;
    }

    public float getHardness(World world)
    {
        Block block = getBlock(world);
        if (block != null)
        {
            return block.getBlockHardness(world, xi(), yi(), zi());
        }
        return -1;
    }

    public boolean isReplaceable(World world)
    {
        Block block = getBlock(world);
        if (block != null)
        {
            return block.isReplaceable(world, xi(), yi(), zi());
        }
        return false;
    }

    public double getResistance(Entity explosionBlameEntity, double x, double y, double z)
    {
        return 0;
    }

    public BlockPos add(IPos3D pos)
    {
        return add(pos.xi(), pos.yi(), pos.zi());
    }

    public BlockPos add(int x, int y, int z)
    {
        return new BlockPos(this.x + x, this.y + y, this.z + z);
    }

    public boolean canSeeSky(World world)
    {
        return world != null && world.canBlockSeeTheSky(xi(), yi(), zi());
    }

    public int getBlockMetadata(World world)
    {
        if (world != null)
        {
            return world.getBlockMetadata(xi(), yi(), zi());
        }
        return 0;
    }

    public double distance(double x, double y, double z)
    {
        return new Pos(this).add(0.5).distance(x, y, z); //TODO replace with actual code
    }

    public Pos toPos()
    {
        return new Pos(xi(), yi(), zi());
    }

    public TileEntity getTileEntity(World world)
    {
        if (world != null)
        {
            return world.getTileEntity(xi(), yi(), zi());
        }
        return null;
    }

    public double distance(IPos3D center)
    {
        return distance(center.x(), center.y(), center.z());
    }

    @Override
    public int hashCode()
    {
        int hash = 17;
        hash = hash * 31 + x;
        hash = hash * 31 + y;
        hash = hash * 31 + z;
        return hash;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof IPos3D)
        {
            IPos3D pos = (IPos3D) o;
            return pos.xi() == xi() && pos.yi() == yi() && pos.zi() == zi();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "BlockPos[" + this.xi() + "," + this.yi() + "," + this.zi() + "]";
    }

    @Override
    public int compareTo(BlockPos that)
    {
        return compare(that);
    }

    public int compare(IPos3D that)
    {
        if (xi() < that.xi() || yi() < that.yi() || zi() < that.zi())
        {
            return -1;
        }
        if (xi() > that.xi() || yi() > that.yi() || zi() > that.zi())
        {
            return 1;
        }
        return 0;
    }
}