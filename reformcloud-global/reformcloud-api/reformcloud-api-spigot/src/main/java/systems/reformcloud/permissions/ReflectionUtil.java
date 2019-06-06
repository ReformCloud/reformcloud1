/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.permissions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class ReflectionUtil implements Serializable {

    private static final String version = Bukkit.getServer().getClass().getPackage().getName()
        .split("\\.")[3];

    private static final String craftPackage, nmsPackage;

    static {
        craftPackage = "org.bukkit.craftbukkit." + version;
        nmsPackage = "net.minecraft.server." + version;
    }

    public static Class<?> reflectClazz(String indentifier) {
        try {
            return Class.forName(craftPackage + indentifier);
        } catch (final Throwable ex) {
            return null;
        }
    }

    public static void setNoAI(Entity entity) {
        try {
            Class<?> nbt = Class.forName(nmsPackage + ".NBTTagCompound");
            Class<?> entityClazz = Class.forName(nmsPackage + ".Entity");
            Object object = nbt.newInstance();

            Object nmsEntity = entity.getClass().getMethod("getHandle").invoke(entity);
            try {
                entityClazz.getMethod("e", nbt).invoke(nmsEntity, object);
            } catch (Exception ex) {
                entityClazz.getMethod("save", nbt).invoke(nmsEntity, object);
            }

            object.getClass().getMethod("setInt", String.class, int.class)
                .invoke(object, "NoAI", 1);
            object.getClass().getMethod("setInt", String.class, int.class)
                .invoke(object, "Silent", 1);
            entityClazz.getMethod("f", nbt).invoke(nmsEntity, object);
        } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | InstantiationException ex) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).addPotionEffect(
                    new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100));
            }

            System.out.println(
                "[ReformCloud] Could not enable no ai for entity " + entity.getUniqueId()
                    + ", trying to set slowness");
            ex.printStackTrace();
        }
    }
}
