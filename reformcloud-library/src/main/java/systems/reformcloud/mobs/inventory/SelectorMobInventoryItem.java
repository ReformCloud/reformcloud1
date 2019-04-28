/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs.inventory;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class SelectorMobInventoryItem implements Serializable {
    private String name, materialName;
    private int slot;
    private short subId;

    @java.beans.ConstructorProperties({"name", "materialName", "slot", "subId"})
    public SelectorMobInventoryItem(String name, String materialName, int slot, short subId) {
        this.name = name;
        this.materialName = materialName;
        this.slot = slot;
        this.subId = subId;
    }

    public String getMaterialName() {
        return materialName.toUpperCase();
    }

    public String getName() {
        return this.name;
    }

    public int getSlot() {
        return this.slot;
    }

    public short getSubId() {
        return this.subId;
    }
}
