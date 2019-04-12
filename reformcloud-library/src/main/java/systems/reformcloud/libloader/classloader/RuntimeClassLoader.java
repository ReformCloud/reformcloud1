/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.classloader;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author _Klaro | Pasqual K. / created on 12.04.2019
 */

public final class RuntimeClassLoader extends URLClassLoader implements Serializable {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    public RuntimeClassLoader(URL[] urls) {
        super(urls);

        try {
            Field field = ClassLoader.class.getDeclaredField("scl");
            field.setAccessible(true);
            field.set(ClassLoader.getSystemClassLoader(), this);
        } catch (final NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }
}
