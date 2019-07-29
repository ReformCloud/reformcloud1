/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.event.abstracts.EventManager;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.language.LanguageManager;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.logging.AbstractLoggerProvider;
import systems.reformcloud.logging.ColouredConsoleProvider;
import systems.reformcloud.meta.cluster.NetworkGlobalCluster;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.abstracts.AbstractChannelHandler;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.threading.AbstractTaskScheduler;
import systems.reformcloud.utility.threading.TaskScheduler;

import java.util.Objects;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public final class ReformCloudLibraryServiceProvider {

    /**
     * The internal instance of this class
     */
    private static ReformCloudLibraryServiceProvider instance;

    /**
     * The internal cloud network
     */
    private InternalCloudNetwork internalCloudNetwork;

    /**
     * The loaded language which should be used
     */
    private Language loaded;

    /**
     * The EventManager of the cloud system
     */
    private EventManager eventManager;

    /**
     * The channel handler of the cloud system
     */
    private AbstractChannelHandler channelHandler;

    /**
     * The logger provider of the cloud system
     */
    private AbstractLoggerProvider colouredConsoleProvider;

    /**
     * The controller key
     */
    private String key;

    /**
     * The ip address of the controller
     */
    private String controllerIP;

    /**
     * The netty handler of the cloud
     */
    private final NettyHandler nettyHandler = new NettyHandler();

    /**
     * The TaskScheduler of the cloud
     */
    private AbstractTaskScheduler taskScheduler;

    /**
     * Creates a new Instance of the {ReformCloudLibraryServiceProvider}
     *
     * @param colouredConsoleProvider The internal ColouredConsoleProvider created by the instances
     * @param key The controller key
     * @param controllerIP The controller ip address
     * @param eventManager The event manager of the cloud
     */
    public ReformCloudLibraryServiceProvider(ColouredConsoleProvider colouredConsoleProvider, String key,
                                             String controllerIP, EventManager eventManager, String lang) {
        if (instance == null) {
            instance = this;
        } else {
            throw new InstanceAlreadyExistsException();
        }

        if (lang != null) {
            this.taskScheduler = new TaskScheduler();
        }

        this.key = key;
        this.controllerIP = controllerIP;
        this.colouredConsoleProvider = colouredConsoleProvider;
        this.eventManager = eventManager;
        this.loaded = new LanguageManager(lang).getLoaded();
    }

    public static ReformCloudLibraryServiceProvider getInstance() {
        return ReformCloudLibraryServiceProvider.instance;
    }

    public InternalCloudNetwork getInternalCloudNetwork() {
        return this.internalCloudNetwork;
    }

    public Language getLoaded() {
        return this.loaded;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    private AbstractChannelHandler getChannelHandler() {
        return this.channelHandler;
    }

    public AbstractLoggerProvider getColouredConsoleProvider() {
        return this.colouredConsoleProvider;
    }

    public String getKey() {
        return this.key;
    }

    public String getControllerIP() {
        return this.controllerIP;
    }

    public NettyHandler getNettyHandler() {
        return this.nettyHandler;
    }

    public AbstractTaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }

    public void setInternalCloudNetwork(InternalCloudNetwork internalCloudNetwork) {
        this.internalCloudNetwork = internalCloudNetwork;
    }

    public void setLoaded(Language loaded) {
        this.loaded = loaded;
    }

    public void setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public NetworkGlobalCluster shiftClusterNetworkInformation() {
        return this.channelHandler.shiftClusterNetworkInformation();
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReformCloudLibraryServiceProvider)) {
            return false;
        }
        final ReformCloudLibraryServiceProvider other = (ReformCloudLibraryServiceProvider) o;
        final Object this$internalCloudNetwork = this.getInternalCloudNetwork();
        final Object other$internalCloudNetwork = other.getInternalCloudNetwork();
        if (!Objects.equals(this$internalCloudNetwork, other$internalCloudNetwork)) {
            return false;
        }
        final Object this$loaded = this.getLoaded();
        final Object other$loaded = other.getLoaded();
        if (!Objects.equals(this$loaded, other$loaded)) {
            return false;
        }
        final Object this$eventManager = this.getEventManager();
        final Object other$eventManager = other.getEventManager();
        if (!Objects.equals(this$eventManager, other$eventManager)) {
            return false;
        }
        final Object this$channelHandler = this.getChannelHandler();
        final Object other$channelHandler = other.getChannelHandler();
        if (!Objects.equals(this$channelHandler, other$channelHandler)) {
            return false;
        }
        final Object this$loggerProvider = this.getColouredConsoleProvider();
        final Object other$loggerProvider = other.getColouredConsoleProvider();
        if (!Objects.equals(this$loggerProvider, other$loggerProvider)) {
            return false;
        }
        final Object this$key = this.getKey();
        final Object other$key = other.getKey();
        if (!Objects.equals(this$key, other$key)) {
            return false;
        }
        final Object this$controllerIP = this.getControllerIP();
        final Object other$controllerIP = other.getControllerIP();
        if (!Objects.equals(this$controllerIP, other$controllerIP)) {
            return false;
        }
        final Object this$nettyHandler = this.getNettyHandler();
        final Object other$nettyHandler = other.getNettyHandler();
        if (!Objects.equals(this$nettyHandler, other$nettyHandler)) {
            return false;
        }
        final Object this$taskScheduler = this.getTaskScheduler();
        final Object other$taskScheduler = other.getTaskScheduler();
        return Objects.equals(this$taskScheduler, other$taskScheduler);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $internalCloudNetwork = this.getInternalCloudNetwork();
        result = result * PRIME + ($internalCloudNetwork == null ? 43
            : $internalCloudNetwork.hashCode());
        final Object $loaded = this.getLoaded();
        result = result * PRIME + ($loaded == null ? 43 : $loaded.hashCode());
        final Object $eventManager = this.getEventManager();
        result = result * PRIME + ($eventManager == null ? 43 : $eventManager.hashCode());
        final Object $channelHandler = this.getChannelHandler();
        result = result * PRIME + ($channelHandler == null ? 43 : $channelHandler.hashCode());
        final Object $loggerProvider = this.getColouredConsoleProvider();
        result = result * PRIME + ($loggerProvider == null ? 43 : $loggerProvider.hashCode());
        final Object $key = this.getKey();
        result = result * PRIME + ($key == null ? 43 : $key.hashCode());
        final Object $controllerIP = this.getControllerIP();
        result = result * PRIME + ($controllerIP == null ? 43 : $controllerIP.hashCode());
        final Object $nettyHandler = this.getNettyHandler();
        result = result * PRIME + $nettyHandler.hashCode();
        final Object $taskScheduler = this.getTaskScheduler();
        result = result * PRIME + ($taskScheduler == null ? 43 : $taskScheduler.hashCode());
        return result;
    }

    public String toString() {
        return "ReformCloudLibraryServiceProvider(internalCloudNetwork=" + this
            .getInternalCloudNetwork() + ", loaded=" + this.getLoaded() + ", eventManager=" + this
            .getEventManager() + ", channelHandler=" + this.getChannelHandler()
            + ", colouredConsoleProvider=" + this.getColouredConsoleProvider() + ", key=" + this.getKey()
            + ", controllerIP=" + this.getControllerIP() + ", nettyHandler=" + this
            .getNettyHandler() + ", taskScheduler=" + this.getTaskScheduler() + ")";
    }
}
