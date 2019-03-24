package com.builtbroken.atomic.content.machines.laser.emitter;

import com.builtbroken.atomic.config.content.ConfigContent;
import com.builtbroken.atomic.content.machines.laser.booster.TileEntityLaserBooster;
import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import com.builtbroken.atomic.lib.power.Battery;
import com.builtbroken.atomic.lib.timer.TickTimer;
import com.builtbroken.atomic.lib.timer.TickTimerConditional;
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

    public int boosterCount;

    public boolean sharingBoosters = false; //TODO change to broken state enum
    public boolean readyToFire = false;

    public final Battery battery = new Battery(() -> ConfigContent.LASER.ENERGY_PER_BOOSTER * boosterCount);
    private final TickTimer scanTimer = TickTimer.newSimple(20, tick -> scanForBoosters());

    private final TickTimer particleTickReadyToFire = TickTimerConditional.newSimple(3, tick -> spawnReadyToFireParticles())
            .setShouldTickFunction(() -> readyToFire);
    private final TickTimer particleTickBroken = TickTimerConditional.newSimple(3, tick -> spawnBrokenParticles())
            .setShouldTickFunction(() -> sharingBoosters);

    @Override
    protected void update(int ticks, boolean isClient)
    {
        if (!isClient)
        {
            scanTimer.tick();

            //Check if we are ready to fire
            readyToFire = battery.getEnergyStored() >= getCostToFire();

            sendDescPacket(); //TODO check for state change
        }
        else
        {
            particleTickReadyToFire.tick();
            particleTickBroken.tick();
        }
    }

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

    public void fire()
    {
        battery.extractEnergy(getCostToFire(), false);
        //TODO laser render
    }

    public int getCostToFire()
    {
        return ConfigContent.LASER.FIRING_COST * boosterCount;
    }

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
