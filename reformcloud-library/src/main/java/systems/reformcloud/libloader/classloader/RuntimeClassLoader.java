/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.classloader;

import lombok.Getter;

import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author _Klaro | Pasqual K. / created on 12.04.2019
 */

@Getter
public final class RuntimeClassLoader extends ClassLoader implements Serializable {
    private final URLClassLoader urlClassLoader;

    public RuntimeClassLoader(ClassLoader parent, URL[] urls) {
        super(parent);
        ClassLoader.registerAsParallelCapable();
        this.urlClassLoader = new URLClassLoader(urls, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.urlClassLoader.loadClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return this.loadClass(name);
    }
}
