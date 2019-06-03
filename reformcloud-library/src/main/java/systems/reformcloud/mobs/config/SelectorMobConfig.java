/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs.config;

import systems.reformcloud.mobs.inventory.SelectorMobInventory;
import systems.reformcloud.mobs.inventory.item.SelectorsMobServerItem;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class SelectorMobConfig implements Serializable {

    private SelectorMobInventory selectorMobInventory;

    private SelectorsMobServerItem selectorsMobServerItem;

    @java.beans.ConstructorProperties({"selectorMobInventory", "selectorsMobServerItem"})
    public SelectorMobConfig(SelectorMobInventory selectorMobInventory,
        SelectorsMobServerItem selectorsMobServerItem) {
        this.selectorMobInventory = selectorMobInventory;
        this.selectorsMobServerItem = selectorsMobServerItem;
    }

    public SelectorMobInventory getSelectorMobInventory() {
        return this.selectorMobInventory;
    }

    public SelectorsMobServerItem getSelectorsMobServerItem() {
        return this.selectorsMobServerItem;
    }
}
