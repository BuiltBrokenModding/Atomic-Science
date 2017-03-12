package resonantinduction.atomic.process.sensor;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import resonant.lib.prefab.item.ItemBlockSaved;
import resonant.lib.utility.LanguageUtility;
import resonant.lib.utility.nbt.NBTUtility;
import universalelectricity.api.vector.Vector3;

/** Handheld thermometer */
public class ItemBlockThermometer extends ItemBlockSaved
{
    public static final int ENERGY_CONSUMPTION = 1000;

    public ItemBlockThermometer(int id)
    {
        super(id);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List par3List, boolean par4)
    {
        super.addInformation(itemStack, player, par3List, par4);
        Vector3 coord = getSavedCoord(itemStack);

        if (coord != null)
        {
            par3List.add("\uaa74" + LanguageUtility.getLocal("tooltip.trackingTemperature"));
            par3List.add("X: " + coord.intX() + ", Y: " + coord.intY() + ", Z: " + coord.intZ());
            // TODO: Add client side temperature.
        }
        else
        {
            par3List.add("\u00a74" + LanguageUtility.getLocal("tooltip.notTrackingTemperature"));
        }
    }

    public void setSavedCoords(ItemStack itemStack, Vector3 position)
    {
        NBTTagCompound nbt = NBTUtility.getNBTTagCompound(itemStack);

        if (position != null)
        {
            nbt.setCompoundTag("trackCoordinate", position.writeToNBT(new NBTTagCompound()));
        }
        else
        {
            nbt.removeTag("trackCoordinate");
        }
    }

    public Vector3 getSavedCoord(ItemStack itemStack)
    {
        NBTTagCompound nbt = NBTUtility.getNBTTagCompound(itemStack);

        if (nbt.hasKey("trackCoordinate"))
        {
            return new Vector3(nbt.getCompoundTag("trackCoordinate"));
        }

        return null;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        setSavedCoords(itemStack, null);

        if (!world.isRemote)
        {
            player.addChatMessage("Cleared tracking coordinate.");
        }
        return itemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (player.isSneaking())
        {
            if (!world.isRemote)
            {
                setSavedCoords(itemStack, new Vector3(x, y, z));
                player.addChatMessage("Tracking coordinate: " + x + ", " + y + ", " + z);
            }

            return true;
        }

        return super.onItemUse(itemStack, player, world, x, y, z, par7, par8, par9, par10);
    }
}
