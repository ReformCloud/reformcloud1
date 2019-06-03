/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.io.Serializable;

/**
 * This class represents the Initialize Event if the CloudSystem
 *
 * @author _Klaro | Pasqual K. / created on 17.02.2019
 */

public final class CloudNetworkInitializeEvent implements Serializable {

    private static final long serialVersionUID = 1709467379313456906L;

    private InternalCloudNetwork internalCloudNetwork;

    @java.beans.ConstructorProperties({"internalCloudNetwork"})
    public CloudNetworkInitializeEvent(InternalCloudNetwork internalCloudNetwork) {
        this.internalCloudNetwork = internalCloudNetwork;
    }

    public InternalCloudNetwork getInternalCloudNetwork() {
        return this.internalCloudNetwork;
    }
}
