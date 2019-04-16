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
public final class InternalCloudNetwork implements Serializable {
    private static final long serialVersionUID = 4564917986901138765L;

    /**
     * The web port, will be set in the controller while starting up
     */
    private int webPort = 4790;

    /**
     * The internal web user which will be used for some actions in the cloud system
     */
    private InternalWebUser internalWebUser = new InternalWebUser(
            ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE) + "-internal",
            StringEncrypt.encryptSHA512(ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE)
                    + StringUtil.EMPTY + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE))
    );

    /**
     * All messages of the cloud system
     */
    private Configuration messages;

    /**
     * The loaded language of the cloud system, will be set in the controller while starting up
     */
    private Language loaded = new English();

    /**
     * The prefix of the cloud system, will be set in the controller while starting up
     */
    private String prefix = StringUtil.NULL;

    /**
     * Returns a message out of the message config
     *
     * @param key The message key
     * @return The message saved in the config file
     */
    public String getMessage(final String key) {
        return this.messages.getStringValue(key).replaceAll("%prefix%", this.prefix);
    }

    /**
     * The server process manger where all currently running server processes are managed in
     */
    private final ServerProcessManager serverProcessManager = new ServerProcessManager();

    /**
     * All registered server groups of the cloud system
     */
    private Map<String, ServerGroup> serverGroups = ReformCloudLibraryService.concurrentHashMap();

    /**
     * All registered proxy groups of the cloud system
     */
    private Map<String, ProxyGroup> proxyGroups = ReformCloudLibraryService.concurrentHashMap();

    /**
     * All registered clients of the cloud system
     */
    private Map<String, Client> clients = ReformCloudLibraryService.concurrentHashMap();
}
