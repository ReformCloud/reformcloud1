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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class CommandJumpto implements Command {
    @Override
    public void execute(@MaybePresent CommandSource commandSource, @NonNull @MaybePresent String[] strings) {
        if (!(commandSource instanceof Player))
            return;

        final Player proxiedPlayer = (Player) commandSource;
        if (!proxiedPlayer.hasPermission("reformcloud.command.jumpto"))
            return;

        if (strings.length == 1) {
            if (!this.isServerRegistered(strings[0]) && VelocityBootstrap.getInstance().getProxyServer().getPlayer(strings[0]).orElse(null) == null) {
                commandSource.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-jumpto-server-player-not-found")));
                return;
            }

            if (VelocityBootstrap.getInstance().getProxyServer().getPlayer(strings[0]).orElse(null) != null) {
                proxiedPlayer.createConnectionRequest(VelocityBootstrap.getInstance().getProxyServer().getServer(VelocityBootstrap
                        .getInstance().getProxyServer().getPlayer(strings[0]).get().getCurrentServer().get().getServerInfo().getName()).get()).connect();
                proxiedPlayer.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-jumpto-success")));
                return;
            }

            if (this.isServerRegistered(strings[0])) {
                proxiedPlayer.createConnectionRequest(VelocityBootstrap.getInstance().getProxyServer().getServer(strings[0]).get()).connect();
                proxiedPlayer.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-jumpto-success")));
            }
        }
    }

    @Override
    public boolean hasPermission(CommandSource source, @NonNull String[] args) {
        return source.getPermissionValue("reformcloud.command.jumpto").asBoolean();
    }

    @Override
    public @MaybePresent List<String> suggest(@MaybePresent CommandSource source, @NonNull @MaybePresent String[] currentArgs) {
        if (!source.hasPermission("reformcloud.command.jumpto"))
            return new LinkedList<>();

        StringBuilder stringBuilder = new StringBuilder();

        Arrays.stream(currentArgs).forEach(stringBuilder::append);

        LinkedList<String> iterable = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getServerProcessManager()
                .getRegisteredServerNameProcesses().stream().filter(e -> e.startsWith(stringBuilder.substring(0))).forEach(iterable::add);
        VelocityBootstrap.getInstance().getProxyServer().getAllPlayers().stream()
                .filter(e -> e.getUsername().startsWith(stringBuilder.substring(0))).forEach(e -> iterable.add(e.getUsername()));

        return iterable;
    }

    public boolean isServerRegistered(String name) {
        return VelocityBootstrap.getInstance().getProxyServer().getAllServers()
                .stream()
                .anyMatch(e -> e.getServerInfo().getName().equals(name));
    }
}
