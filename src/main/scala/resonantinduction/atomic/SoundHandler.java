package resonantinduction.atomic;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import resonantinduction.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHandler
{
    public static final SoundHandler INSTANCE = new SoundHandler();

    public static final String[] SOUND_FILES =
    { "antimatter.ogg", "strangematter.ogg", "alarm.ogg", "accelerator.ogg", "turbine.ogg", "assembler.ogg", "reactorcell.ogg" };

    @ForgeSubscribe
    public void loadSoundEvents(SoundLoadEvent event)
    {
        for (int i = 0; i < SOUND_FILES.length; i++)
        {
            event.manager.addSound(Reference.PREFIX + SOUND_FILES[i]);
        }
    }
}