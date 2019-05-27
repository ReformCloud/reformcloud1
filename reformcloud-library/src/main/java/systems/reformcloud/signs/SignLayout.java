/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

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
        this.materialName = "IRON_BLOCK";
        this.materialData = 0;
    }

    @java.beans.ConstructorProperties({"lines", "materialName", "materialData"})
    public SignLayout(String[] lines, String materialName, int materialData) {
        this.lines = lines;
        this.materialName = materialName;
        this.materialData = materialData;
    }

    public String[] getLines() {
        return this.lines;
    }

    public String getMaterialName() {
        return this.materialName;
    }

    public int getMaterialData() {
        return this.materialData;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setMaterialData(int materialData) {
        this.materialData = materialData;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SignLayout)) return false;
        final SignLayout other = (SignLayout) o;
        if (!java.util.Arrays.deepEquals(this.getLines(), other.getLines())) return false;
        final Object this$materialName = this.getMaterialName();
        final Object other$materialName = other.getMaterialName();
        if (!Objects.equals(this$materialName, other$materialName))
            return false;
        if (this.getMaterialData() != other.getMaterialData()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getLines());
        final Object $materialName = this.getMaterialName();
        result = result * PRIME + ($materialName == null ? 43 : $materialName.hashCode());
        result = result * PRIME + this.getMaterialData();
        return result;
    }

    public String toString() {
        return "SignLayout(lines=" + java.util.Arrays.deepToString(this.getLines()) + ", materialName=" + this.getMaterialName() + ", materialData=" + this.getMaterialData() + ")";
    }

    /**
     * The loading layout sub class
     */
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

        @java.beans.ConstructorProperties({"perSecondAnimation", "currentAnimation", "layouts"})
        public LoadingLayout(int perSecondAnimation, int currentAnimation, SignLayout[] layouts) {
            this.perSecondAnimation = perSecondAnimation;
            this.currentAnimation = currentAnimation;
            this.layouts = layouts;
        }

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

        public int getPerSecondAnimation() {
            return this.perSecondAnimation;
        }

        int getCurrentAnimation() {
            return this.currentAnimation;
        }

        SignLayout[] getLayouts() {
            return this.layouts;
        }

        public void setPerSecondAnimation(int perSecondAnimation) {
            this.perSecondAnimation = perSecondAnimation;
        }

        public void setCurrentAnimation(int currentAnimation) {
            this.currentAnimation = currentAnimation;
        }

        public void setLayouts(SignLayout[] layouts) {
            this.layouts = layouts;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof LoadingLayout)) return false;
            final LoadingLayout other = (LoadingLayout) o;
            if (!other.canEqual(this)) return false;
            if (this.getPerSecondAnimation() != other.getPerSecondAnimation()) return false;
            if (!java.util.Arrays.deepEquals(this.getLayouts(), other.getLayouts())) return false;
            return true;
        }

        boolean canEqual(final Object other) {
            return other instanceof LoadingLayout;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + this.getPerSecondAnimation();
            result = result * PRIME + java.util.Arrays.deepHashCode(this.getLayouts());
            return result;
        }

        public String toString() {
            return "SignLayout.LoadingLayout(perSecondAnimation=" + this.getPerSecondAnimation() + ", currentAnimation=" + this.getCurrentAnimation() + ", layouts=" + java.util.Arrays.deepToString(this.getLayouts()) + ")";
        }
    }

    /**
     * The group layout sub class
     */
    public static class GroupLayout {
        private SignLayout maintenanceLayout, emptyLayout, fullLayout, onlineLayout;

        @java.beans.ConstructorProperties({"maintenanceLayout", "emptyLayout", "fullLayout", "onlineLayout"})
        public GroupLayout(SignLayout maintenanceLayout, SignLayout emptyLayout, SignLayout fullLayout, SignLayout onlineLayout) {
            this.maintenanceLayout = maintenanceLayout;
            this.emptyLayout = emptyLayout;
            this.fullLayout = fullLayout;
            this.onlineLayout = onlineLayout;
        }

        public SignLayout getMaintenanceLayout() {
            return this.maintenanceLayout;
        }

        public SignLayout getEmptyLayout() {
            return this.emptyLayout;
        }

        public SignLayout getFullLayout() {
            return this.fullLayout;
        }

        public SignLayout getOnlineLayout() {
            return this.onlineLayout;
        }

        public void setMaintenanceLayout(SignLayout maintenanceLayout) {
            this.maintenanceLayout = maintenanceLayout;
        }

        public void setEmptyLayout(SignLayout emptyLayout) {
            this.emptyLayout = emptyLayout;
        }

        public void setFullLayout(SignLayout fullLayout) {
            this.fullLayout = fullLayout;
        }

        public void setOnlineLayout(SignLayout onlineLayout) {
            this.onlineLayout = onlineLayout;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof GroupLayout)) return false;
            final GroupLayout other = (GroupLayout) o;
            if (!other.canEqual(this)) return false;
            final Object this$maintenanceLayout = this.getMaintenanceLayout();
            final Object other$maintenanceLayout = other.getMaintenanceLayout();
            if (!Objects.equals(this$maintenanceLayout, other$maintenanceLayout))
                return false;
            final Object this$emptyLayout = this.getEmptyLayout();
            final Object other$emptyLayout = other.getEmptyLayout();
            if (!Objects.equals(this$emptyLayout, other$emptyLayout))
                return false;
            final Object this$fullLayout = this.getFullLayout();
            final Object other$fullLayout = other.getFullLayout();
            if (!Objects.equals(this$fullLayout, other$fullLayout))
                return false;
            final Object this$onlineLayout = this.getOnlineLayout();
            final Object other$onlineLayout = other.getOnlineLayout();
            if (!Objects.equals(this$onlineLayout, other$onlineLayout))
                return false;
            return true;
        }

        boolean canEqual(final Object other) {
            return other instanceof GroupLayout;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $maintenanceLayout = this.getMaintenanceLayout();
            result = result * PRIME + ($maintenanceLayout == null ? 43 : $maintenanceLayout.hashCode());
            final Object $emptyLayout = this.getEmptyLayout();
            result = result * PRIME + ($emptyLayout == null ? 43 : $emptyLayout.hashCode());
            final Object $fullLayout = this.getFullLayout();
            result = result * PRIME + ($fullLayout == null ? 43 : $fullLayout.hashCode());
            final Object $onlineLayout = this.getOnlineLayout();
            result = result * PRIME + ($onlineLayout == null ? 43 : $onlineLayout.hashCode());
            return result;
        }

        public String toString() {
            return "SignLayout.GroupLayout(maintenanceLayout=" + this.getMaintenanceLayout() + ", emptyLayout=" + this.getEmptyLayout() + ", fullLayout=" + this.getFullLayout() + ", onlineLayout=" + this.getOnlineLayout() + ")";
        }
    }

    /**
     * The template layout sub class
     */
    public static class TemplateLayout {
        private SignLayout emptyLayout, fullLayout, onlineLayout;

        @java.beans.ConstructorProperties({"emptyLayout", "fullLayout", "onlineLayout"})
        public TemplateLayout(SignLayout emptyLayout, SignLayout fullLayout, SignLayout onlineLayout) {
            this.emptyLayout = emptyLayout;
            this.fullLayout = fullLayout;
            this.onlineLayout = onlineLayout;
        }

        public SignLayout getEmptyLayout() {
            return this.emptyLayout;
        }

        public SignLayout getFullLayout() {
            return this.fullLayout;
        }

        public SignLayout getOnlineLayout() {
            return this.onlineLayout;
        }

        public void setEmptyLayout(SignLayout emptyLayout) {
            this.emptyLayout = emptyLayout;
        }

        public void setFullLayout(SignLayout fullLayout) {
            this.fullLayout = fullLayout;
        }

        public void setOnlineLayout(SignLayout onlineLayout) {
            this.onlineLayout = onlineLayout;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TemplateLayout)) return false;
            final TemplateLayout other = (TemplateLayout) o;
            if (!other.canEqual(this)) return false;
            final Object this$emptyLayout = this.getEmptyLayout();
            final Object other$emptyLayout = other.getEmptyLayout();
            if (!Objects.equals(this$emptyLayout, other$emptyLayout))
                return false;
            final Object this$fullLayout = this.getFullLayout();
            final Object other$fullLayout = other.getFullLayout();
            if (!Objects.equals(this$fullLayout, other$fullLayout))
                return false;
            final Object this$onlineLayout = this.getOnlineLayout();
            final Object other$onlineLayout = other.getOnlineLayout();
            if (!Objects.equals(this$onlineLayout, other$onlineLayout))
                return false;
            return true;
        }

        boolean canEqual(final Object other) {
            return other instanceof TemplateLayout;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $emptyLayout = this.getEmptyLayout();
            result = result * PRIME + ($emptyLayout == null ? 43 : $emptyLayout.hashCode());
            final Object $fullLayout = this.getFullLayout();
            result = result * PRIME + ($fullLayout == null ? 43 : $fullLayout.hashCode());
            final Object $onlineLayout = this.getOnlineLayout();
            result = result * PRIME + ($onlineLayout == null ? 43 : $onlineLayout.hashCode());
            return result;
        }

        public String toString() {
            return "SignLayout.TemplateLayout(emptyLayout=" + this.getEmptyLayout() + ", fullLayout=" + this.getFullLayout() + ", onlineLayout=" + this.getOnlineLayout() + ")";
        }
    }
}
