/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.classloader;

import java.io.Serializable;
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
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
