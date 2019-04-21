/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs.inventory.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

@AllArgsConstructor
@Getter
public final class SelectorsMobServerItem implements Serializable {
    private String name, itemName;
    private List<String> lore;
    private short subId;

    public String getItemName() {
        return itemName.toUpperCase();
    }
}
