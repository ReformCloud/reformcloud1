/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packet.constants;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 16.06.2019
 */

public final class ChannelConstants implements Serializable {

    public static final long REFORMCLOUD_INTERNAL_INFORMATION_DEFAULT_CHANNEL =
        918277418828850001L;

    public static final long REFORMCLOUD_INTERNAL_QUERY_INFORMATION_DEFAULT_CHANNEL =
        918277418828850001L;

    public static final long REFORMCLOUD_AUTHENTICATION_CHANNEL =
        1582817960060533060L;

    public static final long REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL =
        5783412062793582804L;

    public static final long REFORMCLOUD_PROCESS_CHANNEL =
        5834603807119075018L;

    public static final long REFORMCLOUD_PLAYER_CHANNEL =
        136407928607427553L;
}
