package com.builtbroken.atomic.content.fluid;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASFluids;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidFinite;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/16/2018.
 */
public class BlockASFluid extends BlockFluidFinite
{
    public final ASFluids fluidEnum;
    public BlockASFluid(ASFluids fluidEnum)
    {
        super(fluidEnum.fluid, Material.water);
        this.fluidEnum = fluidEnum;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        IIcon stillIcon = reg.registerIcon(AtomicScience.PREFIX + "fluids/" + fluidEnum.texture_still);
        IIcon flowingIcon = reg.registerIcon(AtomicScience.PREFIX + "fluids/" + fluidEnum.texture_flow);
        fluidEnum.fluid.setIcons(stillIcon, flowingIcon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (getFluid().getIcon() != null)
        {
            return getFluid().getIcon();
        }
        return Blocks.water.getIcon(side, meta);
    }
}
