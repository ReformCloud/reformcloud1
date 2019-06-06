/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.cloudsystem;

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
        StringEncrypt
            .encryptSHA512(ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE)
                + StringUtil.EMPTY + ReformCloudLibraryService.THREAD_LOCAL_RANDOM
                .nextLong(0, Long.MAX_VALUE))
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

    public InternalCloudNetwork() {
    }

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

    private int getWebPort() {
        return this.webPort;
    }

    public InternalWebUser getInternalWebUser() {
        return this.internalWebUser;
    }

    public Configuration getMessages() {
        return this.messages;
    }

    public Language getLoaded() {
        return this.loaded;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public ServerProcessManager getServerProcessManager() {
        return this.serverProcessManager;
    }

    public Map<String, ServerGroup> getServerGroups() {
        return this.serverGroups;
    }

    public Map<String, ProxyGroup> getProxyGroups() {
        return this.proxyGroups;
    }

    public Map<String, Client> getClients() {
        return this.clients;
    }

    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }

    public void setInternalWebUser(InternalWebUser internalWebUser) {
        this.internalWebUser = internalWebUser;
    }

    public void setMessages(Configuration messages) {
        this.messages = messages;
    }

    public void setLoaded(Language loaded) {
        this.loaded = loaded;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setServerGroups(Map<String, ServerGroup> serverGroups) {
        this.serverGroups = serverGroups;
    }

    public void setProxyGroups(Map<String, ProxyGroup> proxyGroups) {
        this.proxyGroups = proxyGroups;
    }

    public void setClients(Map<String, Client> clients) {
        this.clients = clients;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getWebPort();
        final Object $internalWebUser = this.getInternalWebUser();
        result = result * PRIME + ($internalWebUser == null ? 43 : $internalWebUser.hashCode());
        final Object $messages = this.getMessages();
        result = result * PRIME + ($messages == null ? 43 : $messages.hashCode());
        final Object $loaded = this.getLoaded();
        result = result * PRIME + ($loaded == null ? 43 : $loaded.hashCode());
        final Object $prefix = this.getPrefix();
        result = result * PRIME + ($prefix == null ? 43 : $prefix.hashCode());
        final Object $serverProcessManager = this.getServerProcessManager();
        result = result * PRIME + ($serverProcessManager == null ? 43
            : $serverProcessManager.hashCode());
        final Object $serverGroups = this.getServerGroups();
        result = result * PRIME + ($serverGroups == null ? 43 : $serverGroups.hashCode());
        final Object $proxyGroups = this.getProxyGroups();
        result = result * PRIME + ($proxyGroups == null ? 43 : $proxyGroups.hashCode());
        final Object $clients = this.getClients();
        result = result * PRIME + ($clients == null ? 43 : $clients.hashCode());
        return result;
    }

    public String toString() {
        return "InternalCloudNetwork(webPort=" + this.getWebPort() + ", internalWebUser=" + this
            .getInternalWebUser() + ", messages=" + this.getMessages() + ", loaded=" + this
            .getLoaded() + ", prefix=" + this.getPrefix() + ", serverProcessManager=" + this
            .getServerProcessManager() + ", serverGroups=" + this.getServerGroups()
            + ", proxyGroups=" + this.getProxyGroups() + ", clients=" + this.getClients() + ")";
    }
}
