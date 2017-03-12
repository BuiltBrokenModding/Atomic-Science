package resonantinduction.atomic.fission;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import resonant.lib.prefab.block.BlockRadioactive;
import resonantinduction.core.Reference;
import resonantinduction.core.Settings;
import resonantinduction.core.TabRI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Uranium ore block */
public class BlockUraniumOre extends BlockRadioactive
{
    public BlockUraniumOre(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Reference.PREFIX + "oreUranium");
        this.setStepSound(soundStoneFootstep);
        this.setCreativeTab(TabRI.DEFAULT);
        this.setHardness(2f);
        this.setTextureName(Reference.PREFIX + "oreUranium");

        this.isRandomlyRadioactive = Settings.allowRadioactiveOres;
        this.canWalkPoison = Settings.allowRadioactiveOres;
        this.canSpread = false;
        this.radius = 1f;
        this.amplifier = 0;
        this.spawnParticle = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random par5Random)
    {
        if (Settings.allowRadioactiveOres)
        {
            super.randomDisplayTick(world, x, y, z, par5Random);
        }
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        return this.blockIcon;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }
}
