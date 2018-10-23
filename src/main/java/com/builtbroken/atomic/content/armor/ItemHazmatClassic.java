package com.builtbroken.atomic.content.armor;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.armor.IAntiPoisonArmor;
import com.builtbroken.atomic.api.effect.IIndirectEffectInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

/**
 * Simple hazmat suit that takes damage as its used
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) & Calclavia
 */
public class ItemHazmatClassic extends ItemArmor implements IAntiPoisonArmor
{
    /** Prefix for model textures */
    public static final String ARMOR_MODEL_TEXTURE = AtomicScience.PREFIX + AtomicScience.MODEL_TEXTURE_DIRECTORY + "armor/hazmat.png";

    /** Armor material */
    public static ItemArmor.ArmorMaterial hazmatArmorMaterial;

    public static int damagePerTick = 1;
    public static int damagePerAttack = 100; //TODO take damage faster from attacks

    public ItemHazmatClassic(EntityEquipmentSlot slot, String type)
    {
        super(hazmatArmorMaterial, 0, slot);
        this.setCreativeTab(AtomicScience.creativeTab);
        this.setTranslationKey(AtomicScience.PREFIX + "hazmat." + type);
        this.setRegistryName(AtomicScience.PREFIX + "hazmat_" + type);
        this.setMaxDamage(200000);
    }

    ///------------------------------------------------------------------------------------
    /// Texture stuff
    ///------------------------------------------------------------------------------------

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        return ARMOR_MODEL_TEXTURE;
    }

    ///------------------------------------------------------------------------------------
    /// Poison armor API stuff
    ///------------------------------------------------------------------------------------

    @Override
    public boolean doesArmorProtectFromSource(ItemStack itemStack, EntityLivingBase entity, IIndirectEffectInstance instance)
    {
        if (isFullArmorSetNeeded(itemStack, entity, instance) && !hasFullSetOfArmor(entity))
        {
            return false;
        }
        return instance.getIndirectEffectType() == AtomicScienceAPI.RADIATION || instance.getIndirectEffectType() == AtomicScienceAPI.RADIATION_DAMAGE;
    }

    protected boolean hasFullSetOfArmor(EntityLivingBase entity)
    {
        ItemStack itemStack = null;
        for (int i = 2; i < 6; i++)
        {
            final ItemStack slotStack = entity.getItemStackFromSlot(EntityEquipmentSlot.values()[i]);
            if (slotStack != null)
            {
                //Init compare stack
                if (itemStack == null)
                {
                    itemStack = slotStack;
                    continue;
                }

                //Check if item is part of set
                if (slotStack.getItem() instanceof IAntiPoisonArmor && !isArmorPartOfSet(itemStack, slotStack))
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
