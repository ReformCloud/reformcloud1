/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.client;

import systems.reformcloud.meta.info.ClientInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public class Client implements Serializable {

    private static final long serialVersionUID = 7702400116714803106L;

    /**
     * General information about the client
     */
    private String name, ip;

    /**
     * The client info of the client
     */
    private ClientInfo clientInfo;

    @java.beans.ConstructorProperties({"name", "ip", "clientInfo"})
    public Client(String name, String ip, ClientInfo clientInfo) {
        this.name = name;
        this.ip = ip;
        this.clientInfo = clientInfo;
    }

    /**
     * Converts the client info into a client state
     *
     * @return The converted client state
     */
    public ClientState getCurrentState() {
        if (clientInfo == null) {
            return ClientState.DISCONNECTED;
        } else if (clientInfo.isReady()) {
            return ClientState.READY;
        }

        return ClientState.CONNECTED;
    }

    public String getName() {
        return this.name;
    }

    public String getIp() {
        return this.ip;
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public enum ClientState {
        DISCONNECTED,
        CONNECTED,
        READY
    }
}
