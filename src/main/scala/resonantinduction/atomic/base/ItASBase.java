package resonantinduction.atomic.base;

import resonant.lib.prefab.item.ItemBase;
import resonantinduction.core.Reference;
import resonantinduction.core.Settings;
import resonantinduction.core.TabRI;

public class ItASBase extends ItemBase
{
    /** Must be called while in mod init phase. */
    public ItASBase(int itemID, String name)
    {
        super(itemID, name, Settings.CONFIGURATION, Reference.PREFIX, TabRI.DEFAULT);
    }
}
