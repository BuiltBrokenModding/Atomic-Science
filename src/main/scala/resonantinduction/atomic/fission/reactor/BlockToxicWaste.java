package resonantinduction.atomic.fission.reactor;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;
import resonant.lib.prefab.poison.PoisonRadiation;
import universalelectricity.api.vector.Vector3;

public class BlockToxicWaste extends BlockFluidClassic
{
    public BlockToxicWaste(int id)
    {
        super(id, FluidRegistry.getFluid("toxicwaste"), Material.water);
        setTickRate(20);
    }

    @Override
    public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
    {
        super.randomDisplayTick(par1World, x, y, z, par5Random);

        if (par5Random.nextInt(100) == 0)
        {
            double d5 = x + par5Random.nextFloat();
            double d7 = y + this.maxY;
            double d6 = z + par5Random.nextFloat();
            par1World.spawnParticle("suspended", d5, d7, d6, 0.0D, 0.0D, 0.0D);
        }

        if (par5Random.nextInt(200) == 0)
        {
            par1World.playSound(x, y, z, "liquid.lava", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World par1World, int x, int y, int z, Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            entity.attackEntityFrom(DamageSource.wither, 3);
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(x, y, z), (EntityLivingBase) entity, 4);
        }
    }
}
