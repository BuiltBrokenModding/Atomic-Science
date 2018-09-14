package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.File;

/**
 * Enum of fluid created by the mod
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public enum ASFluids
{
    //NOTE: do not change enum names, this will change registry values for fluid blocks
    ANTIMATTER(AtomicScience.PREFIX + "antimatter", "plasma", false), //TODO get texture
    STRANGE_MATTER(AtomicScience.PREFIX + "strange_matter", "plasma", false), //TODO get texture
    DEUTERIUM(AtomicScience.PREFIX + "deuterium", "tritium", false), //TODO get texture, add gas block https://en.wikipedia.org/wiki/Deuterium
    STEAM("steam", "steam", true), //TODO make custom gas block

    //Waste fluid from reactor (yellow color as it has oxidized due to heat + water)
    REACTOR_WASTE(AtomicScience.PREFIX + "reactor_waste", "toxic/reactor/still", "toxic/reactor/flow", true),
    //Waste sludge from chemical extractor (trace minerals)
    LIQUID_MINERAL_WASTE(AtomicScience.PREFIX + "mineral_waste", "toxic/mineral/still", "toxic/mineral/flow", true),
    //Waste water from boiler (trace minerals)
    CONTAMINATED_MINERAL_WATER(AtomicScience.PREFIX + "contaminated_mineral_water", "water/mineral/still", "water/mineral/flow", true),
    //Waste water from cleaning items (contains trace radioactive dust)
    CONTAMINATED_WATER(AtomicScience.PREFIX + "contaminated_water", "water/contaminated/still", "water/contaminated/flow", true),
    //Gas version of uranium
    URANIUM_HEXAFLOURIDE(AtomicScience.PREFIX + "uranium_hexafluoride", "uranium.hexafluoride", false); //TODO make custom gas block

    public final String id;
    public final String texture_still;
    public final String texture_flow;
    public final boolean makeBlock;

    public Fluid fluid;

    ASFluids(String id, boolean makeBlock)
    {
        this(id, null, null, makeBlock);
    }

    ASFluids(String id, String texture, boolean makeBlock)
    {
        this(id, texture, texture, makeBlock);
    }

    ASFluids(String id, String texture_still, String texture_flow, boolean makeBlock)
    {
        this.id = id;
        this.texture_still = texture_still;
        this.texture_flow = texture_flow;
        this.makeBlock = makeBlock;
    }

    protected void register(Configuration configuration)
    {
        if (fluid == null)
        {
            if (texture_still != null)
            {
                fluid = new Fluid(id, new ResourceLocation(AtomicScience.DOMAIN, "blocks/fluids/" + texture_still), new ResourceLocation(AtomicScience.DOMAIN, "blocks/fluids/" + texture_flow)); //TODO handle color
            }
            else
            {
                fluid = new Fluid(id, FluidRegistry.WATER.getStill(), FluidRegistry.WATER.getFlowing());
            }
        }
        if (!id.startsWith(AtomicScience.PREFIX))
        {
            fluid.setUnlocalizedName(AtomicScience.PREFIX + id);
        }
        if (!FluidRegistry.registerFluid(fluid))
        {
            fluid = FluidRegistry.getFluid(id);
        }
    }

    public static class Proxy extends ContentProxy
    {
        public Proxy()
        {
            super("fluids");
        }

        @Override
        public void preInit()
        {
            Configuration configuration = new Configuration(new File(AtomicScience.configFolder, "Fluids.cfg"), AtomicScience.VERSION);
            configuration.load();
            for (ASFluids fluid : values())
            {
                fluid.register(configuration);
            }
            configuration.save();

            STEAM.fluid.setGaseous(true);
            DEUTERIUM.fluid.setGaseous(true);
            URANIUM_HEXAFLOURIDE.fluid.setGaseous(true);
        }
    }
}
