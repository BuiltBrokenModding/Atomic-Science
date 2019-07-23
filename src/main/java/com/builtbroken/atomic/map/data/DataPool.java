package com.builtbroken.atomic.map.data;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/12/2018.
 */
public class DataPool<E extends Object>
{
    public int maxObjectPoleCount = 100000;
    private ConcurrentLinkedQueue<E> objectPole = new ConcurrentLinkedQueue();
    private int objectPoleCount = 0;

    public DataPool(int max)
    {
        this.maxObjectPoleCount = max;
    }

    public boolean has()
    {
        return !objectPole.isEmpty();
    }

    public E get()
    {
        if(objectPoleCount > 0)
        {
            objectPoleCount--;
            return objectPole.poll();
        }
        return null;
    }

    public void dispose(E object)
    {
        if (objectPoleCount < maxObjectPoleCount)
        {
            objectPole.add(object);
            objectPoleCount++;
        }
    }
}
