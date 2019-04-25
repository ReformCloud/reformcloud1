/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.autoicon;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

public final class IconData implements Serializable {
    private List<byte[]> icons;
    private int updateTimeInSeconds;

    @java.beans.ConstructorProperties({"icons", "updateTimeInSeconds"})
    public IconData(List<byte[]> icons, int updateTimeInSeconds) {
        this.icons = icons;
        this.updateTimeInSeconds = updateTimeInSeconds;
    }

    public List<byte[]> getIcons() {
        return this.icons;
    }

    public int getUpdateTimeInSeconds() {
        return this.updateTimeInSeconds;
    }
}
