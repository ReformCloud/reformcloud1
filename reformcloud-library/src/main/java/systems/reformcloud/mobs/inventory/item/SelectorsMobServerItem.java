/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs.inventory.item;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class SelectorsMobServerItem implements Serializable {
    private String name, itemName;
    private List<String> lore;
    private short subId;

    @java.beans.ConstructorProperties({"name", "itemName", "lore", "subId"})
    public SelectorsMobServerItem(String name, String itemName, List<String> lore, short subId) {
        this.name = name;
        this.itemName = itemName;
        this.lore = lore;
        this.subId = subId;
    }

    public String getItemName() {
        return itemName.toUpperCase();
    }

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public short getSubId() {
        return this.subId;
    }
}
