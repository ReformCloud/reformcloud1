/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.ingame.sender;

import systems.reformcloud.meta.info.ServerInfo;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 19.03.2019
 */

public interface IngameCommandSender {
    String getDisplayName();

    void setDisplayName(String name);

    void sendMessage(String message);

    void connect(ServerInfo server);

    ServerInfo getServer();

    int getPing();

    void sendData(String s, byte[] bytes);

    UUID getUniqueId();

    Locale getLocale();

    byte getViewDistance();

    IngameCommandSender.ChatMode getChatMode();

    boolean hasChatColors();

    IngameCommandSender.MainHand getMainHand();

    void resetTabHeader();

    void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay);

    void clearTitle();

    boolean isForgeUser();

    Map<String, String> getModList();

    public enum MainHand {
        LEFT,
        RIGHT
    }

    public enum ChatMode {
        SHOWN,
        COMMANDS_ONLY,
        HIDDEN
    }
}
