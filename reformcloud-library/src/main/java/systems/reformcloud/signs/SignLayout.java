/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

@AllArgsConstructor
@Data
public class SignLayout {

    private String[] lines;
    private String materialName;
    private int materialData;

    public SignLayout(String[] lines) {
        this.lines = lines;
        this.materialName = "STAINED_CLAY";
        this.materialData = 0;
    }

    @AllArgsConstructor
    @Data
    public static class LoadingLayout {

        private int perSecondAnimation;
        private transient int currentAnimation;
        private SignLayout[] layouts;

        public SignLayout getNextLayout() {
            currentAnimation++;
            if (currentAnimation >= layouts.length)
                currentAnimation = 0;
            return layouts[currentAnimation];
        }
    }

    @AllArgsConstructor
    @Data
    public static class GroupLayout {
        private SignLayout maintenanceLayout, emptyLayout, fullLayout, onlineLayout;
    }
}
