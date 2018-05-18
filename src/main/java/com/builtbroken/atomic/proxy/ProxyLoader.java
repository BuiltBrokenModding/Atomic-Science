package com.builtbroken.atomic.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Loader of proxies
 * <p>
 * Used as the main proxy or a way to load a lot of proxies at once
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class ProxyLoader extends ContentProxy
{
    private final List<ContentProxy> proxyList = new ArrayList();

    public ProxyLoader(String name)
    {
        super(name);
    }

    public void add(Class<? extends ContentProxy> proxyClazz, BooleanSupplier function)
    {
        add(proxyClazz, function.getAsBoolean());
    }

    public void add(Class<? extends ContentProxy> proxyClazz, boolean bool)
    {
        if (bool)
        {
            try
            {
                add(proxyClazz.newInstance());
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException("Failed to create proxy from constructor for '" + proxyClazz + "'", e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException("Failed to access proxy constructor for '" + proxyClazz + "'", e);
            }
        }
    }

    public void add(ContentProxy proxy)
    {
        proxyList.add(proxy);
    }

    public void preInit()
    {
        for (ContentProxy proxy : proxyList)
        {
            proxy.preInit();
        }
    }

    public void init()
    {
        for (ContentProxy proxy : proxyList)
        {
            proxy.init();
        }
    }

    public void postInit()
    {
        for (ContentProxy proxy : proxyList)
        {
            proxy.postInit();
        }
    }

    @Override
    public String toString()
    {
        return "ProxyLoader[" + name + "]";
    }
}
