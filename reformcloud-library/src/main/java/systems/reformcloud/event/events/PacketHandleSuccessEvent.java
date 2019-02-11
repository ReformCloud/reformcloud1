/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

@AllArgsConstructor
@Getter
public class PacketHandleSuccessEvent extends Event implements Serializable {
    private static final long serialVersionUID = -1718385423559884194L;

    private boolean done;
    private Packet packet;

    @Override
    @Deprecated
    public void setCancelled(boolean cancelled) {
    }

    @Override
    @Deprecated
    public boolean isCancelled() {
        return false;
    }
}
