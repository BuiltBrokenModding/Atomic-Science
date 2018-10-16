package com.builtbroken.atomic.content.prefab;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.lib.LanguageUtility;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/15/2018.
 */
public class ItemPrefab extends Item
{
    public ItemPrefab(String key, String name)
    {
        this.setRegistryName(AtomicScience.PREFIX + key);
        this.setTranslationKey(AtomicScience.PREFIX + name);
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        LanguageUtility.getLocal(getTranslationKey(stack) + ".info", tooltip);
    }
}
