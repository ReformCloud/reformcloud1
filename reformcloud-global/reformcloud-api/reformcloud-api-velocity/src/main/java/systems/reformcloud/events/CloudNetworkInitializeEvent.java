/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.io.Serializable;

/**
 * This class represents the Initialize Event if the CloudSystem
 *
 * @author _Klaro | Pasqual K. / created on 17.02.2019
 */

@AllArgsConstructor
@Getter
public final class CloudNetworkInitializeEvent implements Serializable {
    private static final long serialVersionUID = 1709467379313456906L;

    private InternalCloudNetwork internalCloudNetwork;
}
