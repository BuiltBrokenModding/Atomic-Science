package com.builtbroken.atomic.content.armor;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.armor.IAntiPoisonArmor;
import com.builtbroken.atomic.api.effect.IIndirectEffectInstance;
import com.builtbroken.atomic.content.ASItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

/**
 * Simple hazmat suit that takes damage as its used
 *
 *
 * Created by Dark(DarkGuardsman, Robert) & Calclavia
 */
public class ItemHazmatClassic extends ItemArmor implements IAntiPoisonArmor
{
    public static int damagePerTick = 1;
    public static int damagePerAttack = 100; //TODO take damage faster from attacks

    public ItemHazmatClassic(EntityEquipmentSlot slot, String type)
    {
        super(ASItems.hazmatArmorMaterial, 0, slot);
        this.setCreativeTab(AtomicScience.creativeTab);
        this.setTranslationKey(AtomicScience.PREFIX + "hazmat." + type);
        this.setRegistryName(AtomicScience.PREFIX + "hazmat_" + type);
        this.setMaxDamage(200000);
    }

    ///------------------------------------------------------------------------------------
    /// Poison armor API stuff
    ///------------------------------------------------------------------------------------

    @Override
    public boolean doesArmorProtectFromSource(ItemStack itemStack, EntityLivingBase entity, IIndirectEffectInstance instance)
    {
        if (isFullArmorSetNeeded(itemStack, entity, instance) && !ArmorRadiationHandler.hasFullSetOfArmor(entity))
        {
            return false;
        }
        return instance.getIndirectEffectType() == AtomicScienceAPI.RADIATION || instance.getIndirectEffectType() == AtomicScienceAPI.RADIATION_DAMAGE;
    }

    @Override
    public boolean isArmorPartOfSet(ItemStack armorStack, ItemStack compareStack)
    {
        return armorStack.getItem() instanceof ItemHazmatClassic && compareStack.getItem() instanceof ItemHazmatClassic;
    }

    @Override
    public void onArmorProtectFromSource(ItemStack itemStack, EntityLivingBase entityLiving, IIndirectEffectInstance instance)
    {
        if (instance.getIndirectEffectType() == AtomicScienceAPI.RADIATION)
        {
            //itemStack.damageItem(damagePerTick, entityLiving); //TODO increase damage based on value
        }
    }

    public EntityEquipmentSlot getArmorSlot()
    {
        return this.armorType;
    }
}
