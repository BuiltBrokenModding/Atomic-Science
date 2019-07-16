package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.content.prefab.ItemPrefab;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.lib.vapor.VaporHandler;
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
 * <p>
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 5/14/2018.
 */
public class ItemHeatProbe extends ItemPrefab
{
    private static final UnitDisplay unitDisplay = new UnitDisplay(UnitDisplay.Unit.JOULES, 0).symbol();
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
                pos = pos.offset(facing);
            }

            //Display Heat
            int heat = MapHandler.THERMAL_MAP.getStoredHeat(world, pos);
            player.sendMessage(new TextComponentString("Heat: " + formatTemp(heat)));

            //Heat movement rate
            float transferRate = ThermalHandler.getTransferRate(world.getBlockState(pos));
            player.sendMessage(new TextComponentString(String.format("Transfer Rate: %.2f", transferRate)));

            //Display Vapor rate
            int vap = VaporHandler.getVaporRate(world, pos);
            player.sendMessage(new TextComponentString("Vap: " + vap));

        }
        return EnumActionResult.SUCCESS;

    }

    public static String formatTemp(double heat) //TODO move to helper
    {
        unitDisplay.value = heat;
        return unitDisplay.toString();
    }

}
