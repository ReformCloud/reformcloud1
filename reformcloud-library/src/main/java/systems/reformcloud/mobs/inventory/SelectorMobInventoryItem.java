/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

@AllArgsConstructor
@Getter
public final class SelectorMobInventoryItem implements Serializable {
    private String name, materialName;
    private int slot;
    private short subId;

    public String getMaterialName() {
        return materialName.toUpperCase();
    }
}
