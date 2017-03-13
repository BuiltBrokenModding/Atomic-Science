package com.builtbroken.atomic.base;

import resonant.lib.prefab.item.ItemBase;
import com.core.Reference;
import com.core.Settings;
import com.core.TabRI;

public class ItASBase extends ItemBase
{
    /** Must be called while in mod init phase. */
    public ItASBase(int itemID, String name)
    {
        super(itemID, name, Settings.CONFIGURATION, Reference.PREFIX, TabRI.DEFAULT);
    }
}
