package com.builtbroken.atomic;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.Random;

/** Creates a reactor explosion.
 *
 * @author Calclavia */
public class ReactorExplosion extends Explosion
{
    private Random explosionRAND = new Random();
    private World worldObj;

    public ReactorExplosion(World world, Entity par2Entity, double par3, double par5, double par7, float par9)
    {
        super(world, par2Entity, par3, par5, par7, par9);
        this.worldObj = world;
        this.isFlaming = true;
    }

    /** Does the second part of the explosion (sound, particles, drop spawn) */
    @Override
    public void doExplosionB(boolean par1)
    {
        super.doExplosionB(par1);

        Iterator iterator = this.affectedBlockPositions.iterator();

        while (iterator.hasNext())
        {
            ChunkPosition chunkposition = (ChunkPosition) iterator.next();
            int x = chunkposition.chunkPosX;
            int y = chunkposition.chunkPosY;
            int z = chunkposition.chunkPosZ;
            Block block = this.worldObj.getBlock(x, y, z);
            Block bellow = this.worldObj.getBlock(x, y - 1, z);

            if (block.isAir(worldObj, x, y, z) && bellow.isSideSolid(worldObj, x, y - 1, z, ForgeDirection.UP) && this.explosionRAND.nextInt(3) == 0)
            {
                this.worldObj.setBlock(x, y, z, Atomic.blockRadioactive);
            }
        }
    }
}
