/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

@AllArgsConstructor
@Data
public final class SignLayout implements Serializable {
    /**
     * The sign lines
     */
    private String[] lines;

    /**
     * The name of the background material
     */
    private String materialName;

    /**
     * The sub id of the sign background material
     */
    private int materialData;

    /**
     * Creates a new default sign layout
     *
     * @param lines     The lines of the sign
     */
    public SignLayout(String[] lines) {
        this.lines = lines;
        this.materialName = "SAND";
        this.materialData = 0;
    }

    /**
     * The loading layout sub class
     */
    @AllArgsConstructor
    @Data
    public static class LoadingLayout {
        /**
         * The animation count per second
         */
        private int perSecondAnimation;

        /**
         * The current animation of the sign
         */
        private transient int currentAnimation;

        /**
         * All loading layouts
         */
        private SignLayout[] layouts;

        /**
         * Gets the next layout
         *
         * @return      The next layout of all registered sign loading layouts
         */
        public SignLayout getNextLayout() {
            currentAnimation++;
            if (currentAnimation >= layouts.length)
                currentAnimation = 0;
            return layouts[currentAnimation];
        }
    }

    /**
     * The group layout sub class
     */
    @AllArgsConstructor
    @Data
    public static class GroupLayout {
        private SignLayout maintenanceLayout, emptyLayout, fullLayout, onlineLayout;
    }

    /**
     * The template layout sub class
     */
    @AllArgsConstructor
    @Data
    public static class TemplateLayout {
        private SignLayout emptyLayout, fullLayout, onlineLayout;
    }
}
