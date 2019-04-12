/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.classloader;

import sun.misc.Launcher;

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

    public RuntimeClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);

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
        Launcher.getBootstrapClassPath().addURL(url);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            try {
                loadedClass = findClass(name);
            } catch (ClassNotFoundException e) {
                loadedClass = super.loadClass(name, resolve);
            }
        }

        if (resolve)
            resolveClass(loadedClass);

        return loadedClass;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.loadClass(name, false);
    }
}
