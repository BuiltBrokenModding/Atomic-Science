package com.builtbroken.atomic.lib.transform.vector;

import com.builtbroken.atomic.lib.transform.IPosWorld;
import com.builtbroken.jlib.data.vector.IPos3D;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.ILocation;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class Location extends AbstractLocation<Location> implements IPosWorld, IPos3D, Comparable<IPosWorld>
{
    public static final Location NULL = new Location(null, 0, 0, 0);

    public Location(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public Location(NBTTagCompound nbt)
    {
        this(DimensionManager.getWorld(nbt.getInteger("dimension")), nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
    }

    public Location(ByteBuf data)
    {
        this(DimensionManager.getWorld(data.readInt()), data.readDouble(), data.readDouble(), data.readDouble());
    }

    public Location(Entity entity)
    {
        this(entity.worldObj, entity.posX, entity.posY, entity.posZ);
    }

    public Location(TileEntity tile)
    {
        this(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public Location(IPosWorld vec)
    {
        this(vec.world(), vec.x(), vec.y(), vec.z());
    }

    public Location(ILocation loc)
    {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
    }

    public Location(World world, IPos3D vector)
    {
        this(world, vector.x(), vector.y(), vector.z());
    }

    public Location(World world, Vec3 vec)
    {
        this(world, vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Location(World world, MovingObjectPosition target)
    {
        this(world, target.hitVec);
    }

    @Override
    public Location newPos(double x, double y, double z)
    {
        return new Location(world, x, y, z);
    }

    public void playSound(String sound, float volume, float pitch)
    {
        world().playSound(x(), y(), z(), sound, volume, pitch, false);
    }

    public void playSound(Block block)
    {
        Block.SoundType soundtype = block.stepSound;
        playSound(soundtype.getStepResourcePath(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(String t, IPos3D vel)
    {
        spawnParticle(t, vel.x(), vel.y(), vel.z());
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(String t, double vel_x, double vel_y, double vel_z)
    {
        world().spawnParticle(t, x(), y(), z(), vel_x, vel_y, vel_z);
    }

    @SideOnly(Side.CLIENT)
    public void playBlockBreakAnimation()
    {
        Block block = getBlock();
        if (block != null && block.getMaterial() != Material.air)
        {
            //Play block sound
            playSound(block);

            //Spawn random particles
            Random rand = world().rand;
            for (int i = 0; i < 3 + rand.nextInt(20); i++)
            {
                Location v = addRandom(rand, 0.5);
                Pos vel = new Pos().addRandom(rand, 0.2);
                v.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + v.getBlockMetadata(), vel);
            }
        }
    }

    public boolean isSideSolid(ForgeDirection side)
    {
        return getBlock().isSideSolid(world(), xi(), yi(), zi(), side);
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof IPosWorld && this.world == ((IPosWorld) o).world() && ((IPosWorld) o).x() == x() && ((IPosWorld) o).y() == y() && ((IPosWorld) o).z() == z();
    }

    @Override
    public int compareTo(IPosWorld that)
    {
        if (world().provider.dimensionId < that.world().provider.dimensionId || x() < that.x() || y() < that.y() || z() < that.z())
        {
            return -1;
        }

        if (world().provider.dimensionId > that.world().provider.dimensionId || x() > that.x() || y() > that.y() || z() > that.z())
        {
            return 1;
        }

        return 0;
    }
}