/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import netscape.security.UserTarget;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class DiscordInformation implements Serializable {

    private String token;

    private String channelID;

    private String game;

    private String status;

    @java.beans.ConstructorProperties({"token", "channelID", "game", "status"})
    public DiscordInformation(String token, String channelID, String game, String status) {
        this.token = token;
        this.channelID = channelID;
        this.game = game;
        this.status = status;
    }

    public String getToken() {
        return this.token;
    }

    public String getChannelID() {
        return this.channelID;
    }

    public String getGame() {
        return this.game;
    }

    public String getStatus() {
        return this.status;
    }
}
