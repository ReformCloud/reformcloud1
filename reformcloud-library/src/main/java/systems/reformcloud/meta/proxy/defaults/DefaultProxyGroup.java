/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.proxy.defaults;

import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.auto.start.AutoStart;
import systems.reformcloud.meta.auto.stop.AutoStop;
import systems.reformcloud.meta.enums.ProxyModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public final class DefaultProxyGroup extends ProxyGroup implements Serializable {
    private static final long serialVersionUID = -1867335836689571544L;

    public DefaultProxyGroup(int memory, final String client, ProxyVersions proxyVersions) {
        super(
                "Proxy",
                Collections.singletonList(client),
                new ArrayList<>(),
                Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
                new ArrayList<>(),
                ProxyModeType.DYNAMIC,
                new AutoStart(true, 510, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.SECONDS.toSeconds(5)),
                true,
                true,
                false,
                25565,
                1,
                -1,
                512,
                memory,
                proxyVersions
        );
    }

    public DefaultProxyGroup(final String name, final String client, ProxyVersions proxyVersions) {
        super(
                name,
                Collections.singletonList(client),
                new ArrayList<>(),
                Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
                new ArrayList<>(),
                ProxyModeType.DYNAMIC,
                new AutoStart(true, 510, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.SECONDS.toSeconds(5)),
                true,
                true,
                false,
                25565,
                1,
                -1,
                512,
                128,
                proxyVersions
        );
    }

    public DefaultProxyGroup(final String name, final String client, ProxyVersions proxyVersions, ProxyModeType proxyModeType) {
        super(
                name,
                Collections.singletonList(client),
                new ArrayList<>(),
                Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
                new ArrayList<>(),
                proxyModeType,
                new AutoStart(true, 510, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.SECONDS.toSeconds(5)),
                true,
                true,
                false,
                25565,
                1,
                -1,
                512,
                128,
                proxyVersions
        );
    }

    public String toString() {
        return "DefaultProxyGroup()";
    }
}
