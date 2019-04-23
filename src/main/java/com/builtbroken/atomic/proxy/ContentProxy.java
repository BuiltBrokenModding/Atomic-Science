package com.builtbroken.atomic.proxy;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class ContentProxy
{
    public final String name;

    public ContentProxy(String name)
    {
        this.name = name;
    }

    public boolean shouldLoad()
    {
        return true; //TODO add config
    }

    public void preInit()
    {

    }

    public void init()
    {

    }

    public void postInit()
    {

    }

    public void loadComplete()
    {

    }

    public static boolean doesClassExist(String className)
    {
        try
        {
            Class.forName(className);
            return true;
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return "ContentProxy[" + name + "]";
    }
}
