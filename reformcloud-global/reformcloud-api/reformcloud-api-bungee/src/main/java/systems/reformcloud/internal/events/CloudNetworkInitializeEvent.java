/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.io.Serializable;

/**
 * This class represents the Initialize Event if the CloudSystem
 *
 * @since RCS1.0
 * @author _Klaro | Pasqual K. / created on 17.02.2019
 */

@AllArgsConstructor
@Getter
public final class CloudNetworkInitializeEvent extends Event implements Serializable {
    private static final long serialVersionUID = 1709467379313456906L;

    private InternalCloudNetwork internalCloudNetwork;
}
