package com.builtbroken.atomic.content.blocks;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Version of dirt that changes colors to give a green glow (entirely for gameplay reasons)
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/22/2018.
 */
public class BlockRadioactiveGrass extends BlockGrass
{
    public BlockRadioactiveGrass()
    {
        super();
        this.setBlockTextureName(AtomicScience.PREFIX + "radioactive/dirt");
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (!world.isRemote)
        {
            //TODO remove snow above block if hot enough

            if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2)
            {
                world.setBlock(x, y, z, ASBlocks.blockRadioactiveDirt);
            }
            else if (world.getBlockLightValue(x, y + 1, z) >= 9)
            {
                for (int l = 0; l < 8; ++l) //default is 4, we grow faster with more radiation
                {
                    int i1 = x + random.nextInt(3) - 1;
                    int j1 = y + random.nextInt(5) - 3;
                    int k1 = z + random.nextInt(3) - 1;

                    if (world.getBlockMetadata(i1, j1, k1) == 0
                            && world.getBlockLightValue(i1, j1 + 1, k1) >= 4
                            && world.getBlockLightOpacity(i1, j1 + 1, k1) <= 2)
                    {
                        if (world.getBlock(i1, j1, k1) == Blocks.dirt)
                        {
                            world.setBlock(i1, j1, k1, Blocks.grass);
                        }
                        else if (world.getBlock(i1, j1, k1) == ASBlocks.blockRadioactiveDirt)
                        {
                            world.setBlock(i1, j1, k1, ASBlocks.blockRadioactiveGrass);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return ASBlocks.blockRadioactiveGrass.getItemDropped(0, p_149650_2_, p_149650_3_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        return super.colorMultiplier(world, x, y, z); //TODO modify color based on radiation level
    }
}
