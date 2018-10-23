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
import net.minecraft.nbt.NBTTagCompound;

import java.awt.*;

/**
 * Colorized version of the Hazmat suit
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert)
 */
public class ItemHazmatColor extends ItemArmor implements IAntiPoisonArmor
{
    public static int damagePerTick = 1;
    public static int damagePerAttack = 100; //TODO take damage faster from attacks

    private static final Color color = new Color(0x99A008);

    public ItemHazmatColor(EntityEquipmentSlot slot, String type)
    {
        super(ASItems.hazmatArmorMaterialColor, 0, slot);
        this.setCreativeTab(AtomicScience.creativeTab);
        this.setTranslationKey(AtomicScience.PREFIX + "hazmat." + type + ".color");
        this.setRegistryName(AtomicScience.PREFIX + "hazmat_color_" + type);
        this.setMaxDamage(200000);
    }

    ///------------------------------------------------------------------------------------
    /// Color overrides
    ///------------------------------------------------------------------------------------

    @Override
    public boolean hasColor(ItemStack stack) //TODO create forge PR so we don't have to override to bypass is leather check
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        return nbttagcompound != null && nbttagcompound.hasKey("display", 10) ? nbttagcompound.getCompoundTag("display").hasKey("color", 3) : false;
    }

    @Override
    public int getColor(ItemStack stack) //TODO create forge PR so we don't have to override to bypass is leather check
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null)
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
            {
                return nbttagcompound1.getInteger("color");
            }
        }

        return color.getRGB();
    }



    @Override
    public void removeColor(ItemStack stack) //TODO create forge PR so we don't have to override to bypass is leather check
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null)
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (nbttagcompound1.hasKey("color"))
            {
                nbttagcompound1.removeTag("color");
            }
        }
    }

    @Override
    public void setColor(ItemStack stack, int color) //TODO create forge PR so we don't have to override to bypass is leather check
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound == null)
        {
            nbttagcompound = new NBTTagCompound();
            stack.setTagCompound(nbttagcompound);
        }

        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

        if (!nbttagcompound.hasKey("display", 10))
        {
            nbttagcompound.setTag("display", nbttagcompound1);
        }

        nbttagcompound1.setInteger("color", color);
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
        return armorStack.getItem() instanceof ItemHazmatColor && compareStack.getItem() instanceof ItemHazmatColor;
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
