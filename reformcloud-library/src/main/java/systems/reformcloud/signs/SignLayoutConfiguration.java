/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.utility.map.Trio;

import java.util.List;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

@AllArgsConstructor
@Getter
public class SignLayoutConfiguration {
    private SignLayout.GroupLayout defaultLayout;
    private Map<String, SignLayout.GroupLayout> groupLayouts;
    private List<Trio<String, String, SignLayout>> groupTemplateLayouts;

    private SignLayout.LoadingLayout loadingLayout;
}
