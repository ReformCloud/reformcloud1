/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.player.DefaultPlayer;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.signs.SignLayoutConfiguration;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.lang.reflect.Type;

/**
 * @author _Klaro | Pasqual K. / created on 05.12.2018
 */

public final class TypeTokenAdaptor {
    @Getter
    private static final Type
            SERVER_GROUP_TYPE = new TypeToken<ServerGroup>() {
    }.getType(),
            PROXY_GROUP_TYPE = new TypeToken<ProxyGroup>() {
            }.getType(),
            SERVER_INFO_TYPE = new TypeToken<ServerInfo>() {
            }.getType(),
            PROXY_INFO_TYPE = new TypeToken<ProxyInfo>() {
            }.getType(),
            SERVER_STARTUP_INFO_TYPE = new TypeToken<ServerStartupInfo>() {
            }.getType(),
            PROXY_STARTUP_INFO_TYPE = new TypeToken<ProxyStartupInfo>() {
            }.getType(),
            INTERNAL_CLOUD_NETWORK_TYPE = new TypeToken<InternalCloudNetwork>() {
            }.getType(),
            PACKET_TYPE = new TypeToken<Packet>() {
            }.getType(),
            ETHERNET_ADDRESS_TYPE = new TypeToken<EthernetAddress>() {
            }.getType(),
            AUTHENTICATION_TYPE = new TypeToken<AuthenticationType>() {
            }.getType(),
            SIGN_LAYOUT_CONFIG_TYPE = new TypeToken<SignLayoutConfiguration>() {
            }.getType(),
            OFFLINE_PLAYER_TYPE = new TypeToken<OfflinePlayer>() {
            }.getType(),
            ONLINE_PLAYER_TYPE = new TypeToken<OnlinePlayer>() {
            }.getType(),
            DEFAULT_PLAYER_TYPE = new TypeToken<DefaultPlayer>() {
            }.getType(),
            PERMISSION_CACHE_TYPE = new TypeToken<PermissionCache>() {
            }.getType(),
            PERMISSION_HOLDER_TYPE = new TypeToken<PermissionHolder>() {
            }.getType(),
            CLIENT_INFO_TYPE = new TypeToken<ClientInfo>() {
            }.getType();
}
