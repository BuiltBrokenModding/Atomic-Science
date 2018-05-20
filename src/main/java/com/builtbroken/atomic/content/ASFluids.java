package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
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
    STEAM("steam", "steam", false),

    //Waste fluid from reactor
    REACTOR_WASTE("toxic_waste", true),
    //Waste sludge from chemical extractor (trace minerals)
    LIQUID_MINERAL_WASTE(AtomicScience.PREFIX + "mineral_waste", "waste_mineral", false),
    //Waste water from boiler (trace minerals)
    CONTAMINATED_MINERAL_WATER(AtomicScience.PREFIX + "contaminated_mineral_water", "contaminated_mineral_water", false),
    //Waste water from cleaning items (contains trace radioactive dust)
    CONTAMINATED_WATER(AtomicScience.PREFIX + "contaminated_water", "contaminated_water", false),
    //Gas version of uranium
    URANIUM_HEXAFLOURIDE(AtomicScience.PREFIX + "uranium_hexafluoride", "waste_mineral", false);

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
