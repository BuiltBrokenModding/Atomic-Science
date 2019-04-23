package com.builtbroken.atomic.content.armor;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.armor.IAntiPoisonArmor;
import com.builtbroken.atomic.api.armor.IArmorSet;
import com.builtbroken.atomic.api.effect.IIndirectEffectInstance;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/24/2018.
 */
@Mod.EventBusSubscriber(modid = AtomicScience.DOMAIN)
public class ArmorRadiationHandler
{
    private static final EntityEquipmentSlot[] CHECK_SLOTS = new EntityEquipmentSlot[]{
            EntityEquipmentSlot.FEET,
            EntityEquipmentSlot.LEGS,
            EntityEquipmentSlot.CHEST,
            EntityEquipmentSlot.HEAD
    };

    //List of armor to protection ratings
    private static final List<ArmorRadData> armorRadiationList = new ArrayList(); //TODO if list becomes an issue switch to Map<Item, data>

    public static boolean isRadiationResistantArmor(ItemStack stack)
    {
        return getArmorRadData(stack) != null;
    }

    public static ArmorRadData getArmorRadData(ItemStack stack)
    {
        return getArmorRadData(stack, false);
    }

    public static ArmorRadData getArmorRadData(ItemStack stack, boolean create)
    {
        for (ArmorRadData data : armorRadiationList)
        {
            if (ItemStack.areItemsEqual(stack, data.item) && ItemStack.areItemStackTagsEqual(stack, data.item))
            {
                return data;
            }
        }
        if (create)
        {
            ArmorRadData data = new ArmorRadData(stack.copy());
            armorRadiationList.add(data);
            return data;
        }
        return null;
    }

    /**
     * Helper method to see if an entity has a full set of armor. Generally used inside
     * Armor items via {@link IAntiPoisonArmor#doesArmorProtectFromSource(ItemStack, EntityLivingBase, IIndirectEffectInstance)} to
     * see if the entity has a full set for a chemical/radiation seal.
     * <p>
     * Requires that all items implement {@link IArmorSet} to work. As this is not used to
     * check vanilla armor at the moment.
     * <p>
     * Will select the first item on the user to compare to all armor items. If an empty slot
     * is found it will return false. As its assume head, chest, legs, and boots are required for a full
     * set. If this is not the case then a different method should be used.
     * <p>
     * Will use {@link IArmorSet#isArmorPartOfSet(ItemStack, ItemStack)} to check if each armor part is contained
     * in the set of the first armor part selected.
     * <p>
     * Do not use this for validating items. Instead call {@link IAntiPoisonArmor#doesArmorProtectFromSource(ItemStack, EntityLivingBase, IIndirectEffectInstance)}
     * on the item so it can handle special logic as needed.
     *
     * @param entity - entity containing armor
     * @return true if a full set exists
     */
    protected static boolean hasFullSetOfArmor(EntityLivingBase entity) //TODO see if there is a way to cache this as its called every tick per armor item when doing radiation checks
    {
        ItemStack compareStack = null;
        for (EntityEquipmentSlot entityEquipmentSlot : CHECK_SLOTS)
        {
            final ItemStack slotStack = entity.getItemStackFromSlot(entityEquipmentSlot);

            //We only care about armor sets
            if (slotStack.getItem() instanceof IArmorSet)
            {
                //Init compare stack
                if (compareStack == null)
                {
                    compareStack = slotStack;
                    continue;
                }

                //Check if item is part of set
                if (!((IArmorSet) slotStack.getItem()).isArmorPartOfSet(compareStack, slotStack))
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }

}
