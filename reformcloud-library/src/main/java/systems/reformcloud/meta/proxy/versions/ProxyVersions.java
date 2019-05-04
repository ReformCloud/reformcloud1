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
    WATERFALL("Waterfall", "https://papermc.io/ci/job/Waterfall/lastSuccessfulBuild/artifact/Waterfall-Proxy/bootstrap/target/Waterfall.jar"),
    HEXACORD("HexaCord", "https://archive.mcmirror.io/HexaCord/HexaCord-v139.jar"),
    BUNGEECORD("BungeeCord", "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar"),
    VELOCITY("Velocity", "https://ci.velocitypowered.com/job/velocity/lastSuccessfulBuild/artifact/proxy/build/libs/velocity-proxy-1.0.0-SNAPSHOT-all.jar"),
    TRAVERTINE("Travertine", "https://papermc.io/ci/job/Travertine/lastSuccessfulBuild/artifact/Travertine-Proxy/launcher/target/Travertine.jar");

    public static Map<String, ProxyVersions> PROVIDERS = new ConcurrentHashMap<>();

    static {
        for (ProxyVersions proxyProviders : values())
            if (!PROVIDERS.containsKey(proxyProviders.name()))
                PROVIDERS.put(proxyProviders.name(), proxyProviders);
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

    private String name, url;

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
