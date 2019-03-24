/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server.versions;

import lombok.Getter;

import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author _Klaro | Pasqual K. / created on 28.01.2019
 */

@Getter
public enum SpigotVersions implements Serializable {
    /**
     * Spigot Versions
     */
    SPIGOT_1_7_10("Spigot 1.7.10", "1.7.10", "https://archive.mcmirror.io/Spigot/spigot-1.7.10-SNAPSHOT-b1643.jar"),
    SPIGOT_1_8("Spigot 1.8", "1.8", "https://archive.mcmirror.io/Spigot/spigot-1.8-R0.1-SNAPSHOT.jar"),
    SPIGOT_1_8_3("Spigot 1.8.3", "1.8.3", "https://archive.mcmirror.io/Spigot/spigot-1.8.3-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_8_4("Spigot 1.8.4", "1.8.4", "https://archive.mcmirror.io/Spigot/spigot-1.8.4-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_8_5("Spigot 1.8.5", "1.8.5", "https://archive.mcmirror.io/Spigot/spigot-1.8.5-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_8_6("Spigot 1.8.6", "1.8.6", "https://archive.mcmirror.io/Spigot/spigot-1.8.6-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_8_7("Spigot 1.8.7", "1.8.7", "https://archive.mcmirror.io/Spigot/spigot-1.8.7-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_8_8("Spigot 1.8.8", "1.8.8", "https://archive.mcmirror.io/Spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_9("Spigot 1.9-R0.1-SNAPSHOT", "1.9", "https://archive.mcmirror.io/Spigot/spigot-api-1.9-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_9_2("Spigot 1.9.2", "1.9.2", "https://archive.mcmirror.io/Spigot/spigot-1.9.2-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_9_4("Spigot 1.9.4", "1.9.4", "https://archive.mcmirror.io/Spigot/spigot-1.9.4-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_10("Spigot 1.10-R0.1-SNAPSHOT", "1.10", "https://archive.mcmirror.io/Spigot/spigot-api-1.10-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_10_2("Spigot 1.10.2", "1.10.2", "https://archive.mcmirror.io/Spigot/spigot-1.10.2-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_11("Spigot 1.11-R0-SNAPSHOT", "1.11", "https://archive.mcmirror.io/Spigot/spigot-api-1.11-R0.1-SNAPSHOT-latest.jar"),
    SPIGOT_1_11_2("Spigot 1.11.2-R0-SNAPSHOT", "1.11.2", "https://archive.mcmirror.io/Spigot/spigot-api-1.11.2-R0.1-SNAPSHOT.jar"),
    SPIGOT_1_12("Spigot 1.12", "1.12", "https://mcmirror.io/files/Spigot/Spigot-1.12-596221b_d00c057-20170726-0522.jar"),
    SPIGOT_1_12_1("Spigot 1.12.1", "1.12.1", "https://mcmirror.io/files/Spigot/Spigot-1.12.1-da42974_8f47214-20170909-0744.jar"),
    SPIGOT_1_12_2("Spigot 1.12.2", "1.12.2", "https://mcmirror.io/files/Spigot/Spigot-1.12.2-e8ded36-20181110-0947.jar"),
    SPIGOT_1_13("Spigot 1.13", "1.13", "https://mcmirror.io/files/Spigot/Spigot-1.13-fe3ab0d_1bc2433-20180815-2348.jar"),
    SPIGOT_1_13_1("Spigot 1.13.1", "1.13.1", "https://mcmirror.io/files/Spigot/Spigot-1.13.1-f6a273b_1ceee63-20180926-0919.jar"),
    SPIGOT_1_13_2("Spigot 1.13.2", "1.13.2", "https://mcmirror.io/files/Spigot/Spigot-1.13.2-f56e2e7-20190106-2330.jar"),
    /**
     * Paper Versions
     */
    PAPER_1_7_10("Paper 1.7.10", "1.7.10", "https://archive.mcmirror.io/Paper/Paper-1.7.10-R0.1-SNAPSHOT-latest.jar"),
    PAPER_1_8("Paper 1.8-R0-1-SNAPSHOT", "1.8", "https://archive.mcmirror.io/Paper/Paper-1.8-R0.1-SNAPSHOT-b235.jar"),
    PAPER_1_8_3("Paper 1.8.3-R0-1-SNAPSHOT", "1.8.3", "https://archive.mcmirror.io/Paper/Paper-1.8.3-R0.1-SNAPSHOT-b253.jar"),
    PAPER_1_8_4("Paper 1.8.4-R0-1-SNAPSHOT", "1.8.4", "https://archive.mcmirror.io/Paper/Paper-1.8.4-R0.1-SNAPSHOT-latest.jar"),
    PAPER_1_8_5("Paper 1.8.5-R0-1-SNAPSHOT", "1.8.5", "https://archive.mcmirror.io/Paper/Paper-1.8.5-R0.1-SNAPSHOT-latest.jar"),
    PAPER_1_8_6("Paper 1.8.6-R0-1-SNAPSHOT", "1.8.6", "https://archive.mcmirror.io/Paper/Paper-1.8.6-R0.1-SNAPSHOT-latest.jar"),
    PAPER_1_8_7("Paper 1.8.7-R0-1-SNAPSHOT", "1.8.7", "https://archive.mcmirror.io/Paper/Paper-1.8.7-R0.1-SNAPSHOT-latest.jar"),
    PAPER_1_8_8("Paper 1.8.8-R0-1-SNAPSHOT", "1.8.8", "https://archive.mcmirror.io/Paper/Paper-1.8.8-R0.1-SNAPSHOT-latest.jar"),
    PAPER_1_11_2("Paper 1.11.2", "1.11.2", "https://archive.mcmirror.io/Paper/Paper-1.11.2-b1000.jar"),
    PAPER_1_12_2("Paper 1.12.2", "1.12.2", "https://mcmirror.io/files/Paper/Paper-1.12.2-ac69748-20181207-0309.jar"),
    PAPER_1_13_2("Paper 1.13.2", "1.13.2", "https://mcmirror.io/files/Paper/Paper-1.13.2-44e66f7-20190121-0708.jar"),
    /**
     * CraftBukkit Versions
     */
    CRAFTBUKKIT_1_7_10("CraftBukkit 1.7.10-R0-1-SNAPSHOT", "1.7.10", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.7.10-R0.1-20140808.005431-8.jar"),
    CRAFTBUKKIT_1_8("CraftBukkit 1.8", "1.8", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.8-R0.1-SNAPSHOT.jar"),
    CRAFTBUKKIT_1_8_3("CraftBukkit 1.8.3", "1.8.3", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.8.3-R0.1-SNAPSHOT.jar"),
    CRAFTBUKKIT_1_8_4("CraftBukkit 1.8.4", "1.8.4", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.8.4-R0.1-SNAPSHOT-latest.jar"),
    CRAFTBUKKIT_1_8_5("CraftBukkit 1.8.5", "1.8.5", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.8.5-R0.1-SNAPSHOT-latest.jar"),
    CRAFTBUKKIT_1_8_6("CraftBukkit 1.8.6", "1.8.6", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.8.6-R0.1-SNAPSHOT-latest.jar"),
    CRAFTBUKKIT_1_8_7("CraftBukkit 1.8.7", "1.8.7", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.8.7-R0.1-SNAPSHOT-latest.jar"),
    CRAFTBUKKIT_1_8_8("CraftBukkit 1.8.8", "1.8.8", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.8.8-R0.1-SNAPSHOT-latest.jar"),
    CRAFTBUKKIT_1_9("CraftBukkit 1.9", "1.9", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.9-R0.1-SNAPSHOT.jar"),
    CRAFTBUKKIT_1_9_2("CraftBukkit 1.9.2", "1.9.2", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.9.2-R0.1-SNAPSHOT.jar"),
    CRAFTBUKKIT_1_9_4("CraftBukkit 1.9.4", "1.9.4", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.9.4-R0.1-SNAPSHOT.jar"),
    CRAFTBUKKIT_1_10("CraftBukkit 1.10", "1.10", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.10-R0.1-SNAPSHOT-latest.jar"),
    CRAFTBUKKIT_1_10_2("CraftBukkit 1.10.2", "1.10.2", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.10.2-R0.1-SNAPSHOT.jar"),
    CRAFTBUKKIT_1_11("CraftBukkit 1.11", "1.11", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.11-R0.1-SNAPSHOT.jar"),
    CRAFTBUKKIT_1_11_2("CraftBukkit 1.11.2", "1.11.2", "https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.11.2-R0.1-SNAPSHOT.jar"),
    CRAFTBUKKIT_1_12_2("CraftBukkit 1.12.2", "1.12.2", "https://mcmirror.io/files/CraftBukkit/CraftBukkit-1.12.2-acbc348-20181110-0937.jar"),
    CRAFTBUKKIT_1_13_2("CraftBukkit 1.13.2", "1.13.2", "https://mcmirror.io/files/CraftBukkit/CraftBukkit-1.13.2-1a64d4a-20190120-2339.jar"),
    /**
     * ShortSpigot Versions
     */
    SHORTSPIGOT_1_12_2("ShortSpigot 1.12.2", "1.12.2", "https://dl.shortspigot.sh/file/latest"),
    /**
     * GlowStone Versions
     */
    GLOWSTONE_1_12_2("Glowstone 1.12.2", "1.12.2", "https://github.com/GlowstoneMC/Glowstone/releases/download/2018.9.0/glowstone.jar");

    public static final Map<String, SpigotVersions> PROVIDERS = new ConcurrentHashMap<>();
    public static final Deque<String> AVAILABLE_VERSIONS = new ConcurrentLinkedDeque<>();

    static {
        for (SpigotVersions SpigotVersions : values()) {
            if (!PROVIDERS.containsKey(SpigotVersions.name()))
                PROVIDERS.put(SpigotVersions.name(), SpigotVersions);

            if (!AVAILABLE_VERSIONS.contains(SpigotVersions.version)) {
                AVAILABLE_VERSIONS.add(SpigotVersions.version);
            }
        }
    }

    public static SpigotVersions getByName(final String name) {
        return PROVIDERS.getOrDefault(name.toUpperCase(), null);
    }

    public static TreeMap<String, SpigotVersions> sorted() {
        return new TreeMap<>(PROVIDERS);
    }

    public static TreeMap<String, SpigotVersions> sortedByVersion(final String version) {
        Map<String, SpigotVersions> stringSpigotVersionsMap = new HashMap<>();
        PROVIDERS.values().stream().filter(e -> e.version.equalsIgnoreCase(version)).forEach(e -> stringSpigotVersionsMap.put(e.name, e));
        return new TreeMap<>(stringSpigotVersionsMap);
    }

    public static String getAsFormattedJarFileName(SpigotVersions SpigotVersions) {
        return SpigotVersions.name.toLowerCase().replace(" ", "-") + ".jar";
    }

    private final String name, version, url;

    SpigotVersions(final String name, final String version, final String url) {
        this.name = name;
        this.version = version;
        this.url = url;
    }
}
