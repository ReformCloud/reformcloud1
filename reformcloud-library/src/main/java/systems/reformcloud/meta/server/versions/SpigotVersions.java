/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server.versions;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author _Klaro | Pasqual K. / created on 28.01.2019
 */

public enum SpigotVersions implements Serializable {
    /**
     * Spigot Versions
     */
    SPIGOT_1_7_10("Spigot 1.7.10", "1.7.10",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.7.10.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.7.10.jar"),
    SPIGOT_1_8("Spigot 1.8", "1.8",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.8.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.8.jar"),
    SPIGOT_1_8_3("Spigot 1.8.3", "1.8.3",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.8.3.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.8.3.jar"),
    SPIGOT_1_8_4("Spigot 1.8.4", "1.8.4",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.8.4.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.8.4.jar"),
    SPIGOT_1_8_5("Spigot 1.8.5", "1.8.5",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.8.5.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.8.5.jar"),
    SPIGOT_1_8_6("Spigot 1.8.6", "1.8.6",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.8.6.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.8.6.jar"),
    SPIGOT_1_8_7("Spigot 1.8.7", "1.8.7",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.8.7.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.8.7.jar"),
    SPIGOT_1_8_8("Spigot 1.8.8", "1.8.8",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.8.8.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.8.8.jar"),
    SPIGOT_1_9("Spigot 1.9-R0.1-SNAPSHOT", "1.9",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.9.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.9.jar"),
    SPIGOT_1_9_2("Spigot 1.9.2", "1.9.2",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.9.2.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.9.2.jar"),
    SPIGOT_1_9_4("Spigot 1.9.4", "1.9.4",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.9.4.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.9.4.jar"),
    SPIGOT_1_10("Spigot 1.10-R0.1-SNAPSHOT", "1.10",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.10.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.10.jar"),
    SPIGOT_1_10_2("Spigot 1.10.2", "1.10.2",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.10.2.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.10.2.jar"),
    SPIGOT_1_11("Spigot 1.11-R0-SNAPSHOT", "1.11",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.11.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.11.jar"),
    SPIGOT_1_11_2("Spigot 1.11.2-R0-SNAPSHOT", "1.11.2",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.11.2.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.11.2.jar"),
    SPIGOT_1_12("Spigot 1.12", "1.12",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.12.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.12.jar"),
    SPIGOT_1_12_1("Spigot 1.12.1", "1.12.1",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.12.1.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.12.1.jar"),
    SPIGOT_1_12_2("Spigot 1.12.2", "1.12.2",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.12.2.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.12.2.jar"),
    SPIGOT_1_13("Spigot 1.13", "1.13",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.13.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.13.jar"),
    SPIGOT_1_13_1("Spigot 1.13.1", "1.13.1",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.13.1.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.13.1.jar"),
    SPIGOT_1_13_2("Spigot 1.13.2", "1.13.2",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.13.2.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.13.2.jar"),
    SPIGOT_1_14("Spigot 1.14", "1.14",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.14.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.14.jar"),
    SPIGOT_1_14_1("Spigot 1.14.1", "1.14.1",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.14.1.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.14.1.jar"),
    SPIGOT_1_14_2("Spigot 1.14.2", "1.14.2",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.14.2.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.14.2.jar"),
    SPIGOT_1_14_3("Spigot 1.14.3", "1.14.3",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.14.3.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.14.3.jar"),
    SPIGOT_1_14_4("Spigot 1.14.4", "1.14.4",
        "https://dl.reformcloud.systems/mcversions/spigots/spigot-1.14.4.jar",
        "https://internal.byteexception.eu/mcversions/spigots/spigot-1.14.4.jar"),

    /**
     * Paper Versions
     */
    PAPER_1_7_10("Paper 1.7.10", "1.7.10",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.7.10.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.7.10.jar"),
    PAPER_1_8("Paper 1.8-R0-1-SNAPSHOT", "1.8",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.8.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.8.jar"),
    PAPER_1_8_3("Paper 1.8.3-R0-1-SNAPSHOT", "1.8.3",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.8.3.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.8.3.jar"),
    PAPER_1_8_4("Paper 1.8.4-R0-1-SNAPSHOT", "1.8.4",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.8.4.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.8.4.jar"),
    PAPER_1_8_5("Paper 1.8.5-R0-1-SNAPSHOT", "1.8.5",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.8.5.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.8.5.jar"),
    PAPER_1_8_6("Paper 1.8.6-R0-1-SNAPSHOT", "1.8.6",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.8.6.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.8.6.jar"),
    PAPER_1_8_7("Paper 1.8.7-R0-1-SNAPSHOT", "1.8.7",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.8.7.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.8.7.jar"),
    PAPER_1_8_8("Paper 1.8.8-R0-1-SNAPSHOT", "1.8.8",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.8.8.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.8.8.jar"),
    PAPER_1_11_2("Paper 1.11.2", "1.11.2",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.11.2.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.11.2.jar"),
    PAPER_1_12_2("Paper 1.12.2", "1.12.2",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.12.2.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.12.2.jar"),
    PAPER_1_13_2("Paper 1.13.2", "1.13.2",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.13.2jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.13.2jar"),
    PAPER_1_14_1("Paper 1.14.1", "1.14.1",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.14.1.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.14.1.jar"),
    PAPER_1_14_2("Paper 1.14.2", "1.14.2",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.14.2.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.14.2.jar"),
    PAPER_1_14_3("Paper 1.14.3", "1.14.3",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.14.3.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.14.3.jar"),
    PAPER_1_14_4("Paper 1.14.4", "1.14.4",
        "https://dl.reformcloud.systems/mcversions/paper/paper-1.14.4.jar",
        "https://internal.byteexception.eu/mcversions/paper/paper-1.14.4.jar"),

    /**
     * SpongeVanilla Versions
     */
    SPONGEVANILLA_1_8_9("SpongeVanilla 1.8.9", "1.8.9",
        "https://dl.reformcloud.systems/mcversions/spongevanilla/spongevanilla-1.8.9.jar",
        "https://internal.byteexception.eu/mcversions/spongevanilla/spongevanilla-1.8.9.jar"),
    SPONGEVANILLA_1_9_4("SpongeVanilla 1.9.4", "1.9.4",
        "https://dl.reformcloud.systems/mcversions/spongevanilla/spongevanilla-1.9.4.jar",
        "https://internal.byteexception.eu/mcversions/spongevanilla/spongevanilla-1.9.4.jar"),
    SPONGEVANILLA_1_10_2("SpongeVanilla 1.10.2", "1.10.2",
        "https://dl.reformcloud.systems/mcversions/spongevanilla/spongevanilla-1.10.2.jar",
        "https://internal.byteexception.eu/mcversions/spongevanilla/spongevanilla-1.10.2.jar"),
    SPONGEVANILLA_1_11_2("SpongeVanilla 1.11.2", "1.11.2",
        "https://dl.reformcloud.systems/mcversions/spongevanilla/spongevanilla-1.11.2.jar",
        "https://internal.byteexception.eu/mcversions/spongevanilla/spongevanilla-1.11.2.jar"),
    SPONGEVANILLA_1_12_2("SpongeVanilla 1.12.2", "1.12.2",
        "https://dl.reformcloud.systems/mcversions/spongevanilla/spongevanilla-1.12.2.jar",
        "https://internal.byteexception.eu/mcversions/spongevanilla/spongevanilla-1.12.2.jar"),
    /**
     * SpongeForge Versions
     */
    SPONGEFORGE_1_8_9("SpongeForge 1.8.9", "1.8.9",
        "https://dl.reformcloud.systems/mcversions/forge/sponge-1.8.9.zip",
        "https://internal.byteexception.eu/mcversions/forge/sponge-1.8.9.zip"),
    SPONGEFORGE_1_10_2("SpongeForge 1.10.2", "1.10.2",
        "https://dl.reformcloud.systems/mcversions/forge/sponge-1.10.2.zip",
        "https://internal.byteexception.eu/mcversions/forge/sponge-1.10.2.zip"),
    SPONGEFORGE_1_11_2("SpongeForge 1.11.2", "1.11.2",
        "https://dl.reformcloud.systems/mcversions/forge/sponge-1.11.2.zip",
        "https://internal.byteexception.eu/mcversions/forge/sponge-1.11.2.zip"),
    SPONGEFORGE_1_12_2("SpongeForge 1.12.2", "1.12.2",
        "https://dl.reformcloud.systems/mcversions/forge/sponge-1.12.2.zip",
        "https://internal.byteexception.eu/mcversions/forge/sponge-1.12.2.zip"),
    /**
     * TacoSpigot Versions
     */
    TACO_1_8_8("Taco 1.8.8", "1.8.8",
        "https://dl.reformcloud.systems/mcversions/taco/tacospigot-1.8.8.jar",
        "https://internal.byteexception.eu/mcversions/taco/tacospigot-1.8.8.jar"),
    TACO_1_11_2("Taco 1.11.2", "1.11.2",
        "https://dl.reformcloud.systems/mcversions/taco/tacospigot-1.11.2.jar",
        "https://internal.byteexception.eu/mcversions/taco/tacospigot-1.11.2.jar"),
    TACO_1_12_2("Taco 1.12.2", "1.12.2",
        "https://dl.reformcloud.systems/mcversions/taco/tacospigot-1.12.2.jar",
        "https://internal.byteexception.eu/mcversions/taco/tacospigot-1.12.2.jar"),
    /**
     * TorchSpigot Versions
     */
    TORCH_1_8_8("Torch 1.8.8", "1.8.8",
        "https://dl.reformcloud.systems/mcversions/torch/Torch-1.8.8.jar",
        "https://internal.byteexception.eu/mcversions/torch/Torch-1.8.8.jar"),
    TORCH_1_9_4("Torch 1.9.4", "1.9.4",
        "https://dl.reformcloud.systems/mcversions/torch/Torch-1.9.4.jar",
        "https://internal.byteexception.eu/mcversions/torch/Torch-1.9.4.jar"),
    TORCH_1_11_2("Torch 1.11.2", "1.11.2",
        "https://dl.reformcloud.systems/mcversions/torch/Torch-1.11.2.jar",
        "https://internal.byteexception.eu/mcversions/torch/Torch-1.11.2.jar"),
    /**
     * Hose Versions
     */
    HOSE_1_8_8("Hose 1.8.8", "1.8.8",
        "https://dl.reformcloud.systems/mcversions/hose/hose-1.8.8.jar",
        "https://internal.byteexception.eu/mcversions/hose/hose-1.8.8.jar"),
    HOSE_1_9_4("Hose 1.9.4", "1.9.4",
        "https://dl.reformcloud.systems/mcversions/hose/hose-1.9.4.jar",
        "https://internal.byteexception.eu/mcversions/hose/hose-1.9.4.jar"),
    HOSE_1_10_2("Hose 1.10.2", "1.10.2",
        "https://dl.reformcloud.systems/mcversions/hose/hose-1.10.2.jar",
        "https://internal.byteexception.eu/mcversions/hose/hose-1.10.2.jar"),
    /**
     * Akarin Versions
     */
    AKARIN_1_12_2("Akarin 1.12.2", "1.12.2",
        "https://github.com/Akarin-project/Akarin/releases/download/1.12.2-R0.4.2/akarin-1.12.2.jar",
        null);

    private static final Map<String, SpigotVersions> PROVIDERS = new ConcurrentHashMap<>();
    public static final Deque<String> AVAILABLE_VERSIONS = new ConcurrentLinkedDeque<>();

    static {
        for (SpigotVersions SpigotVersions : values()) {
            if (!PROVIDERS.containsKey(SpigotVersions.name())) {
                PROVIDERS.put(SpigotVersions.name(), SpigotVersions);
            }

            if (!AVAILABLE_VERSIONS.contains(SpigotVersions.version)) {
                AVAILABLE_VERSIONS.addAll(Arrays.asList(
                    "1.7.9",
                    "1.7.10",
                    "1.8",
                    "1.8.3",
                    "1.8.4",
                    "1.8.5",
                    "1.8.6",
                    "1.8.7",
                    "1.8.8",
                    "1.8.9",
                    "1.9",
                    "1.9.2",
                    "1.9.4",
                    "1.10",
                    "1.10.2",
                    "1.11",
                    "1.11.2",
                    "1.12",
                    "1.12.1",
                    "1.12.2",
                    "1.13",
                    "1.13.1",
                    "1.13.2",
                    "1.14",
                    "1.14.1",
                    "1.14.2",
                    "1.14.3",
                    "1.14.4"
                ));
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
        PROVIDERS.values().stream().filter(e -> e.version.equalsIgnoreCase(version))
            .forEach(e -> stringSpigotVersionsMap.put(e.name, e));
        return new TreeMap<>(stringSpigotVersionsMap);
    }

    public static String getAsFormattedJarFileName(SpigotVersions SpigotVersions) {
        return SpigotVersions.name.toLowerCase().replace(" ", "-") + ".jar";
    }

    private final String name;

    private final String version;

    private final String url;

    private final String fallbackUrl;

    SpigotVersions(final String name, final String version, final String url, final String fallbackUrl) {
        this.name = name;
        this.version = version;
        this.url = url;
        this.fallbackUrl = fallbackUrl;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getUrl() {
        return this.url;
    }

    public String getFallbackUrl() {
        return fallbackUrl;
    }
}
