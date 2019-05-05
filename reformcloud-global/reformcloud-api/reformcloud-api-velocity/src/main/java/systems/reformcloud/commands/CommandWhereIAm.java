/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.optional.qual.MaybePresent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class CommandWhereIAm implements Serializable, Command {
    @Override
    public void execute(@MaybePresent CommandSource commandSource, @NonNull @MaybePresent String[] strings) {
        if (!(commandSource instanceof Player)) {
            commandSource.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() + "An §cerror §7occurred")
            );
            return;
        }

        final Player proxiedPlayer = (Player) commandSource;
        if (proxiedPlayer.getCurrentServer().orElse(null) == null || proxiedPlayer.getCurrentServer().get().getServerInfo() == null) {
            proxiedPlayer.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() + "An §cerror §7occurred")
            );
            return;
        }

        ServerInfo serverInfo = ReformCloudAPIVelocity.getInstance()
                .getInternalCloudNetwork()
                .getServerProcessManager()
                .getRegisteredServerByName(proxiedPlayer.getCurrentServer().get().getServerInfo().getName());
        if (serverInfo == null) {
            proxiedPlayer.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() + "An §cerror §7occurred")
            );
            return;
        }

        proxiedPlayer.sendMessage(TextComponent.of(
                ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() +
                        "You are currently connected to §e" + serverInfo.getCloudProcess().getName() +
                        " §7on ServerGroup §e" + serverInfo.getServerGroup().getName() + "§7 (Process UniqueID: §e" +
                        serverInfo.getCloudProcess().getProcessUID() + ")")
        );
    }
}
