/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.optional.qual.MaybePresent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.info.ServerInfo;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

public final class CommandHub implements Command {

    @Override
    public void execute(@MaybePresent CommandSource commandSource,
        @NonNull @MaybePresent String[] strings) {
        if (!(commandSource instanceof Player)) {
            return;
        }

        final Player proxiedPlayer = (Player) commandSource;
        if (ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getServerProcessManager()
            .getRegisteredServerByName(
                proxiedPlayer.getCurrentServer().get().getServerInfo().getName()) != null
            && ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
            .getServerProcessManager()
            .getRegisteredServerByName(
                proxiedPlayer.getCurrentServer().get().getServerInfo().getName())
            .getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
            proxiedPlayer.sendMessage(net.kyori.text.TextComponent
                .of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-hub-already")));
            return;
        }

        final ServerInfo fallback = ReformCloudAPIVelocity.getInstance().nextFreeLobby(
            ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup(), proxiedPlayer
        );
        if (fallback == null) {
            proxiedPlayer.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance()
                .getInternalCloudNetwork()
                .getMessage("internal-api-bungee-command-hub-not-available")));
        } else {
            proxiedPlayer.createConnectionRequest(VelocityBootstrap.getInstance().getProxyServer()
                .getServer(fallback.getCloudProcess().getName()).get()).connect();
        }
    }
}
