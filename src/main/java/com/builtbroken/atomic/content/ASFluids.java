package com.builtbroken.atomic.content;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Enum of fluid created by the mod
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public enum ASFluids
{
    ANTIMATTER("antimatter", false),
    STRANGE_MATTER("strange_matter", false),
    DEUTERIUM("deuterium", false),
    TOXIC_WASTE("toxic_waste", true),
    STEAM("steam", false);
    //PLASMA("steam", false) TODO see if there is a use for plasma as a fluid

    public final String id;
    public final String texture;
    public final boolean makeBlock;

    public Fluid fluid;

    ASFluids(String id, boolean makeBlock)
    {
        this(id, null, makeBlock);
    }

    ASFluids(String id, String texture, boolean makeBlock)
    {
        this.id = id;
        this.texture = texture;
        this.makeBlock = makeBlock;
    }

    public static void register()
    {
        for (ASFluids fluid : values())
        {
            fluid.fluid = new Fluid(fluid.id);
            if (!FluidRegistry.registerFluid(fluid.fluid))
            {
                fluid.fluid = FluidRegistry.getFluid(fluid.id);
            }
        }
    }
}
