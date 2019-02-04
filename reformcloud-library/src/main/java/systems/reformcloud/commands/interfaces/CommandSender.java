/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.interfaces;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

/**
 * CommandSender interface to create a custom CommandSender
 */
public interface CommandSender {
    void sendMessage(String message);

    boolean hasPermission(String permission);
}
