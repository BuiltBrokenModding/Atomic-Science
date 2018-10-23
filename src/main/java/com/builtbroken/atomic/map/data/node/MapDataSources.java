package com.builtbroken.atomic.map.data.node;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.map.IDataMapSource;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/12/2018.
 */
@Mod.EventBusSubscriber(modid = AtomicScience.DOMAIN)
public final class MapDataSources
{
    private static final List<IDataMapSource> sources = new ArrayList();
    private static final HashMap<IDataMapSource, MapSourceInfo> sourceInfo = new HashMap();
    private static final List<IDataMapSource> waitingForThread = new ArrayList();

    private static boolean callChecked = false;

    @SubscribeEvent()
    public static void serverTick(TickEvent.ServerTickEvent event)
    {
        if(!callChecked)
        {
            callChecked = true;
            if(AtomicScience.runningAsDev)
            {
                AtomicScience.logger.info("MapDataSources#serverTick() is working");
            }
        }
        Iterator<IDataMapSource> it = sources.iterator();
        while (it.hasNext())
        {
            final IDataMapSource source = it.next();

            try
            {
                //Clear dead sources
                if (source == null || !source.doesSourceExist())
                {
                    //Remove
                    it.remove();
                    onSourceRemoved(source);

                    if (AtomicScience.runningAsDev)
                    {
                        AtomicScience.logger.info("MapDataSources#serverTick() - Removed dead source, " + source);
                    }
                }
                //Disconnect invalid sources
                else if (!source.isStillValid())
                {
                    if(source.hasActiveMapData())
                    {
                        if (AtomicScience.runningAsDev)
                        {
                            AtomicScience.logger.info("MapDataSources#serverTick() - Disconnected map data, " + source);
                        }
                        source.disconnectMapData();

                        //Remove thread tracking
                        waitingForThread.remove(source);

                        //Remove info
                        sourceInfo.remove(source);
                    }
                }
                //Handle updates
                else
                {
                    MapSourceInfo info = sourceInfo.get(source);
                    if (info == null)
                    {
                        sourceInfo.put(source, info = new MapSourceInfo(source));
                    }

                    //Update
                    source.update();

                    //Check if the source needs updating
                    if (!waitingForThread.contains(source) && info.needsQueued())
                    {
                        if (AtomicScience.runningAsDev)
                        {
                            AtomicScience.logger.info("MapDataSources#serverTick() - Marked source for update, " + source);
                        }

                        //Queue to thread
                        source.getType().queueSourceForUpdate(source);

                        //Log data about source so we know when it changes
                        info.logState();
                    }
                }
            }
            catch (Exception e)
            {
                AtomicScience.logger.error("MapDataSources#serverTick() - Unexpected error while checking source, " + source, e);
            }
        }

    }

    /**
     * Called to add source to the map
     * <p>
     * Only call this from the main thread. As the list of sources
     * is iterated at the end of each tick to check for changes.
     *
     * @param source - valid source currently in the world
     */
    public static void addSource(IDataMapSource source)
    {
        if (source != null && !source.world().isRemote && source.isStillValid() && !sources.contains(source))
        {
            if(AtomicScience.runningAsDev)
            {
                AtomicScience.logger.info("MapDataSources#addSource(" + source + ")");
            }
            sources.add(source);
        }
    }

    /**
     * Called to remove a source from the map
     * <p>
     * Only call this from the main thread. As the list of sources
     * is iterated at the end of each tick to check for changes.
     * <p>
     * Only remove if external logic requires it. As the source
     * should return false for {@link IDataMapSource#isStillValid()}
     * to be automatically removed.
     *
     * @param source - valid source currently in the world
     */
    public static void removeSource(IDataMapSource source)
    {
        if (sources.contains(source))
        {
            //Remove
            sources.remove(source);
            onSourceRemoved(source);

            if(AtomicScience.runningAsDev)
            {
                AtomicScience.logger.info("MapDataSources#removeSource(" + source + ")");
            }
        }
    }

    private static void onSourceRemoved(IDataMapSource source)
    {
        //Remove from thread tracker
        waitingForThread.remove(source);

        //Remove info
        sourceInfo.remove(source);

        //Callback fro removal
        source.onRemovedFromMap();

    }

    public static void addSource(Entity entity)
    {
        //TODO implement
    }

    public static void removeSource(Entity entity)
    {
        //TODO implement
    }

    public static void onAddedToThread(IDataMapSource source)
    {
        waitingForThread.add(source);
    }

    public static void onRemovedFromThread(IDataMapSource source)
    {
        waitingForThread.remove(source);
    }
}
