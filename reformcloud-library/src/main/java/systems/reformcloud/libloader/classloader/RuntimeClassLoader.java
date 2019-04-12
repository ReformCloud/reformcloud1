/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.classloader;

import lombok.Getter;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 12.04.2019
 */

@Getter
public final class RuntimeClassLoader extends ClassLoader implements Serializable {
    private final RuntimeURLClassLoader urlClassLoader;

    public RuntimeClassLoader(ClassLoader parent, URL[] urls, List<URL> loadedURLs) {
        super(parent);
        ClassLoader.registerAsParallelCapable();
        this.urlClassLoader = new RuntimeURLClassLoader(urls, parent);
        loadedURLs.forEach(this.urlClassLoader::addURL);

        try {
            Field field = ClassLoader.class.getDeclaredField("scl");
            field.setAccessible(true);
            field.set(ClassLoader.getSystemClassLoader(), this);
        } catch (final NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        System.out.println(ClassLoader.getSystemClassLoader());
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.urlClassLoader.loadClass(name);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return this.urlClassLoader.loadClass(name, resolve);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return this.urlClassLoader.findClass(name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        return this.urlClassLoader.findResources(name);
    }

    @Override
    protected URL findResource(String name) {
        return this.urlClassLoader.findResource(name);
    }

    public class RuntimeURLClassLoader extends URLClassLoader {
        private RuntimeURLClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            return super.findClass(name);
        }

        @Override
        public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }

        @Override
        public void addURL(URL url) {
            super.addURL(url);
        }
    }
}
