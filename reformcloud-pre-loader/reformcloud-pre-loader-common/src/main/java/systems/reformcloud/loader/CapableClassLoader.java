/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.loader;

import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author _Klaro | Pasqual K. / created on 14.05.2019
 */

public final class CapableClassLoader extends URLClassLoader implements Serializable {

    public CapableClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    static {
        if (!ClassLoader.registerAsParallelCapable()) {
            throw new IllegalStateException("Cannot create ClassLoader");
        }
    }
}
