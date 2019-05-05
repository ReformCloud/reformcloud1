/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

public final class SignPosition implements Serializable {
    /**
     * The general information about the sign
     */
    private String targetGroup, world;

    /**
     * The sign position
     */
    private int x, y, z;

    @java.beans.ConstructorProperties({"targetGroup", "world", "x", "y", "z"})
    public SignPosition(String targetGroup, String world, int x, int y, int z) {
        this.targetGroup = targetGroup;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getTargetGroup() {
        return this.targetGroup;
    }

    public String getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
}
