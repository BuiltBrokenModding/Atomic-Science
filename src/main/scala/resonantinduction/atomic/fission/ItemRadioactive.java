package resonantinduction.atomic.fission;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import resonant.lib.prefab.poison.PoisonRadiation;
import universalelectricity.api.vector.Vector3;

/** Radioactive Items */
public class ItemRadioactive extends Item
{
    public ItemRadioactive(int itemID)
    {
        super(itemID);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity entity, int par4, boolean par5)
    {
        if (entity instanceof EntityLivingBase)
        {
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), (EntityLivingBase) entity, 1);
        }
    }

}
