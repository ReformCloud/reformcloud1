/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.proxy.versions;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author _Klaro | Pasqual K. / created on 28.01.2019
 */

public enum ProxyVersions implements Serializable {
    WATERFALL("Waterfall",
        "https://papermc.io/ci/job/Waterfall/lastSuccessfulBuild/artifact/Waterfall-Proxy/bootstrap/target/Waterfall.jar"),
    HEXACORD("HexaCord",
        "https://github.com/HexagonMC/BungeeCord/releases/download/v246/BungeeCord.jar"),
    BUNGEECORD("BungeeCord",
        "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.ja"),
    VELOCITY("Velocity",
        "https://ci.velocitypowered.com/job/velocity/lastSuccessfulBuild/artifact/proxy/build/libs/velocity-proxy-1.0.4-SNAPSHOT-all.jar"),
    TRAVERTINE("Travertine",
        "https://papermc.io/ci/job/Travertine/lastSuccessfulBuild/artifact/Travertine-Proxy/bootstrap/target/Travertine.jar");

    private static Map<String, ProxyVersions> PROVIDERS = new ConcurrentHashMap<>();

    static {
        for (ProxyVersions proxyProviders : values()) {
            if (!PROVIDERS.containsKey(proxyProviders.name())) {
                PROVIDERS.put(proxyProviders.name(), proxyProviders);
            }
        }
    }

    public static ProxyVersions getByName(final String name) {
        return PROVIDERS.getOrDefault(name.toUpperCase(), null);
    }

    public static TreeMap<String, ProxyVersions> sorted() {
        return new TreeMap<>(PROVIDERS);
    }

    public static String getAsJarFileName(ProxyVersions proxyProviders) {
        return proxyProviders.name().toLowerCase() + ".jar";
    }

    private final String name;

    private final String url;

    private final String fallbackUrl;

    ProxyVersions(final String name, final String url, final String fallbackUrl) {
        this.name = name;
        this.url = url;
        this.fallbackUrl = fallbackUrl;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getFallbackUrl() {
        return fallbackUrl;
    }
}
