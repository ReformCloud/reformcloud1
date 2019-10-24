/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.update;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.04.2019
 */

public final class InternalCloudNetworkUpdateEvent extends Event implements Serializable {

    private InternalCloudNetwork internalCloudNetwork;

    @java.beans.ConstructorProperties({"internalCloudNetwork"})
    public InternalCloudNetworkUpdateEvent(InternalCloudNetwork internalCloudNetwork) {
        this.internalCloudNetwork = internalCloudNetwork;
    }

    public InternalCloudNetwork getInternalCloudNetwork() {
        return this.internalCloudNetwork;
    }
}
