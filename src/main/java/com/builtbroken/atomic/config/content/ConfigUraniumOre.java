package com.builtbroken.atomic.config.content;

import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/24/2018.
 */
public class ConfigUraniumOre
{
    @Config.Name("enable")
    @Config.Comment("Should world generation be enabled? True to allow ore to spawn; False to disable")
    public boolean ENABLE_URANIUM_ORE = true;

    @Config.Name("max_y")
    @Config.Comment("Highest y level (height) that ore can spawn")
    @Config.RangeInt(min = 1, max = 254)
    public int URANIUM_ORE_MIN_Y = 10;

    @Config.Name("min_y")
    @Config.Comment("Lowest y level (height) that ore can spawn")
    @Config.RangeInt(min = 0, max = 255)
    public int URANIUM_ORE_MAX_Y = 40;

    @Config.Name("chunk_count")
    @Config.Comment("Max amount of ore to spawn in each chunk. Actual count per chunk is a mix of randomization and conditions of the chunk itself")
    @Config.RangeInt(min = 1)
    public int URANIUM_ORE_COUNT = 20;

    @Config.Name("branch_size")
    @Config.Comment("Amount of ore to generate per branch")
    @Config.RangeInt(min = 1)
    public int URANIUM_ORE_BRANCH_SIZE = 3;

    @Config.Name("harvest_level")
    @Config.Comment("Tool level needed to mine the ore \n" +
            "*     Wood:    0\n" +
            "*     Stone:   1\n" +
            "*     Iron:    2\n" +
            "*     Diamond: 3\n" +
            "*     Gold:    0")
    @Config.RangeInt(min = 1)
    public int URANIUM_ORE_HARVEST_LEVEL = 1;
}
