/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.cloudsystem;

import systems.reformcloud.utility.annotiations.ForRemoval;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@Deprecated
@ForRemoval
public final class EthernetAddress implements Serializable {

    /**
     * The host of the address
     */
    private String host;

    /**
     * The port of the address
     */
    private int port;

    @java.beans.ConstructorProperties({"host", "port"})
    public EthernetAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }
}
