package com.builtbroken.atomic.lib.transform.vector;

import com.builtbroken.atomic.lib.transform.rotation.EulerAngle;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.jlib.data.vector.ITransform;
import com.builtbroken.jlib.data.vector.Pos3D;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Abstract version of Pos3D for interaction with the minecraft world
 * Created by robert on 1/13/2015.
 */
public abstract class AbstractPos<R extends AbstractPos> extends Pos3D<R> implements IPosition
{
    public AbstractPos()
    {
        this(0, 0, 0);
    }

    public AbstractPos(double a)
    {
        this(a, a, a);
    }

    public AbstractPos(double x, double y, double z)
    {
        super(x, y, z);
    }

    public AbstractPos(double yaw, double pitch)
    {
        this(-Math.sin(Math.toRadians(yaw)), Math.sin(Math.toRadians(pitch)), -Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
    }

    public AbstractPos(TileEntity tile)
    {
        this(tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public AbstractPos(Entity entity)
    {
        this(entity.posX, entity.posY, entity.posZ);
    }

    public AbstractPos(IPos3D vec)
    {
        this(vec.x(), vec.y(), vec.z());
    }

    public AbstractPos(NBTTagCompound nbt)
    {
        this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
    }

    public AbstractPos(ByteBuf data)
    {
        this(data.readDouble(), data.readDouble(), data.readDouble());
    }

    public AbstractPos(MovingObjectPosition par1)
    {
        this(par1.blockX, par1.blockY, par1.blockZ);
    }

    public AbstractPos(ChunkCoordinates par1)
    {
        this(par1.posX, par1.posY, par1.posZ);
    }

    public AbstractPos(ForgeDirection dir)
    {
        this(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public AbstractPos(EnumFacing dir)
    {
        this(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ());
    }

    public AbstractPos(Vec3 vec)
    {
        this(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public double angle(IPos3D other)
    {
        return Math.acos((this.cross(other)).magnitude() / (new Pos(other).magnitude() * magnitude()));
    }

    public double anglePreNorm(IPos3D other)
    {
        return Math.acos(this.cross(other).magnitude());
    }

    //=========================
    //========Converters=======
    //=========================

    public Vec3 toVec3()
    {
        return Vec3.createVectorHelper(x(), y(), z());
    }

    public Point toVector2()
    {
        return new Point(x(), z());
    }

    public ForgeDirection toForgeDirection()
    {
        //TODO maybe add a way to convert convert any vector into a direction from origin
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            if (xi() == dir.offsetX && yi() == dir.offsetY && zi() == dir.offsetZ)
            {
                return dir;
            }
        }
        return ForgeDirection.UNKNOWN;
    }

    public EulerAngle toEulerAngle(IPos3D target)
    {
        return sub(target).toEulerAngle();
    }

    public EulerAngle toEulerAngle()
    {
        return new EulerAngle(Math.toDegrees(Math.atan2(x(), z())), Math.toDegrees(-Math.atan2(y(), Math.hypot(z(), x()))));
    }

    public IPos3D transform(ITransform transformer)
    {
        if (this instanceof IPos3D)
        {
            return transformer.transform((IPos3D) this);
        }
        return null;
    }

    /**
     * Calls {@link Math#abs(double)} on each term of the pos data
     *
     * @return abs
     */
    public R absolute()
    {
        return newPos(Math.abs(x()), Math.abs(y()), Math.abs(z()));
    }

    //=========================
    //======Math Operators=====
    //=========================

    public R add(ForgeDirection dir)
    {
        return add(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public R add(ForgeDirection dir, float scale)
    {
        return add(dir.offsetX * scale, dir.offsetY * scale, dir.offsetZ * scale);
    }

    public R add(EnumFacing face)
    {
        return add(face.getFrontOffsetX(), face.getFrontOffsetY(), face.getFrontOffsetZ());
    }

    public R add(Vec3 vec)
    {
        return add(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public R sub(ForgeDirection dir)
    {
        return sub(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public R sub(EnumFacing face)
    {
        return sub(face.getFrontOffsetX(), face.getFrontOffsetY(), face.getFrontOffsetZ());
    }

    public R sub(Vec3 vec)
    {
        return add(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public double distance(Vec3 vec)
    {
        return distance(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public double distance(Entity entity)
    {
        return distance(entity.posX, entity.posY, entity.posZ);
    }

    public double distance(TileEntity tileEntity)
    {
        return distance(tileEntity.xCoord + 0.5, tileEntity.yCoord + 0.5, tileEntity.zCoord + 0.5);
    }

    public R multiply(ForgeDirection dir)
    {
        return multiply(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public R multiply(EnumFacing face)
    {
        return multiply(face.getFrontOffsetX(), face.getFrontOffsetY(), face.getFrontOffsetZ());
    }

    public R multiply(Vec3 vec)
    {
        return multiply(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public R divide(ForgeDirection dir)
    {
        return divide(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public R divide(EnumFacing face)
    {
        return divide(face.getFrontOffsetX(), face.getFrontOffsetY(), face.getFrontOffsetZ());
    }

    public R divide(Vec3 vec)
    {
        return divide(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    @Override
    public R floor()
    {
        return newPos(Math.floor(x()), Math.floor(y()), Math.floor(z()));
    }

    //=========================
    //========NBT==============
    //=========================

    public NBTTagCompound toNBT()
    {
        return writeNBT(new NBTTagCompound());
    }

    public NBTTagCompound toIntNBT()
    {
        return writeIntNBT(new NBTTagCompound());
    }

    public NBTTagCompound writeNBT(NBTTagCompound nbt)
    {
        nbt.setDouble("x", x());
        nbt.setDouble("y", y());
        nbt.setDouble("z", z());
        return nbt;
    }


    public NBTTagCompound writeIntNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("x", xi());
        nbt.setInteger("y", yi());
        nbt.setInteger("z", zi());
        return nbt;
    }

    public ByteBuf writeByteBuf(ByteBuf data)
    {
        data.writeDouble(x());
        data.writeDouble(y());
        data.writeDouble(z());
        return data;
    }

    public MovingObjectPosition rayTrace(World world, IPos3D dir, double dist)
    {
        return rayTrace(world, new Pos(x() + dir.x() * dist, y() + dir.y() * dist, z() + dir.z() * dist));
    }


    public MovingObjectPosition rayTrace(World world, IPos3D end)
    {
        return rayTrace(world, end, false, false, false);
    }

    public MovingObjectPosition rayTrace(World world, IPos3D end, boolean rightClickWithBoat, boolean doColliderCheck, boolean doMiss)
    {
        MovingObjectPosition block = rayTraceBlocks(world, end, rightClickWithBoat, doColliderCheck, doMiss);
        MovingObjectPosition entity = rayTraceEntities(world, end);

        if (block == null)
        {
            return entity;
        }
        if (entity == null)
        {
            return block;
        }

        if (distance(new Pos(block.hitVec)) < distance(new Pos(entity.hitVec)))
        {
            return block;
        }

        return entity;
    }


    public MovingObjectPosition rayTraceBlocks(World world, IPos3D end)
    {
        return rayTraceBlocks(world, end, false, false, false);
    }

    public MovingObjectPosition rayTraceBlocks(World world, IPos3D end, boolean b1, boolean b2, boolean b3)
    {
        return world.func_147447_a(toVec3(), Vec3.createVectorHelper(end.x(), end.y(), end.z()), b1, b2, b3);
    }

    public MovingObjectPosition rayTraceEntities(World world, IPos3D end)
    {
        MovingObjectPosition closestEntityMOP = null;
        double closetDistance = 0D;

        double checkDistance = distance(end);
        AxisAlignedBB scanRegion = AxisAlignedBB.getBoundingBox(-checkDistance, -checkDistance, -checkDistance, checkDistance, checkDistance, checkDistance).offset(x(), y(), z());

        List checkEntities = world.getEntitiesWithinAABB(Entity.class, scanRegion);

        for (Object obj : checkEntities)
        {
            Entity entity = (Entity) obj;
            if (entity != null && entity.canBeCollidedWith() && entity.boundingBox != null)
            {
                float border = entity.getCollisionBorderSize();
                AxisAlignedBB bounds = entity.boundingBox.expand(border, border, border);
                MovingObjectPosition hit = bounds.calculateIntercept(toVec3(), Vec3.createVectorHelper(end.x(), end.y(), end.z()));

                if (hit != null)
                {
                    if (bounds.isVecInside(toVec3()))
                    {
                        if (0 < closetDistance || closetDistance == 0)
                        {
                            closestEntityMOP = new MovingObjectPosition(entity);

                            closestEntityMOP.hitVec = hit.hitVec;
                            closetDistance = 0;
                        }
                    }
                    else
                    {
                        double dist = distance(new Pos(hit.hitVec));

                        if (dist < closetDistance || closetDistance == 0)
                        {
                            closestEntityMOP = new MovingObjectPosition(entity);
                            closestEntityMOP.hitVec = hit.hitVec;

                            closetDistance = dist;
                        }
                    }
                }
            }
        }

        return closestEntityMOP;
    }

    //===================
    //===World Setters===
    //===================
    public boolean setBlock(World world, Block block)
    {
        return setBlock(world, block, 0);
    }

    public boolean setBlock(World world, Block block, int metadata)
    {
        return setBlock(world, block, metadata, 3);
    }

    public boolean setBlock(World world, Block block, int metadata, int notify)
    {
        if (world != null && block != null)
        {
            return world.setBlock(xi(), yi(), zi(), block, metadata, notify);
        }
        else
        {
            return false;
        }
    }

    public boolean setBlockToAir(World world)
    {
        return world.setBlockToAir(xi(), yi(), zi());
    }

    //===================
    //==World Accessors==
    //===================
    public boolean isAirBlock(World world)
    {
        return world.isAirBlock(xi(), yi(), zi());
    }

    public boolean isBlockFreezable(World world)
    {
        return world.isBlockFreezable(xi(), yi(), zi());
    }

    /**
     * Checks if the block is replaceable
     *
     * @return true if it can be replaced
     */
    public boolean isReplaceable(World world)
    {
        Block block = getBlock(world);
        return block == null || block == Blocks.air || block.isAir(world, xi(), yi(), zi()) || block.isReplaceable(world, xi(), yi(), zi());
    }

    /**
     * Checks to see if the tile can see the sky
     *
     * @return true if it can see sky, false if not or world is null
     */
    public boolean canSeeSky(World world)
    {
        return world.canBlockSeeTheSky(xi(), yi(), zi());
    }

    public boolean isBlockEqual(World world, Block block)
    {
        Block b = getBlock(world);
        return b != null && b == block;
    }

    public Block getBlock(IBlockAccess world)
    {
        if (world != null) //TODO check if chunk is loaded
        {
            return world.getBlock(xi(), yi(), zi());
        }
        else
        {
            return null;
        }
    }

    public int getBlockMetadata(IBlockAccess world)
    {
        if (world != null)
        {
            return world.getBlockMetadata(xi(), yi(), zi());
        }
        else
        {
            return 0;
        }
    }

    public TileEntity getTileEntity(IBlockAccess world)
    {
        if (world != null) //TODO check if chunk is loaded
        {
            return world.getTileEntity(xi(), yi(), zi());
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
        else
        {
            return 0;
        }
    }

    /**
     * Gets the resistance of a block using block.getResistance method
     *
     * @param cause - entity that triggered/is the explosion
     */
    public float getResistance(Entity cause)
    {
        return getResistance(cause.worldObj, cause, x(), y(), z());
    }

    /**
     * Gets the resistance of a block using block.getResistance method
     *
     * @param cause - entity that triggered/is the explosion
     */
    public float getResistanceToEntity(Entity cause)
    {
        return getBlock(cause.worldObj).getExplosionResistance(cause);
    }

    /**
     * Gets the resistance of a block using block.getResistance method
     *
     * @param cause - entity that triggered/is the explosion
     */
    public float getResistanceToEntity(World world, Entity cause)
    {
        return getBlock(world).getExplosionResistance(cause);
    }

    /**
     * Gets the resistance of a block using block.getResistance method
     *
     * @param world - world to check in
     * @param cause - entity that triggered/is the explosion
     */
    public float getResistance(World world, Entity cause)
    {
        return getResistance(world, cause, cause.posX, cause.posY, cause.posZ);
    }

    /**
     * Gets the resistance of a block using block.getResistance method
     *
     * @param world - world to check in
     * @param cause - entity that triggered/is the explosion
     * @param xx    - xPos location of the explosion
     * @param yy    - xPos location of the explosion
     * @param zz    - xPos location of the explosion
     */
    public float getResistance(World world, Entity cause, double xx, double yy, double zz)
    {
        return getBlock(world).getExplosionResistance(cause, world, xi(), yi(), zi(), xx, yy, zz);
    }

    public boolean isAboveBedrock()
    {
        return y() > 0;
    }

    public boolean isInsideMap()
    {
        return isAboveBedrock() && y() < 255;
    }

    /**
     * Marks a block for update
     *
     * @param world - world to update the location in
     */
    public void markForUpdate(World world)
    {
        world.markBlockForUpdate(xi(), yi(), zi());
    }

    //===================
    //==ILocation Accessors==
    //===================
    @Override
    public double getX()
    {
        return x();
    }

    @Override
    public double getY()
    {
        return y();
    }

    @Override
    public double getZ()
    {
        return z();
    }
}
