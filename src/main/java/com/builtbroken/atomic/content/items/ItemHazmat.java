package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.armor.IAntiPoisonArmor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Simple hazmat suit that takes damage as its used
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) & Calclavia
 */
public class ItemHazmat extends ItemArmor implements IAntiPoisonArmor
{
    /** Armor material */
    public static final ItemArmor.ArmorMaterial hazmatArmorMaterial = EnumHelper.addArmorMaterial("HAZMAT", 0, new int[]{0, 0, 0, 0}, 0);

    /** Prefix for model textures */
    public static final String ARMOR_MODEL_FOLDER = AtomicScience.PREFIX + AtomicScience.MODEL_DIRECTORY + "armor/";
    /** Prefix for item textures */
    public static final String ARMOR_TEXTURE_FOLDER = AtomicScience.PREFIX + "armor/";


    public static int damagePerTick = 1;
    public static int damagePerAttack = 100; //TODO take damage faster from attacks

    public ItemHazmat(int slot)
    {
        super(hazmatArmorMaterial, 0, slot);
        this.setMaxDamage(200000);
    }

    ///------------------------------------------------------------------------------------
    /// Texture stuff
    ///------------------------------------------------------------------------------------
    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(ARMOR_TEXTURE_FOLDER + "hazmat_" +
                (
                        this.armorType == 0 ? "helmet" :
                                this.armorType == 1 ? "chestplate" :
                                        this.armorType == 2 ? "leggings" :
                                                this.armorType == 3 ? "boots" : "helmet"
                )
        );
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        int suffix = this.armorType == 2 ? 2 : 1;
        return ARMOR_MODEL_FOLDER + "hazmat" + "_" + suffix + ".png";
    }

    ///------------------------------------------------------------------------------------
    /// Poison armor API stuff
    ///------------------------------------------------------------------------------------

    @Override
    public boolean doesArmorProtectFromSource(ItemStack itemStack, EntityLivingBase entityLiving, String sourceType, float value)
    {
        return sourceType.equalsIgnoreCase("radiation") || sourceType.equalsIgnoreCase("chemical") || sourceType.equalsIgnoreCase("contagious");
    }

    @Override
    public void onArmorProtectFromSource(ItemStack itemStack, EntityLivingBase entityLiving, String type, float value)
    {
        itemStack.damageItem(damagePerTick, entityLiving); //TODO increase damage based on value
    }

    @Override
    public int getArmorType()
    {
        return this.armorType;
    }
}
