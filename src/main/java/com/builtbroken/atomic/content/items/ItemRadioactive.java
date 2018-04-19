package com.builtbroken.atomic.content.items;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.armor.IAntiPoisonArmor;
import com.builtbroken.atomic.api.effect.IIndirectEffectInstance;
import com.builtbroken.atomic.api.effect.IIndirectEffectType;
import com.builtbroken.atomic.content.effects.IndirectEffectInstance;
import com.builtbroken.atomic.content.effects.source.SourceWrapperItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Radioactive item
 * <p>
 * Purpose: Prefab & Generic item for radioactive objects
 * Features:
 * * Generate radiation in the environment
 * * Radiate entities
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
public class ItemRadioactive extends Item
{
    public ItemRadioactive(String name, String texture)
    {
        this.setUnlocalizedName(AtomicScience.PREFIX + name);
        this.setTextureName(AtomicScience.PREFIX + texture);
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean par5)
    {
        if (entity instanceof EntityLivingBase && shouldApplyEffect(itemStack, world, entity, slot))
        {
            SourceWrapperItem sourceWrapper = new SourceWrapperItem(entity, itemStack, slot);
            IndirectEffectInstance indirectEffectInstance = new IndirectEffectInstance(getEffectType(itemStack), sourceWrapper, getEffectPower(itemStack));
            if (isProtected((EntityLivingBase) entity, indirectEffectInstance))
            {
                onProtected((EntityLivingBase) entity, indirectEffectInstance);
            }
            else
            {
                indirectEffectInstance.applyIndirectEffect(entity);
            }
        }
    }

    public IIndirectEffectType getEffectType(ItemStack itemStack)
    {
        return AtomicScienceAPI.RADIATION;
    }

    public float getEffectPower(ItemStack itemStack)
    {
        return 1;
    }

    public boolean shouldApplyEffect(ItemStack itemStack, World world, Entity entity, int slot)
    {
        return world.rand.nextFloat() > 0.8f;
    }

    public boolean isProtected(EntityLivingBase entity, IIndirectEffectInstance indirectEffectInstance)
    {
        //Loop armor 1-4
        for (int i = 1; i < 5; i++)
        {
            final ItemStack stack = entity.getEquipmentInSlot(i);

            //Armor set is checked by the armor itself. In theory this means the first item should return true for a full set.
            if (stack.getItem() instanceof IAntiPoisonArmor && ((IAntiPoisonArmor) stack.getItem()).doesArmorProtectFromSource(stack, entity, indirectEffectInstance))
            {
                return true;
            }
        }
        return false;
    }

    public void onProtected(EntityLivingBase entity, IIndirectEffectInstance indirectEffectInstance)
    {
        for (int i = 1; i < 5; i++)
        {
            final ItemStack stack = entity.getEquipmentInSlot(i);

            //Armor set is checked by the armor itself. In theory this means the first item should return true for a full set.
            if (stack.getItem() instanceof IAntiPoisonArmor)
            {
                ((IAntiPoisonArmor) stack.getItem()).onArmorProtectFromSource(stack, entity, indirectEffectInstance);
            }
        }
    }
}
