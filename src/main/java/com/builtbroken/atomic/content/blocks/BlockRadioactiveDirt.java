package com.builtbroken.atomic.content.blocks;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

/**
 * Version of dirt that changes colors to give a green glow (entirely for gameplay reasons)
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/22/2018.
 */
public class BlockRadioactiveDirt extends Block
{
    //TODO change texture color to match radioactive levels
    //TODO turn to dirt if radioactive levels are zero
    public BlockRadioactiveDirt()
    {
        super(Material.ground);
        this.setBlockTextureName(AtomicScience.PREFIX + "radioactive/dirt");
        this.setTickRandomly(true);
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        return super.colorMultiplier(world, x, y, z); //TODO modify color based on radiation level
    }
}
