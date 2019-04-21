/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

@AllArgsConstructor
@Getter
public final class SelectorMobInventory implements Serializable {
    private String name;
    private int size;
    private Collection<SelectorMobInventoryItem> items;
}
