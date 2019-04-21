/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.mobs.inventory.SelectorMobInventory;
import systems.reformcloud.mobs.inventory.item.SelectorsMobServerItem;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

@AllArgsConstructor
@Getter
public final class SelectorMobConfig implements Serializable {
    private SelectorMobInventory selectorMobInventory;
    private SelectorsMobServerItem selectorsMobServerItem;
}
