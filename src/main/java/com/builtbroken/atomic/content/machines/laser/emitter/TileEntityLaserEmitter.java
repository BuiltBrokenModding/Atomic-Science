package com.builtbroken.atomic.content.machines.laser.emitter;

import com.builtbroken.atomic.config.content.ConfigContent;
import com.builtbroken.atomic.content.machines.accelerator.gun.TileEntityAcceleratorGun;
import com.builtbroken.atomic.content.machines.container.TileEntityItemContainer;
import com.builtbroken.atomic.content.machines.laser.booster.TileEntityLaserBooster;
import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import com.builtbroken.atomic.lib.power.Battery;
import com.builtbroken.atomic.lib.timer.TickTimerConditional;
import com.builtbroken.atomic.lib.timer.TickTimerTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2019.
 */
public class TileEntityLaserEmitter extends TileEntityMachine
{
    public static final String NBT_BOOSTER_COUNT = "booster_count";
    public static final String NBT_ENERGY = "energy";

    /** Number of boosters we have connected */
    public int boosterCount;

    /** Time left to cooldown after firing */
    public int cooldown = 0;

    /** Did we detect a booster being shared by another laser */
    public boolean sharingBoosters = false; //TODO change to broken state enum so we can track different errors

    /** Are we ready to fire, As in do we have enough energy */
    public boolean readyToFire = false;

    /** Should the laser fire */
    public boolean shouldFire = false;

    /** Is an external machine asking us to fire */
    public boolean fireOverride = false;

    public boolean hasRedstone = false;

    /** Battery of the laser */
    public final Battery battery = new Battery(() -> ConfigContent.LASER.ENERGY_PER_BOOSTER * boosterCount);

    public TileEntityLaserEmitter()
    {
        tickServer.add(TickTimerTileEntity.newSimple(20, tick -> scanForBoosters()));
        tickServer.add(TickTimerTileEntity.newSimple(3, tick -> checkForRedstone()));
        tickServer.add(TickTimerTileEntity.newSimple(3, tick -> checkMachineState()));
        tickServer.add(TickTimerConditional.newTrigger(() -> ConfigContent.LASER.LASER_FIRING_DELAY, tick -> fire(), () -> readyToFire && shouldFire));

        //Client only TODO find a way to init only if client
        tickClient.add(TickTimerTileEntity.newConditional(3, tick -> spawnReadyToFireParticles(), () -> readyToFire));
        tickClient.add(TickTimerTileEntity.newConditional(3, tick -> spawnBrokenParticles(), () -> sharingBoosters));
    }

    //Spawn particles to show the machine is ready to fire
    private void spawnReadyToFireParticles()
    {
        final EnumFacing facing = getDirection();
        for (int i = 0; i < 2; i++)
        {
            world.spawnParticle(EnumParticleTypes.REDSTONE,
                    getPos().getX() + 0.5 + 0.5 * facing.getXOffset() + Math.random() * 0.05,
                    getPos().getY() + 0.5 + 0.5 * facing.getYOffset() + Math.random() * 0.05,
                    getPos().getZ() + 0.5 + 0.5 * facing.getZOffset() + Math.random() * 0.05,
                    0, 0, 0);
        }
    }

    private void spawnBrokenParticles()
    {
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5,
                Math.random() * 0.3, Math.random() * 0.3, Math.random() * 0.3);
    }

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        dataList.add(readyToFire);
        dataList.add(sharingBoosters);
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        readyToFire = buf.readBoolean();
        sharingBoosters = buf.readBoolean();
    }

    //Check for redstone
    protected boolean checkForRedstone()
    {
        return hasRedstone = world.isBlockPowered(getPos());
    }

    //Check state of the machine
    protected void checkMachineState()
    {
        boolean prevReadyToFire = readyToFire; //TODO build a state change var with listener hooks to check for change
        boolean prevShouldFire = shouldFire;

        readyToFire = battery.getEnergyStored() >= getCostToFire();
        shouldFire = hasRedstone || fireOverride;

        if (prevReadyToFire != readyToFire || prevShouldFire != shouldFire)
        {
            syncClientNextTick();
        }
    }

    /**
     * Called to trigger the laser to fire
     */
    public boolean triggerFire()
    {
        fireOverride = true;
        return true;
    }

    /**
     * Fires the laser without a delay
     * <p>
     * Use {@link #triggerFire()} to fire the laser with its normal
     * delay and warm up.
     * <p>
     * Normally this is controlled by internal timers. So
     * only directly call from outside the laser for special needs.
     */
    public void fire()
    {
        //Reset
        readyToFire = false;
        shouldFire = false;
        fireOverride = false;

        //Eat energy
        battery.extractEnergy(getCostToFire(), false);
        doFire();

        //Mark cooldown
        cooldown = ConfigContent.LASER.LASER_COOLDOWN;

        //Sync state so particles turn off
        syncClientNextTick();
    }

    protected void doFire()
    {
        //TODO trigger client side laser

        final EnumFacing facing = getDirection();

        //Get tile in from of laser
        TileEntity tileEntity = world.getTileEntity(getPos().offset(facing));

        if (tileEntity instanceof TileEntityItemContainer) //TODO turn into capability so any machine can action with laser in front
        {
            //Try to find accelerator end cap
            TileEntity tileEntity2 = world.getTileEntity(getPos().offset(facing, 2));

            if (tileEntity2 instanceof TileEntityAcceleratorGun) //TODO turn into capability so any machine can action with laser in front
            {
                ((TileEntityAcceleratorGun) tileEntity2).onLaserFiredInto((TileEntityItemContainer) tileEntity, this);
            }
        }
        else
        {
            //TODO raytrace to cause damage to open air entities and find block we can interact with
        }
    }

    /**
     * Cost in FE to use the laser
     *
     * @return forge energy cost
     */
    public int getCostToFire()
    {
        return ConfigContent.LASER.FIRING_COST * boosterCount;
    }

    //Check for boosters behind the laser
    private void scanForBoosters()
    {
        //Reset
        boosterCount = 0;
        sharingBoosters = false;

        //Get direction
        final EnumFacing facing = getDirection().getOpposite();

        //Search for boosters
        BlockPos pos = getPos();
        for (int i = 0; i < ConfigContent.LASER.BOOSTER_MAX; i++)
        {
            //Offset
            pos = pos.offset(facing);

            //Only work on loaded chunks
            if (!world.isBlockLoaded(pos))
            {
                break;
            }

            //Get tile
            final TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityLaserBooster) //TODO change to capability booster
            {
                final EnumFacing boosterFacing = ((TileEntityLaserBooster) tileEntity).getDirection();

                //Only allow boosters facing in our same axis
                if (boosterFacing != facing && boosterFacing != facing.getOpposite())
                {
                    break;
                }

                //Count booster
                boosterCount++;

                //Error if sharing
                if (((TileEntityLaserBooster) tileEntity).host != null && ((TileEntityLaserBooster) tileEntity).host != this)
                {
                    sharingBoosters = true;
                    break;
                }

                //Set host
                ((TileEntityLaserBooster) tileEntity).host = this;
            }
            //Exit condition
            else
            {
                break;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        boosterCount = compound.getInteger(NBT_BOOSTER_COUNT);
        battery.setEnergy(compound.getInteger(NBT_ENERGY));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger(NBT_BOOSTER_COUNT, boosterCount);
        compound.setInteger(NBT_ENERGY, battery.getEnergyStored());
        return super.writeToNBT(compound);
    }
}
