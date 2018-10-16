package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.content.prefab.ItemPrefab;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.jlib.data.science.units.UnitDisplay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * Simple accessor of data in the map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/14/2018.
 */
public class ItemHeatProbe extends ItemPrefab
{
    private final UnitDisplay unitDisplay = new UnitDisplay(UnitDisplay.Unit.JOULES, 0).symbol();
    private final double colorHeatMax = 1811; //Melting point of iron

    public ItemHeatProbe()
    {
        super("heat_probe", "heat.probe");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if (player.isSneaking())
            {
                pos = pos.add(facing.getDirectionVec());
            }
            double heat = MapHandler.THERMAL_MAP.getJoules(world, pos);
            double env = MapHandler.THERMAL_MAP.getEnvironmentalJoules(world, pos);
            double temp = MapHandler.THERMAL_MAP.getTemperature(world, pos);
            //double heatDelta = temp - 300; //Difference from room temp

            int tempDisplay = (int) Math.floor(temp);

            player.sendMessage(new TextComponentString("Heat: " + formatTemp(heat)
                            + " + " + formatTemp(env)
                            + "  Temp: " + tempDisplay + "k"
                            + "  HTM: " + formatTemp(heat + env)
                            + "/"
                            + formatTemp(ThermalHandler.energyCostToChangeStates(world, pos))
                    )
            );

            int vap = ThermalHandler.getVaporRate(world, pos);
            player.sendMessage(new TextComponentString("Vap: " + vap));

        }
        return EnumActionResult.SUCCESS;

    }

    protected String formatTemp(double heat)
    {
        unitDisplay.value = heat;
        return unitDisplay.toString();
    }

}
