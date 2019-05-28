/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.config;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

final class IconConfig implements Serializable {
    private String targetGroup;
    private int updateTimeInSeconds;
    private List<String> iconPaths;

    @java.beans.ConstructorProperties({"targetGroup", "updateTimeInSeconds", "iconPaths"})
    IconConfig(String targetGroup, int updateTimeInSeconds, List<String> iconPaths) {
        this.targetGroup = targetGroup;
        this.updateTimeInSeconds = updateTimeInSeconds;
        this.iconPaths = iconPaths;
    }

    public String getTargetGroup() {
        return this.targetGroup;
    }

    public int getUpdateTimeInSeconds() {
        return this.updateTimeInSeconds;
    }

    public List<String> getIconPaths() {
        return this.iconPaths;
    }
}
