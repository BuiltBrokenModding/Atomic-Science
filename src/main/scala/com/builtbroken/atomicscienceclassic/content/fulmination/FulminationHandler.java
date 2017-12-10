package com.builtbroken.atomicscienceclassic.content.fulmination;

import com.builtbroken.atomicscienceclassic.Atomic;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.explosion.ExplosionEvent.DoExplosionEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/** Atomic Science Event Handling. */
public class FulminationHandler
{
    public static final FulminationHandler INSTANCE = new FulminationHandler();

    public static final List<TileFulmination> list = new ArrayList<TileFulmination>();

    public void register(TileFulmination tileEntity)
    {
        if (!list.contains(tileEntity))
        {
            list.add(tileEntity);
        }
    }

    public void unregister(TileFulmination tileEntity)
    {
        list.remove(tileEntity);
    }

    @SubscribeEvent
    public void BaoZha(DoExplosionEvent evt)
    {
        if (evt.iExplosion != null)
        {
            if (evt.iExplosion.getRadius() > 0 && evt.iExplosion.getEnergy() > 0)
            {
                HashSet<TileFulmination> avaliableGenerators = new HashSet<TileFulmination>();

                for (TileFulmination tileEntity : FulminationHandler.list)
                {
                    if (tileEntity != null)
                    {
                        if (!tileEntity.isInvalid())
                        {
                            Pos tileDiDian = new Pos((TileEntity) tileEntity);
                            tileDiDian = tileDiDian.add(0.5f);
                            double juLi = tileDiDian.distance(new Pos(evt.x, evt.y, evt.z));

                            if (juLi <= evt.iExplosion.getRadius() && juLi > 0)
                            {
                                float miDu = evt.world.getBlockDensity(Vec3.createVectorHelper(evt.x, evt.y, evt.z), Atomic.blockFulmination.getCollisionBoundingBoxFromPool(evt.world, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));

                                if (miDu < 1)
                                {
                                    avaliableGenerators.add(tileEntity);
                                }
                            }
                        }
                    }
                }

                final float totalEnergy = evt.iExplosion.getEnergy();
                final float maxEnergyPerGenerator = totalEnergy / avaliableGenerators.size();

                for (TileFulmination tileEntity : avaliableGenerators)
                {
                    float density = evt.world.getBlockDensity(Vec3.createVectorHelper(evt.x, evt.y, evt.z), Atomic.blockFulmination.getCollisionBoundingBoxFromPool(evt.world, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
                    double juLi = new Pos((TileEntity) tileEntity).distance(new Pos(evt.x, evt.y, evt.z));

                    int energy = (int) Math.min(maxEnergyPerGenerator, maxEnergyPerGenerator / (juLi / evt.iExplosion.getRadius()));
                    energy = (int) Math.max((1 - density) * energy, 0); //TODO redo math to fix rounding errors
                    tileEntity.getEnergyBuffer(ForgeDirection.UNKNOWN).addEnergyToStorage(energy, true);
                }
            }
        }
    }
}
