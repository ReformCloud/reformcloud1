/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class SelectorMobPosition implements Serializable {
    private String targetGroup, world;
    private double x, y, z, yaw, pitch;

    @java.beans.ConstructorProperties({"targetGroup", "world", "x", "y", "z", "yaw", "pitch"})
    public SelectorMobPosition(String targetGroup, String world, double x, double y, double z, double yaw, double pitch) {
        this.targetGroup = targetGroup;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public String toString() {
        return "World: " + world + ", X: " + x + ", Y: " + y + ", Z: " + z + " @" + targetGroup;
    }

    public String getTargetGroup() {
        return this.targetGroup;
    }

    public String getWorld() {
        return this.world;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getYaw() {
        return this.yaw;
    }

    public double getPitch() {
        return this.pitch;
    }
}
