/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class PacketOutDispatchConsoleCommand extends Packet {
    public PacketOutDispatchConsoleCommand(final String command) {
        super("DispatchCommandLine", new Configuration().addValue("command", command));
    }
}
