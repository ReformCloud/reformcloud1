/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.permissions;

import org.bukkit.Bukkit;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class ReflectionUtil implements Serializable {
    public static Class<?> reflectClazz(String indentifier) {
        try {
            final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            return Class.forName("org.bukkit.craftbukkit." + version + indentifier);
        } catch (final Throwable ex) {
            return null;
        }
    }
}
