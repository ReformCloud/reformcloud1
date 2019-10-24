/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.meta.client.settings.ClientSettings;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.player.DefaultPlayer;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.signs.SignLayoutConfiguration;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author _Klaro | Pasqual K. / created on 05.12.2018
 */

public final class TypeTokenAdaptor implements Serializable {

    /**
     * Some helpful type tokens for the cloud system
     */
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
        PACKET_TYPE = new TypeToken<DefaultPacket>() {
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
        CLIENT_SETTING_TYPE = new TypeToken<ClientSettings>() {
        }.getType(),
        CLIENT_INFO_TYPE = new TypeToken<ClientInfo>() {
        }.getType();

    public static Type getSERVER_GROUP_TYPE() {
        return TypeTokenAdaptor.SERVER_GROUP_TYPE;
    }

    public static Type getPROXY_GROUP_TYPE() {
        return TypeTokenAdaptor.PROXY_GROUP_TYPE;
    }

    public static Type getSERVER_INFO_TYPE() {
        return TypeTokenAdaptor.SERVER_INFO_TYPE;
    }

    public static Type getPROXY_INFO_TYPE() {
        return TypeTokenAdaptor.PROXY_INFO_TYPE;
    }

    public static Type getSERVER_STARTUP_INFO_TYPE() {
        return TypeTokenAdaptor.SERVER_STARTUP_INFO_TYPE;
    }

    public static Type getPROXY_STARTUP_INFO_TYPE() {
        return TypeTokenAdaptor.PROXY_STARTUP_INFO_TYPE;
    }

    public static Type getINTERNAL_CLOUD_NETWORK_TYPE() {
        return TypeTokenAdaptor.INTERNAL_CLOUD_NETWORK_TYPE;
    }

    public static Type getPACKET_TYPE() {
        return TypeTokenAdaptor.PACKET_TYPE;
    }

    public static Type getETHERNET_ADDRESS_TYPE() {
        return TypeTokenAdaptor.ETHERNET_ADDRESS_TYPE;
    }

    public static Type getAUTHENTICATION_TYPE() {
        return TypeTokenAdaptor.AUTHENTICATION_TYPE;
    }

    public static Type getSIGN_LAYOUT_CONFIG_TYPE() {
        return TypeTokenAdaptor.SIGN_LAYOUT_CONFIG_TYPE;
    }

    public static Type getOFFLINE_PLAYER_TYPE() {
        return TypeTokenAdaptor.OFFLINE_PLAYER_TYPE;
    }

    public static Type getONLINE_PLAYER_TYPE() {
        return TypeTokenAdaptor.ONLINE_PLAYER_TYPE;
    }

    public static Type getDEFAULT_PLAYER_TYPE() {
        return TypeTokenAdaptor.DEFAULT_PLAYER_TYPE;
    }

    public static Type getPERMISSION_CACHE_TYPE() {
        return TypeTokenAdaptor.PERMISSION_CACHE_TYPE;
    }

    public static Type getPERMISSION_HOLDER_TYPE() {
        return TypeTokenAdaptor.PERMISSION_HOLDER_TYPE;
    }

    public static Type getCLIENT_SETTING_TYPE() {
        return TypeTokenAdaptor.CLIENT_SETTING_TYPE;
    }

    public static Type getCLIENT_INFO_TYPE() {
        return TypeTokenAdaptor.CLIENT_INFO_TYPE;
    }
}
