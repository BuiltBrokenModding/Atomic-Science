package com.builtbroken.atomic.config;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/24/2018.
 */
public class ConfigOre extends ContentProxy
{
    private static final String CATEGORY_URANIUM = "uranium_ore";

    public static boolean ENABLE_URANIUM_ORE = true;
    public static int URANIUM_ORE_MIN_Y = 10;
    public static int URANIUM_ORE_MAX_Y = 40;
    public static int URANIUM_ORE_COUNT = 20;
    public static int URANIUM_ORE_BRANCH_SIZE = 3;
    public static int URANIUM_ORE_HARVEST_LEVEL = 1;

    public ConfigOre()
    {
        super("config.ore");
    }

    @Override
    public void preInit()
    {
        Configuration configuration = new Configuration(new File(AtomicScience.configFolder, "Ore.cfg"), AtomicScience.VERSION);
        configuration.load();
        ENABLE_URANIUM_ORE = configuration.getBoolean("enable", CATEGORY_URANIUM, ENABLE_URANIUM_ORE, "Should world generation be enabled? True to allow ore to spawn; False to disable");
        URANIUM_ORE_MIN_Y = configuration.getInt("min_y", CATEGORY_URANIUM, URANIUM_ORE_MIN_Y, 0, 255, "Lowest y level (height) that ore can spawn");
        URANIUM_ORE_MAX_Y = configuration.getInt("max_y", CATEGORY_URANIUM, URANIUM_ORE_MAX_Y, 0, 255, "Highest y level (height) that ore can spawn");
        URANIUM_ORE_COUNT = configuration.getInt("chunk_count", CATEGORY_URANIUM, URANIUM_ORE_MAX_Y, 1, 100, "Max amount of ore to spawn in each chunk. " +
                "Actual count per chunk is a mix of randomization and conditions of the chunk itself.");
        URANIUM_ORE_BRANCH_SIZE = configuration.getInt("branch_size", CATEGORY_URANIUM, URANIUM_ORE_BRANCH_SIZE, 0, 100, "Amount of ore to generate per branch");
        URANIUM_ORE_HARVEST_LEVEL = configuration.getInt("harvest_level", CATEGORY_URANIUM, URANIUM_ORE_HARVEST_LEVEL, 0, 255, "Tool level needed to mine the ore \n" +
                "*     Wood:    0\n" +
                "*     Stone:   1\n" +
                "*     Iron:    2\n" +
                "*     Diamond: 3\n" +
                "*     Gold:    0");
        configuration.save();
    }
}
