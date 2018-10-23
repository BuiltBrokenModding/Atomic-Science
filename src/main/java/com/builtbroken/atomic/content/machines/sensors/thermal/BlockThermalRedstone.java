package com.builtbroken.atomic.content.machines.sensors.thermal;

import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.content.prefab.BlockPrefab;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/23/2018.
 */
@Mod.EventBusSubscriber
public class BlockThermalRedstone extends BlockPrefab
{
    protected BlockThermalRedstone()
    {
        super(Material.IRON);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return null;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHeatChanged(MapSystemEvent.OnValueChanged event)
    {
        final World world = event.world();
        if (world != null && !world.isRemote && event.type == DataMapType.THERMAL && world.isBlockLoaded(event.getPos()))
        {

        }
    }

}
