/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

@AllArgsConstructor
@Getter
public final class SelectorMobPosition implements Serializable {
    private String targetGroup, world;
    private double x, y, z, yaw, pitch;

    @Override
    public String toString() {
        return "World: " + world + ", X: " + x + ", Y: " + y + ", Z: " + z + " @" + targetGroup;
    }
}
