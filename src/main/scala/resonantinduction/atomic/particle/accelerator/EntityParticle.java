package resonantinduction.atomic.particle.accelerator;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.ForgeDirection;
import resonant.api.IElectromagnet;
import resonant.lib.prefab.poison.PoisonRadiation;
import resonantinduction.atomic.Atomic;
import resonantinduction.core.Reference;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorHelper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

/** The particle entity used to determine the particle acceleration. */
public class EntityParticle extends Entity implements IEntityAdditionalSpawnData
{
    private static final int MOVE_TICK_RATE = 20;
    public Ticket updateTicket;
    public boolean didParticleCollide = false;
    private int lastTurn = 60;
    private Vector3 movementVector = new Vector3();
    private ForgeDirection movementDirection = ForgeDirection.NORTH;

    public EntityParticle(World par1World)
    {
        super(par1World);
        this.setSize(0.3f, 0.3f);
        this.renderDistanceWeight = 4f;
        this.ignoreFrustumCheck = true;
    }

    public EntityParticle(World world, Vector3 pos, Vector3 movementVec, ForgeDirection dir)
    {
        this(world);
        this.setPosition(pos.x, pos.y, pos.z);
        this.movementVector = movementVec;
        this.movementDirection = dir;
    }

    public static boolean canRenderAcceleratedParticle(World world, Vector3 pos)
    {
        if (pos.getBlockID(world) != 0)
        {
            return false;
        }

        for (int i = 0; i <= 1; i++)
        {
            ForgeDirection dir = ForgeDirection.getOrientation(i);

            if (!isElectromagnet(world, pos, dir))
            {
                return false;
            }
        }

        return true;
    }

    public static boolean isElectromagnet(World world, Vector3 position, ForgeDirection dir)
    {
        Vector3 checkPos = position.clone().translate(dir);
        TileEntity tile = checkPos.getTileEntity(world);

        if (tile instanceof IElectromagnet)
        {
            return ((IElectromagnet) tile).isRunning();

        }
        return false;
    }

    @Override
    public void writeSpawnData(ByteArrayDataOutput data)
    {
        data.writeInt(this.movementVector.intX());
        data.writeInt(this.movementVector.intY());
        data.writeInt(this.movementVector.intZ());
        data.writeInt(this.movementDirection.ordinal());
    }

    @Override
    public void readSpawnData(ByteArrayDataInput data)
    {
        this.movementVector.x = data.readInt();
        this.movementVector.y = data.readInt();
        this.movementVector.z = data.readInt();
        this.movementDirection = ForgeDirection.getOrientation(data.readInt());
    }

    @Override
    protected void entityInit()
    {
        this.dataWatcher.addObject(MOVE_TICK_RATE, (byte) 3);

        if (this.updateTicket == null)
        {
            this.updateTicket = ForgeChunkManager.requestTicket(Atomic.INSTANCE, this.worldObj, Type.ENTITY);
            this.updateTicket.getModData();
            this.updateTicket.bindEntity(this);
        }
    }

