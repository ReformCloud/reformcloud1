/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.bootstrap;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import systems.reformcloud.utility.Dependency;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.netty.util.ResourceLeakDetector;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.addons.dependency.DependencyLoader;
import systems.reformcloud.addons.dependency.util.DynamicDependency;
import systems.reformcloud.commands.CommandHub;
import systems.reformcloud.commands.CommandJumpto;
import systems.reformcloud.commands.CommandReformCloud;
import systems.reformcloud.commands.CommandWhereIAm;
import systems.reformcloud.listener.CloudAddonsListener;
import systems.reformcloud.listener.CloudConnectListener;
import systems.reformcloud.listener.CloudProcessListener;
import systems.reformcloud.listener.CloudProxyPingListener;

/**
 * @author _Klaro | Pasqual K. / created on 24.03.2019
 */

@Plugin(id = "reformcloudapi",
    name = "ReformCloudAPIVelocity",
    version = "1.0",
    description = "Default ReformCloudAPI for Velocity",
    authors = {"_Klaro"},
    url = "https://reformcloud.systems"
)
public final class VelocityBootstrap implements Serializable {

    private static final LegacyChannelIdentifier LEGACY_BUNGEE_CHANNEL = new LegacyChannelIdentifier(
        "BungeeCord");
    private static final MinecraftChannelIdentifier MODERN_BUNGEE_CHANNEL = MinecraftChannelIdentifier
        .create("bungeecord", "main");

    private static VelocityBootstrap instance;

    private ProxyServer proxy;

    @Inject
    public VelocityBootstrap(ProxyServer proxyServer, Logger logger) {
        DependencyLoader.loadDependency(new DynamicDependency(null) {
            @Override
            public String getGroupID() {
                return "io.netty";
            }

            @Override
            public String getName() {
                return "netty-all";
            }

            @Override
            public String getVersion() {
                return version;
            }
            
            @Override
            public Dependency setVersion(String version)
                this.version = version;
                return this;
            }
        });
        instance = this;

        this.proxy = proxyServer;
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
        proxy.getConfiguration().getAttemptConnectionOrder().clear();
    }

    public static VelocityBootstrap getInstance() {
        return VelocityBootstrap.instance;
    }

    public ProxyServer getProxyServer() {
        return this.proxy;
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(ProxyInitializeEvent event) {
        proxy.getChannelRegistrar().register(LEGACY_BUNGEE_CHANNEL, MODERN_BUNGEE_CHANNEL);

        Arrays.asList(
            new CloudProxyPingListener(),
            new CloudProcessListener(),
            new CloudAddonsListener(),
            new CloudConnectListener()
        ).forEach(listener -> this.proxy.getEventManager().register(this, listener));

        this.proxy.getCommandManager().register(new CommandJumpto(), "jumpto", "goto", "jt", "gt");
        this.proxy.getCommandManager().register(new CommandHub(), "hub", "l", "lobby", "leave");
        this.proxy.getCommandManager().register(new CommandReformCloud(), "reformcloud");
        this.proxy.getCommandManager().register(new CommandWhereIAm(), "whereiam", "whereami");

        try {
            new ReformCloudAPIVelocity();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Subscribe
    public void handle(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(LEGACY_BUNGEE_CHANNEL) && !event.getIdentifier()
            .equals(MODERN_BUNGEE_CHANNEL)) {
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }

        ServerConnection connection = (ServerConnection) event.getSource();
        ByteArrayDataInput in = event.dataAsDataStream();
        String subChannel = in.readUTF();

        if (subChannel.equals("Connect")) {
            Optional<RegisteredServer> info = proxy.getServer(in.readUTF());
            info.ifPresent(serverInfo -> connection.getPlayer().createConnectionRequest(serverInfo)
                .fireAndForget());
        }
    }

    public ProxyServer getProxy() {
        return this.proxy;
    }
}
