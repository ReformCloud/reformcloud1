/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.signs.map.TemplateMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

@AllArgsConstructor
@Getter
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
}
