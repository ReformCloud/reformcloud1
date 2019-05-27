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
    /**
     * Get the display name of the sender
     *
     * @return      The display name of the sender
     */
    String getDisplayName();

    /**
     * Sets the display name of the command sender
     *
     * @param name      The new name of the command sender
     */
    void setDisplayName(String name);

    /**
     * Sends a message to the command sender
     *
     * @param message       The message which should be send
     */
    void sendMessage(String message);

    /**
     * Connects the player to the given server
     *
     * @param server        The server info of the server
     */
    void connect(ServerInfo server);

    /**
     * Get the current server of the player
     *
     * @return      The server info of the current server
     */
    ServerInfo getServer();

    /**
     * Get the ping of the online player
     *
     * @return      The ping of the command sender
     */
    int getPing();

    /**
     * Send a message to the command sender
     *
     * @param s         The channel for the message
     * @param bytes     The message as byte array
     */
    void sendData(String s, byte[] bytes);

    /**
     * Get the uuid of the player
     *
     * @return the uuid of the player
     */
    UUID getUniqueId();

    /**
     * Get the locale of the player
     *
     * @return the player locale
     */
    Locale getLocale();

    /**
     * Get the view distance of the player
     *
     * @return The view distance of the player
     */
    byte getViewDistance();

    /**
     * The chat mode of the player
     *
     * @return The current chat mode of the player
     */
    IngameCommandSender.ChatMode getChatMode();

    /**
     * Get if the player has chat colour enabled
     *
     * @return If the player uses chat color
     */
    boolean hasChatColors();

    /**
     * Get the side of the main hand
     *
     * @return The main hand side
     */
    IngameCommandSender.MainHand getMainHand();

    /**
     * Resets the tab header of the player
     */
    void resetTabHeader();

    /**
     * Sends a title
     *
     * @param title         The main title
     * @param subTitle      The sub title
     * @param fadeIn        The fade in time
     * @param fadeOut       The fade out time
     * @param stay          The stay time
     */
    void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay);

    /**
     * Clears the title
     */
    void clearTitle();

    /**
     * Get if the player uses forge
     *
     * @return If the player uses forge
     */
    boolean isForgeUser();

    /**
     * Get the mod list of the player
     *
     * @return The list of all mods which the player uses
     */
    Map<String, String> getModList();

    /**
     * The main hand enum to get the main hand side
     */
    enum MainHand {
        LEFT,
        RIGHT
    }

    /**
     * The chat modes
     */
    enum ChatMode {
        SHOWN,
        COMMANDS_ONLY,
        HIDDEN
    }
}
