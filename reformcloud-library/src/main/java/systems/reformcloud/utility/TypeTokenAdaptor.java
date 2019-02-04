/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.netty.authentication.enums.AuthenticationType;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.signs.SignLayoutConfiguration;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import lombok.Getter;

import java.lang.reflect.Type;

/**
 * @author _Klaro | Pasqual K. / created on 05.12.2018
 */

public final class TypeTokenAdaptor {
    @Getter
    private static final Type
            serverGroupType = new TypeToken<ServerGroup>() {
    }.getType(),
            proxyGroupType = new TypeToken<ProxyGroup>() {
            }.getType(),
            serverInfoType = new TypeToken<ServerInfo>() {
            }.getType(),
            proxyInfoType = new TypeToken<ProxyInfo>() {
            }.getType(),
            serverStartupInfoType = new TypeToken<ServerStartupInfo>() {
            }.getType(),
            proxyStartupInfoType = new TypeToken<ProxyStartupInfo>() {
            }.getType(),
            internalCloudNetworkType = new TypeToken<InternalCloudNetwork>() {
            }.getType(),
            packetType = new TypeToken<Packet>() {
            }.getType(),
            ethernetAddressType = new TypeToken<EthernetAddress>() {
            }.getType(),
            authenticationType = new TypeToken<AuthenticationType>() {
            }.getType(),
            signLayoutConfigType = new TypeToken<SignLayoutConfiguration>() {
            }.getType(),
            clientInfoType = new TypeToken<ClientInfo>() {
            }.getType();
}
