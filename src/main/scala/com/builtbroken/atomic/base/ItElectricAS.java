package com.builtbroken.atomic.base;

import com.core.Reference;
import com.core.Settings;
import com.core.TabRI;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.item.IEnergyItem;
import universalelectricity.api.item.ItemElectric;

//@UniversalClass
public abstract class ItElectricAS extends ItemElectric implements IEnergyItem
{
    public ItElectricAS(int itemID, String name)
    {
        super(Settings.CONFIGURATION.getItem(name, itemID).getInt());
        this.setUnlocalizedName(Reference.PREFIX + name);
        this.setCreativeTab(TabRI.DEFAULT);
        this.setTextureName(Reference.PREFIX + name);

    }
}
