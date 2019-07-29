package systems.reformcloud.player.version;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 22.02.2019
 */

public enum SpigotVersion implements Serializable {
    V1_14_4(498),    
    V1_14_3(490),
    V1_14_2(485),
    V1_14_1(480),
    V1_14(477),
    V1_13_2(404),
    V1_13_1(401),
    V1_13(393),
    V1_12_2(340),
    V1_12_1(338),
    V1_12(335),
    V1_11_2(316),
    V1_11_1(316),
    V1_11(315),
    V1_10_2(210),
    V1_10_1(210),
    V1_10(210),
    V1_9_4(110),
    V1_9_3(110),
    V1_9_2(109),
    V1_9_1(108),
    V1_9(107),
    V1_8_9(47),
    V1_8_8(47),
    V1_8_7(47),
    V1_8_6(47),
    V1_8_5(47),
    V1_8_4(47),
    V1_8_3(47),
    V1_8_2(47),
    V1_8_1(47),
    V1_8(47),
    V1_7_10(5),
    V1_7_9(5),
    V1_7_8(5),
    V1_7_7(5),
    V1_7_6(5),
    V1_7_5(4),
    V1_7_4(4),
    V1_7_2(4);

    private int protocolId;

    SpigotVersion(int protocolId) {
        this.protocolId = protocolId;
    }

    private static final Map<Integer, SpigotVersion> BY_PROTOCOL_ID = new HashMap<>();

    static {
        for (SpigotVersion version : values()) {
            if (!BY_PROTOCOL_ID.containsKey(version.protocolId)) {
                BY_PROTOCOL_ID.put(version.protocolId, version);
            }
        }
    }

    public static SpigotVersion getByProtocolId(int protocolId) {
        if (protocolId < 4) {
            return V1_13_2;
        }

        return BY_PROTOCOL_ID.get(protocolId);
    }
}
