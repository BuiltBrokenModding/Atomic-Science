package com.builtbroken.atomic.map.data;

import com.builtbroken.atomic.AtomicScience;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Generic thread used for calculating actions based on data changes in the main game world.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2018.
 */
public abstract class ThreadDataChange extends Thread
{
    /** Name of the thread, used for display purposes and unique ID of the thread */
    public final String name;

    /** Should the thread continue to run, true -> run... false -> stop & terminate */
    public boolean shouldRun = true;

    //Object queues
    private ConcurrentLinkedQueue<DataChange> changeQueue = new ConcurrentLinkedQueue();
    private ConcurrentLinkedQueue<DataChunk> addScanQueue = new ConcurrentLinkedQueue();
    private ConcurrentLinkedQueue<DataChunk> removeScanQueue = new ConcurrentLinkedQueue();

    public ThreadDataChange(String name)
    {
        super(null, null, AtomicScience.PREFIX + name, 0);
        this.name = name;
        setPriority(3); //default is 5, lower value -> lower priority
        setDaemon(true);
    }

    @Override
    public void start()
    {
        shouldRun = true;
        AtomicScience.logger.info(name + ": Starting thread");
        super.start();
    }

    @Override
    public void run()
    {
        while (shouldRun)
        {
            try
            {
                //Cleanup of chunks, remove data from chunk so when re-added later it doesn't spike values
                while (shouldRun && !removeScanQueue.isEmpty())
                {
                    queueRemove(removeScanQueue.poll());
                }

                //New additions, update data for values
                while (shouldRun && !addScanQueue.isEmpty())
                {
                    queueAddition(addScanQueue.poll());
                }

                //Stop looping if we have chunks to scan, only loop if we have something to do
                while (shouldRun && !changeQueue.isEmpty() && addScanQueue.isEmpty() && removeScanQueue.isEmpty())
                {
                    DataChange change = changeQueue.poll();
                    if (change != null)
                    {
                        updateLocation(change);
                    }
                }

                //Nothing left to do, then sleep for 100ms before checking on updates
                sleep(100);
            }
            catch (Exception e)
            {
                AtomicScience.logger.error(name + ": Unexpected error during operation", e);
            }
        }

        //If stopped, clear all data
        removeScanQueue.clear();
        addScanQueue.clear();
        changeQueue.clear();
    }

    /**
     * Called to scan a chunk to add remove calls
     *
     * @param chunk
     */
    protected void queueRemove(DataChunk chunk)
    {
        if (chunk != null)
        {
            for (DataLayer layer : chunk.getLayers())
            {
                for (int cx = 0; cx < 16; cx++)
                {
                    for (int cz = 0; cz < 16; cz++)
                    {
                        if (layer != null && layer.getData(cx, cz) > 0)
                        {
                            int x = cx + chunk.xPosition * 16;
                            int z = cz + chunk.xPosition * 16;
                            changeQueue.add(new DataChange(chunk.dimension, x, layer.y_index, z, layer.getData(cx, cz), 0));
                        }
                    }
                }
            }
        }
    }

    /**
     * Called to scan a chunk to add addition calls
     *
     * @param chunk
     */
    protected void queueAddition(DataChunk chunk)
    {
        if (chunk != null)
        {
            for (DataLayer layer : chunk.getLayers())
            {
                for (int cx = 0; cx < 16; cx++)
                {
                    for (int cz = 0; cz < 16; cz++)
                    {
                        if (layer != null && layer.getData(cx, cz) > 0)
                        {
                            int x = cx + chunk.xPosition * 16;
                            int z = cz + chunk.xPosition * 16;
                            changeQueue.add(new DataChange(chunk.dimension, x, layer.y_index, z, 0, layer.getData(cx, cz)));
                        }
                    }
                }
            }
        }
    }

    /**
     * Called to update the exposure value at the location
     *
     * @param change
     */
    protected abstract void updateLocation(DataChange change);

    public void kill()
    {
        shouldRun = false;
        AtomicScience.logger.info(name + ": Stopping thread");
    }

    public void queueChunkForRemoval(DataChunk chunk)
    {
        removeScanQueue.add(chunk);
    }

    public void queueChunkForAddition(DataChunk chunk)
    {
        addScanQueue.add(chunk);
    }

    public void queuePosition(DataChange radChange)
    {
        changeQueue.add(radChange);
    }
}
