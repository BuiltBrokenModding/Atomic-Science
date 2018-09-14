package com.builtbroken.atomic.content.fluid;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by Dark on 8/8/2015.
 */
public class BlockSimpleFluid extends BlockFluidClassic
{
    public BlockSimpleFluid(Fluid fluid, String blockName)
    {
        super(fluid, Material.WATER);
        setTranslationKey(AtomicScience.DOMAIN + ":" + blockName);
    }
}
