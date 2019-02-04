package systems.reformcloud.event.enums;

import systems.reformcloud.utility.StringUtil;
import lombok.Getter;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

@Getter
public enum EventTargetType {
    LOAD_SUCCESS(1, "loading success"),
    PACKET_HANDLE_SUCCESS(2, "packet handle success"),
    INCOMING_PACKET(3, "incoming packet"),
    OUTGOING_PACKET(4, "packet outgoing"),
    PROCESS_REGISTERED(5, "process registering..."),
    PROCESS_UNREGISTERED(6, "process close"),
    NOT_DEFINED(10, StringUtil.NULL);

    EventTargetType(final int id, final String abstractName) {
        this.id = id;
        this.name = abstractName;
    }

    private final int id;
    private final String name;
}