    @Override
    public void onUpdate()
    {
        /** Play sound fxs. */
        if (this.ticksExisted % 10 == 0)
        {
            this.worldObj.playSoundAtEntity(this, Reference.PREFIX + "accelerator", 1f, (float) (0.6f + (0.4 * (this.getParticleVelocity() / TileAccelerator.clientParticleVelocity))));
        }

        /** Check if the accelerator tile entity exists. */
        TileEntity t = this.worldObj.getBlockTileEntity(this.movementVector.intX(), this.movementVector.intY(), this.movementVector.intZ());

        if (!(t instanceof TileAccelerator))
        {
            setDead();
            return;
        }

        TileAccelerator tileEntity = (TileAccelerator) t;

        if (tileEntity.entityParticle == null)
        {
            tileEntity.entityParticle = this;
        }

        for (int x = -1; x < 1; x++)
        {
            for (int z = -1; z < 1; z++)
            {
                ForgeChunkManager.forceChunk(this.updateTicket, new ChunkCoordIntPair(((int) this.posX >> 4) + x, ((int) this.posZ >> 4) + z));
            }
        }

        try
        {
            if (!this.worldObj.isRemote)
            {
                this.dataWatcher.updateObject(MOVE_TICK_RATE, (byte) this.movementDirection.ordinal());
            }
            else
            {
                this.movementDirection = ForgeDirection.getOrientation(this.dataWatcher.getWatchableObjectByte(MOVE_TICK_RATE));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        double acceleration = 0.0006f;

        if ((!isElectromagnet(worldObj, new Vector3(this), movementDirection.getRotation(ForgeDirection.UP)) || !isElectromagnet(worldObj, new Vector3(this), movementDirection.getRotation(ForgeDirection.DOWN))) && this.lastTurn <= 0)
        {
            acceleration = turn();
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            this.lastTurn = 40;
        }

        this.lastTurn--;

        /** Checks if the current block condition allows the particle to exist */
        if (!canRenderAcceleratedParticle(this.worldObj, new Vector3(this)) || this.isCollided)
        {
            explode();
            return;
        }

        Vector3 dongLi = new Vector3();
        dongLi.translate(this.movementDirection);
        dongLi.scale(acceleration);
        this.motionX = Math.min(dongLi.x + this.motionX, TileAccelerator.clientParticleVelocity);
        this.motionY = Math.min(dongLi.y + this.motionY, TileAccelerator.clientParticleVelocity);
        this.motionZ = Math.min(dongLi.z + this.motionZ, TileAccelerator.clientParticleVelocity);
        this.isAirBorne = true;

        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;

        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        this.setPosition(this.posX, this.posY, this.posZ);

        if (this.lastTickPosX == this.posX && this.lastTickPosY == this.posY && this.lastTickPosZ == this.posZ && this.getParticleVelocity() <= 0 && this.lastTurn <= 0)
        {
            this.setDead();
        }

        this.worldObj.spawnParticle("portal", this.posX, this.posY, this.posZ, 0, 0, 0);
        this.worldObj.spawnParticle("largesmoke", this.posX, this.posY, this.posZ, 0, 0, 0);

        float radius = 0.5f;

        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(this.posX - radius, this.posY - radius, this.posZ - radius, this.posX + radius, this.posY + radius, this.posZ + radius);
        List<Entity> entitiesNearby = this.worldObj.getEntitiesWithinAABB(Entity.class, bounds);

        if (entitiesNearby.size() > 1)
        {
            this.explode();
            return;
        }
    }

    /** Try to move the particle left or right depending on which side is empty.
     * 
     * @return The new velocity. */
    private double turn()
    {
        ForgeDirection zuoFangXiang = VectorHelper.getOrientationFromSide(this.movementDirection, ForgeDirection.EAST);
        Vector3 zuoBian = new Vector3(this).floor();
        zuoBian.translate(zuoFangXiang);

        ForgeDirection youFangXiang = VectorHelper.getOrientationFromSide(this.movementDirection, ForgeDirection.WEST);
        Vector3 youBian = new Vector3(this).floor();
        youBian.translate(youFangXiang);

        if (zuoBian.getBlockID(this.worldObj) == 0)
        {
            this.movementDirection = zuoFangXiang;
        }
        else if (youBian.getBlockID(this.worldObj) == 0)
        {
            this.movementDirection = youFangXiang;
        }
        else
        {
            setDead();
            return 0;
        }

        this.setPosition(Math.floor(this.posX) + 0.5, Math.floor(this.posY) + 0.5, Math.floor(this.posZ) + 0.5);

        return this.getParticleVelocity() - (this.getParticleVelocity() / Math.min(Math.max(70 * this.getParticleVelocity(), 4), 30));

    }

    public void explode()
    {
        this.worldObj.playSoundAtEntity(this, Reference.PREFIX + "antimatter", 1.5f, 1f - this.worldObj.rand.nextFloat() * 0.3f);

        if (!this.worldObj.isRemote)
        {
            if (this.getParticleVelocity() > TileAccelerator.clientParticleVelocity / 2)
            {
                /* Check for nearby particles and if colliding with another one, drop strange matter. */
                float radius = 1f;

                AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(this.posX - radius, this.posY - radius, this.posZ - radius, this.posX + radius, this.posY + radius, this.posZ + radius);
                List<EntityParticle> entitiesNearby = this.worldObj.getEntitiesWithinAABB(EntityParticle.class, bounds);

                if (entitiesNearby.size() > 0)
                {
                    didParticleCollide = true;
                    setDead();
                    return;
                }
            }

            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.getParticleVelocity() * 2.5f, true);
        }

        float radius = 6;

        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(this.posX - radius, this.posY - radius, this.posZ - radius, this.posX + radius, this.posY + radius, this.posZ + radius);
        List<EntityLiving> livingNearby = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, bounds);

        for (EntityLiving entity : livingNearby)
        {
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), entity);
        }

        setDead();
    }

    public double getParticleVelocity()
    {
        return Math.abs(this.motionX) + Math.abs(this.motionY) + Math.abs(this.motionZ);
    }

    @Override
    public void applyEntityCollision(Entity par1Entity)
    {
        this.explode();
    }

    @Override
    public void setDead()
    {
        ForgeChunkManager.releaseTicket(this.updateTicket);
        super.setDead();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.movementVector = new Vector3(nbt.getCompoundTag("jiqi"));
        ForgeDirection.getOrientation(nbt.getByte("fangXiang"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setTag("jiqi", this.movementVector.writeToNBT(new NBTTagCompound()));
        nbt.setByte("fangXiang", (byte) this.movementDirection.ordinal());
    }

}
