/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.port;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 09.08.2019
 */

public final class PortManagement implements Serializable {

    public static final PortManagement INTERNAL_INSTANCE = new PortManagement();

    private final List<Integer> usedPorts = new LinkedList<>();

    public Integer nextPortAndRegister(int startPort) {
        while (this.usedPorts.contains(startPort) || !isPortInUse(startPort)) {
            startPort++;
        }

        usedPorts.add(startPort);
        return startPort;
    }

    public void unregisterPort(Integer port) {
        this.usedPorts.remove(port);
    }

    private boolean isPortInUse(final int port) {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(port));
            return true;
        } catch (final IOException ignored) {
        }

        return false;
    }
}
