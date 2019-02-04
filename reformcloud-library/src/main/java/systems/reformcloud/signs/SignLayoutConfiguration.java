/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

@AllArgsConstructor
@Getter
public class SignLayoutConfiguration {
    private SignLayout.GroupLayout defaultLayout;
    private Map<String, SignLayout.GroupLayout> groupLayouts;

    private SignLayout.LoadingLayout loadingLayout;
}
