/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.loader.CapableClassLoader;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 14.05.2019
 */

public final class CommonLoader implements Serializable {
    public static ClassLoader createClassLoader(List<URL> urls) {
        checkNonNull(urls);
        return createClassLoader(urls.toArray(new URL[0]));
    }

    public static ClassLoader createClassLoader(URL urls) {
        checkNonNull(urls);
        return createClassLoader(new URL[]{urls});
    }

    public static ClassLoader createClassLoader(URL[] urls) {
        checkNonNull(urls);
        return new CapableClassLoader(urls, ClassLoader.getSystemClassLoader());
    }

    public static String getCurrentFallbackVersion() {
        return CommonLoader.class.getPackage().getImplementationVersion();
    }

    private static void checkNonNull(Object x) {
        if (x == null)
            throw new IllegalStateException("Invocation with null parameters");
    }
}
