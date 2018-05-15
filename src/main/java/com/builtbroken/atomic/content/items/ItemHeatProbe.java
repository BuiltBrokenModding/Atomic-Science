package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.jlib.data.science.units.UnitDisplay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Simple accessor of data in the map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/14/2018.
 */
public class ItemHeatProbe extends Item
{
    private final UnitDisplay unitDisplay = new UnitDisplay(UnitDisplay.Unit.JOULES, 0).symbol();
    private final double colorHeatMax = 1811; //Melting point of iron

    public ItemHeatProbe()
    {
        this.setUnlocalizedName(AtomicScience.PREFIX + "heat.probe");
        this.setTextureName(AtomicScience.PREFIX + "heat.probe");
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit)
    {
        if (!world.isRemote)
        {
            if (player.isSneaking())
            {
                ForgeDirection direction = ForgeDirection.getOrientation(side);
                x += direction.offsetX;
                y += direction.offsetY;
                z += direction.offsetZ;
            }
            double heat = MapHandler.THERMAL_MAP.getJoules(world, x, y, z);
            double env = MapHandler.THERMAL_MAP.getEnvironmentalJoules(world, x, y, z);
            double temp = MapHandler.THERMAL_MAP.getTemperature(world, x, y, z);
            //double heatDelta = temp - 300; //Difference from room temp

            int tempDisplay = (int) Math.floor(temp);

            player.addChatComponentMessage(new ChatComponentText("Heat: " + formatTemp(heat)
                            + " + " + formatTemp(env)
                            + "  Temp: " + tempDisplay + "k"
                            + "  HTM: " + formatTemp(heat + env)
                            + "/"
                            + formatTemp(ThermalHandler.energyCostToChangeStates(world, x, y, z))
                    )
            );

            int vap = ThermalHandler.getVaporRate(world, x, y, z);
            player.addChatComponentMessage(new ChatComponentText("Vap: " + vap));

        }
        return true;

    }

    protected String formatTemp(double heat)
    {
        unitDisplay.value = heat;
        return unitDisplay.toString();
    }

}
