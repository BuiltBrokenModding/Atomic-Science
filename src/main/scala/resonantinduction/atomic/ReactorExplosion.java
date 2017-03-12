package resonantinduction.atomic;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

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
            int x = chunkposition.x;
            int y = chunkposition.y;
            int z = chunkposition.z;
            int id = this.worldObj.getBlockId(x, y, z);
            int i1 = this.worldObj.getBlockId(x, y - 1, z);

            if (id == 0 && Block.opaqueCubeLookup[i1] && this.explosionRAND.nextInt(3) == 0)
            {
                this.worldObj.setBlock(x, y, z, Atomic.blockRadioactive.blockID);
            }
        }
    }
}
