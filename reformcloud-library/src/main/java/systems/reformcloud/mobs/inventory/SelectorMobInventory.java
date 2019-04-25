/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs.inventory;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class SelectorMobInventory implements Serializable {
    private String name;
    private int size;
    private Collection<SelectorMobInventoryItem> items;

    @java.beans.ConstructorProperties({"name", "size", "items"})
    public SelectorMobInventory(String name, int size, Collection<SelectorMobInventoryItem> items) {
        this.name = name;
        this.size = size;
        this.items = items;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public Collection<SelectorMobInventoryItem> getItems() {
        return this.items;
    }
}
