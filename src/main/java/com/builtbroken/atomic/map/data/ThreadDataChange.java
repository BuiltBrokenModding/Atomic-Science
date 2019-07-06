package com.builtbroken.atomic.map.data;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.map.data.storage.DataChunk;
import com.builtbroken.jlib.data.vector.IPos3D;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Generic thread used for calculating actions based on data changes in the main game world.
 * <p>
 * <p>
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
                    DataChunk chunk = removeScanQueue.poll();
                    if (chunk != null)
                    {
                        queueRemove(chunk);
                    }
                }

                //New additions, update data for values
                while (shouldRun && !addScanQueue.isEmpty())
                {
                    DataChunk chunk = addScanQueue.poll();
                    if (chunk != null)
                    {
                        queueAddition(chunk);
                    }
                }

                //Stop looping if we have chunks to scan, only loop if we have something to do
                while (shouldRun && !changeQueue.isEmpty() && addScanQueue.isEmpty() && removeScanQueue.isEmpty())
                {
                    DataChange change = changeQueue.poll();
                    if (change != null)
                    {
                        //If return true, then clear object
                        if (updateLocation(change))
                        {
                            if (change.completionListener != null)
                            {
                                change.completionListener.accept(change.source);
                            }
                            change.source.onThreadComplete();
                            change.dispose();
                        }
                        //False add back to queue, as we are not done
                        else
                        {
                            changeQueue.add(change);
                        }
                    }
                    else
                    {
                        change.dispose();
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

    }

    /**
     * Called to scan a chunk to add addition calls
     *
     * @param chunk
     */
    protected void queueAddition(DataChunk chunk)
    {

    }

    /**
     * Single box shaped range check
     *
     * @param center - center to range from
     * @param i      - point
     * @param j      - point
     * @param k      - point
     * @param range  - distance
     * @return true if in range of center point
     */
    protected boolean inRange(IPos3D center, int i, int j, int k, int range)
    {
        return inRange(center.xi(), i, range) && inRange(center.yi(), j, range) && inRange(center.zi(), k, range);
    }

    /**
     * Simple range check ( i < range && i > - range)
     *
     * @param x     - center point
     * @param i     - point
     * @param range - distance
     * @return true if in range of x
     */
    protected boolean inRange(int x, int i, int range)
    {
        int delta = x - i;
        return delta < range && delta > -range;
    }

    /**
     * Called to update the exposure value at the location
     *
     * @param change
     * @return true to note change has completed
     */
    protected abstract boolean updateLocation(DataChange change);

    public void kill()
    {
        shouldRun = false;
        AtomicScience.logger.info(name + ": Stopping thread");
    }

    public void queueChunkForRemoval(DataChunk chunk)
    {
        if (chunk != null && chunk.hasData())
        {
            removeScanQueue.add(chunk);
        }
    }

    public void queueChunkForAddition(DataChunk chunk)
    {
        if (chunk != null && chunk.hasData())
        {
            addScanQueue.add(chunk);
        }
    }

    public void queuePosition(DataChange change)
    {
        if (change != null)
        {
            changeQueue.add(change);

            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.info(String.format(this + ": Queued to thread a new change %sx %sy %sz | %sn",
                        change.xi(), change.yi(), change.zi(),
                        change.value
                ));
            }
        }
    }
}
