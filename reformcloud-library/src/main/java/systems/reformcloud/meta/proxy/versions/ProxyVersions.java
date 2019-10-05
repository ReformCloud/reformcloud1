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
        "https://dl.reformcloud.systems/mcversions/proxies/waterfall.jar"),
    HEXACORD("HexaCord",
        "https://dl.reformcloud.systems/mcversions/proxies/hexacord-1.7.10-1.14.x.jar"),
    BUNGEECORD("BungeeCord",
        "https://dl.reformcloud.systems/mcversions/proxies/bungeecord.jar"),
    VELOCITY("Velocity",
        "https://dl.reformcloud.systems/mcversions/proxies/velocity.jar"),
    TRAVERTINE("Travertine",
        "https://dl.reformcloud.systems/mcversions/proxies/travertine.jar");

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

    ProxyVersions(final String name, final String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }
}
