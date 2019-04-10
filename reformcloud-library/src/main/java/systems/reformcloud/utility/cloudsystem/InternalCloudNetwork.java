/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.cloudsystem;

import lombok.Data;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.language.languages.defaults.English;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.web.InternalWebUser;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 20.10.2018
 */

@Data
public class InternalCloudNetwork implements Serializable {
    private static final long serialVersionUID = 4564917986901138765L;

    private int webPort = 4790;

    private InternalWebUser internalWebUser = new InternalWebUser(
            ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE) + "-internal",
            StringEncrypt.encrypt(ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE)
                    + StringUtil.EMPTY + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE))
    );

    private Configuration messages;
    private Language loaded = new English();

    private String prefix = StringUtil.NULL;

    public String getMessage(final String key) {
        return this.messages.getStringValue(key).replaceAll("%prefix%", this.prefix);
    }

    private final ServerProcessManager serverProcessManager = new ServerProcessManager();

    private Map<String, ServerGroup> serverGroups = ReformCloudLibraryService.concurrentHashMap();
    private Map<String, ProxyGroup> proxyGroups = ReformCloudLibraryService.concurrentHashMap();
    private Map<String, Client> clients = ReformCloudLibraryService.concurrentHashMap();
}
