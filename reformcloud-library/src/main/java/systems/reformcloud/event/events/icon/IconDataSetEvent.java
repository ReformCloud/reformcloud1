/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.icon;

import systems.reformcloud.autoicon.IconData;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class IconDataSetEvent extends Event implements Serializable {

    public IconDataSetEvent(IconData iconData) {
        this.iconData = iconData;
    }

    private final IconData iconData;

    public IconData getIconData() {
        return iconData;
    }
}
