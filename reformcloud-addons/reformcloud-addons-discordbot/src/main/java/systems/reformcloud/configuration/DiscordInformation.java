/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class DiscordInformation implements Serializable {
    private String token, channelID, game;

    @java.beans.ConstructorProperties({"token", "channelID", "game"})
    public DiscordInformation(String token, String channelID, String game) {
        this.token = token;
        this.channelID = channelID;
        this.game = game;
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
}
