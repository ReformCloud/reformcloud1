/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import systems.reformcloud.signs.map.TemplateMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

public final class SignLayoutConfiguration implements Serializable {
    /**
     * The default layout which will be used when no layout is configured for the current group
     */
    private SignLayout.GroupLayout defaultLayout;

    /**
     * All registered group layouts
     */
    private Map<String, SignLayout.GroupLayout> groupLayouts;

    /**
     * All registered template layouts
     */
    private List<TemplateMap<String, String, SignLayout.TemplateLayout>> groupTemplateLayouts;

    /**
     * The loading layout
     */
    private SignLayout.LoadingLayout loadingLayout;

    @java.beans.ConstructorProperties({"defaultLayout", "groupLayouts", "groupTemplateLayouts", "loadingLayout"})
    public SignLayoutConfiguration(SignLayout.GroupLayout defaultLayout, Map<String, SignLayout.GroupLayout> groupLayouts, List<TemplateMap<String, String, SignLayout.TemplateLayout>> groupTemplateLayouts, SignLayout.LoadingLayout loadingLayout) {
        this.defaultLayout = defaultLayout;
        this.groupLayouts = groupLayouts;
        this.groupTemplateLayouts = groupTemplateLayouts;
        this.loadingLayout = loadingLayout;
    }

    public SignLayout.GroupLayout getDefaultLayout() {
        return this.defaultLayout;
    }

    public Map<String, SignLayout.GroupLayout> getGroupLayouts() {
        return this.groupLayouts;
    }

    public List<TemplateMap<String, String, SignLayout.TemplateLayout>> getGroupTemplateLayouts() {
        return this.groupTemplateLayouts;
    }

    public SignLayout.LoadingLayout getLoadingLayout() {
        return this.loadingLayout;
    }
}
