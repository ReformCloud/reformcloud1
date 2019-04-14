/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.parameters;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

@AllArgsConstructor
@Data
public final class ParameterGroup implements Serializable {
    private String groupName, javaCommand;
    private List<String> preParameters, afterParameters;
}
