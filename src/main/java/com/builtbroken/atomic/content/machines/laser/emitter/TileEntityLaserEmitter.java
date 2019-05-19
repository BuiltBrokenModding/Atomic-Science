package com.builtbroken.atomic.content.machines.laser.emitter;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.config.content.ConfigContent;
import com.builtbroken.atomic.content.machines.accelerator.gun.TileEntityAcceleratorGun;
import com.builtbroken.atomic.content.machines.container.item.TileEntityItemContainer;
import com.builtbroken.atomic.content.machines.laser.booster.TileEntityLaserBooster;
import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import com.builtbroken.atomic.lib.power.Battery;
import com.builtbroken.atomic.lib.timer.TickTimerConditional;
import com.builtbroken.atomic.lib.timer.TickTimerTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
    public static int MAX_RAY_DISTANCE = 5;

    public static final String NBT_BOOSTER_COUNT = "booster_count";
    public static final String NBT_ENERGY = "energy";
    public static final String NBT_LASER_MODE = "laser_mode";

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

    public boolean doFireLaser = false;

    /** Battery of the laser */
    public final Battery battery = new Battery(() -> ConfigContent.LASER.ENERGY_PER_BOOSTER * boosterCount);

    /** Current mode of the laser */
    private LaserModes _laserMode = LaserModes.NORMAL;

    public TileEntityLaserEmitter()
    {
        //Machine state checks
        tickServer.add(TickTimerTileEntity.newSimple(20, tick -> scanForBoosters()));
        tickServer.add(TickTimerTileEntity.newSimple(3, tick -> checkForRedstone()));
        tickServer.add(TickTimerTileEntity.newSimple(tick -> checkMachineState()));

        //Laser fire checks
        tickServer.add(TickTimerConditional.newTrigger(() -> ConfigContent.LASER.LASER_FIRING_DELAY, tick -> fire(),
                () -> getLaserMode() == LaserModes.NORMAL && canFireLaser()));
        tickServer.add(TickTimerTileEntity.newConditional(1, tick -> fire(),
                () -> getLaserMode() == LaserModes.FIELD && canFireLaser()));

        //Client only TODO find a way to init only if client
        tickClient.add(TickTimerTileEntity.newConditional(3, tick -> spawnReadyToFireParticles(), () -> readyToFire));
        tickClient.add(TickTimerTileEntity.newConditional(3, tick -> spawnBrokenParticles(), () -> sharingBoosters));
        tickClient.add(TickTimerTileEntity.newConditional(1, tick -> spawnLaser(), () -> doFireLaser || getLaserMode() == LaserModes.FIELD && readyToFire && shouldFire));
        //TODO consider firing laser for several ticks instead of 1 tick for normal
    }

    public boolean canFireLaser()
    {
        return readyToFire && shouldFire;
    }

    //Spawn particles to show the machine is ready to fire
    private void spawnReadyToFireParticles()
    {
        final EnumFacing facing = getDirection();
        AtomicScience.sideProxy.spawnParticle(getLaserMode().warmedUpParticle,
                getPos().getX() + 0.5 + 0.5 * facing.getXOffset(),
                getPos().getY() + 0.5 + 0.5 * facing.getYOffset(),
                getPos().getZ() + 0.5 + 0.5 * facing.getZOffset(),
                0, 0, 0);
    }

    private void spawnLaser()
    {
        //Reset
        doFireLaser = _laserMode != LaserModes.NORMAL;

        //Get impact point
        final EnumFacing facing = getDirection();

        final double sx = getPos().getX() + 0.5 + 0.5 * facing.getXOffset();
        final double sy = getPos().getY() + 0.5 + 0.5 * facing.getYOffset();
        final double sz = getPos().getZ() + 0.5 + 0.5 * facing.getZOffset();

        final BlockPos impactPos = getLaserImpactPos();

        double x, y, z;
        if(impactPos != null)
        {
            x = impactPos.getX() + 0.5;
            y = impactPos.getY() + 0.5;
            z = impactPos.getZ() + 0.5;
        }
        else
        {
            x = sx + MAX_RAY_DISTANCE * facing.getXOffset();
            y = sy + MAX_RAY_DISTANCE * facing.getYOffset();
            z = sz + MAX_RAY_DISTANCE * facing.getZOffset();
        }

        AtomicScience.sideProxy.spawnParticle(getLaserMode().fireParticle,
                sx, sy, sz,
                x, y, z
        );
    }

    private BlockPos getLaserImpactPos()
    {
        //Get impact point
        final EnumFacing facing = getDirection();

        //Loop until end of ray check distance
        for (int step = 0; step < MAX_RAY_DISTANCE; step++)
        {
            final BlockPos pos = getPos().offset(facing, step + 1);
            if (world.isBlockLoaded(pos))
            {
                final IBlockState blockState = world.getBlockState(pos);

                //Check if laser can move through block
                if (!canLaserPassThrough(blockState, pos))
                {
                    return pos;
                }
            }
            else
            {
                return pos;
            }
        }
        return null;
    }

    private boolean canLaserPassThrough(IBlockState blockState, BlockPos pos)
    {
        Block block = blockState.getBlock();
        if (block == Blocks.GLASS || block == Blocks.GLASS_PANE || block == Blocks.IRON_BARS)
        {
            return true;
        }
        //TODO damage plants
        return blockState.getBlock().isAir(blockState, world, pos);
    }

    private void spawnBrokenParticles()
    {
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5,
                Math.random() * 0.3, Math.random() * 0.3, Math.random() * 0.3);
    }

    @Override
    protected void sendDescPacket()
    {
        super.sendDescPacket();

        //Reset client only conditionals
        doFireLaser = false;
    }

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        dataList.add(readyToFire);
        dataList.add(sharingBoosters);
        dataList.add(doFireLaser);
        dataList.add((byte) getLaserMode().ordinal());
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        readyToFire = buf.readBoolean();
        sharingBoosters = buf.readBoolean();
        doFireLaser = buf.readBoolean();
        _laserMode = LaserModes.get(buf.readByte());
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

        readyToFire = battery.getEnergyStored() >= getCostToFire() && boosterCount > 0;
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

        //Client toggle
        doFireLaser = true; //TODO fix so we are not sending a packet per tick for field mode

        //Mark cooldown
        cooldown = getLaserMode() == LaserModes.FIELD ? 0 : ConfigContent.LASER.LASER_COOLDOWN;

        //Sync state so particles turn off
        syncClientNextTick();
    }

    protected void doFire()
    {
        //TODO trigger client side laser

        final EnumFacing facing = getDirection();

        if (getLaserMode() == LaserModes.NORMAL)
        {
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
        else
        {
            TileEntity tileEntity;
            int steps = 0;
            do
            {
                tileEntity = world.getTileEntity(getPos().offset(facing, ++steps));
            } while (tileEntity == null && steps < MAX_RAY_DISTANCE);

            if (tileEntity instanceof TileEntityItemContainer)
            {
                //Calculate power to send
                int powerToSend = ConfigContent.LASER.FIELD_TRANSFER_POWER * boosterCount;

                //Get actual send
                powerToSend = battery.extractEnergy(powerToSend, true);

                //Send power
                int taken = ((TileEntityItemContainer) tileEntity).internalBattery.receiveEnergy(powerToSend, false);

                //Extract what was taken
                battery.extractEnergy(taken, false);
            }
        }
    }

    /**
     * Cost in FE to use the laser
     *
     * @return forge energy cost
     */
    public int getCostToFire()
    {
        return getLaserMode().getEnergyCost() * boosterCount;
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
        _laserMode = LaserModes.get(compound.getInteger(NBT_LASER_MODE));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger(NBT_BOOSTER_COUNT, boosterCount);
        compound.setInteger(NBT_ENERGY, battery.getEnergyStored());
        compound.setInteger(NBT_LASER_MODE, getLaserMode().ordinal());
        return super.writeToNBT(compound);
    }

    public LaserModes getLaserMode()
    {
        if (_laserMode == null)
        {
            _laserMode = LaserModes.NORMAL;
        }
        return _laserMode;
    }

    public void setLaserMode(LaserModes laserMode)
    {
        syncClientNextTick();
        this._laserMode = laserMode;
    }
}
