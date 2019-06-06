/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.event.EventManager;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.language.LanguageManager;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
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
    private ChannelHandler channelHandler;

    /**
     * The logger provider of the cloud system
     */
    private LoggerProvider loggerProvider;

    /**
     * Some internal information about the controller
     */
    private String key, controllerIP;

    /**
     * The netty handler of the cloud
     */
    private final NettyHandler nettyHandler = new NettyHandler();

    /**
     * The TaskScheduler of the cloud
     */
    private TaskScheduler taskScheduler;

    /**
     * Creates a new Instance of the {ReformCloudLibraryServiceProvider}
     *
     * @param loggerProvider The internal LoggerProvider created by the instances
     * @param key The controller key
     * @param controllerIP The controller ip address
     * @param eventManager The event manager of the cloud
     * @throws Throwable Will be thrown if any exception occurs
     */
    public ReformCloudLibraryServiceProvider(LoggerProvider loggerProvider, String key,
        String controllerIP, EventManager eventManager, String lang) throws Throwable {
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
        this.loggerProvider = loggerProvider;
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

    private ChannelHandler getChannelHandler() {
        return this.channelHandler;
    }

    public LoggerProvider getLoggerProvider() {
        return this.loggerProvider;
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

    public TaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }

    public void setInternalCloudNetwork(InternalCloudNetwork internalCloudNetwork) {
        this.internalCloudNetwork = internalCloudNetwork;
    }

    public void setLoaded(Language loaded) {
        this.loaded = loaded;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    public void setLoggerProvider(LoggerProvider loggerProvider) {
        this.loggerProvider = loggerProvider;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setControllerIP(String controllerIP) {
        this.controllerIP = controllerIP;
    }

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
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
        final Object this$loggerProvider = this.getLoggerProvider();
        final Object other$loggerProvider = other.getLoggerProvider();
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
        if (!Objects.equals(this$taskScheduler, other$taskScheduler)) {
            return false;
        }
        return true;
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
        final Object $loggerProvider = this.getLoggerProvider();
        result = result * PRIME + ($loggerProvider == null ? 43 : $loggerProvider.hashCode());
        final Object $key = this.getKey();
        result = result * PRIME + ($key == null ? 43 : $key.hashCode());
        final Object $controllerIP = this.getControllerIP();
        result = result * PRIME + ($controllerIP == null ? 43 : $controllerIP.hashCode());
        final Object $nettyHandler = this.getNettyHandler();
        result = result * PRIME + ($nettyHandler == null ? 43 : $nettyHandler.hashCode());
        final Object $taskScheduler = this.getTaskScheduler();
        result = result * PRIME + ($taskScheduler == null ? 43 : $taskScheduler.hashCode());
        return result;
    }

    public String toString() {
        return "ReformCloudLibraryServiceProvider(internalCloudNetwork=" + this
            .getInternalCloudNetwork() + ", loaded=" + this.getLoaded() + ", eventManager=" + this
            .getEventManager() + ", channelHandler=" + this.getChannelHandler()
            + ", loggerProvider=" + this.getLoggerProvider() + ", key=" + this.getKey()
            + ", controllerIP=" + this.getControllerIP() + ", nettyHandler=" + this
            .getNettyHandler() + ", taskScheduler=" + this.getTaskScheduler() + ")";
    }
}
