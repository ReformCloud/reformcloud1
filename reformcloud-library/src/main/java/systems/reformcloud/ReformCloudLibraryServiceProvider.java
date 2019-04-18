/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import lombok.Data;
import lombok.Getter;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.language.LanguageManager;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.threading.TaskScheduler;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@Data
public final class ReformCloudLibraryServiceProvider {
    /**
     * The internal instance of this class
     */
    @Getter
    private static ReformCloudLibraryServiceProvider instance;

    /**
     * The internal cloud network
     */
    private InternalCloudNetwork internalCloudNetwork;

    /**
     * The loaded language which should be used
     */
    public Language loaded;

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
     * @param loggerProvider        The internal LoggerProvider created by the instances
     * @param key                   The controller key
     * @param controllerIP          The controller ip address
     * @param eventManager          The event manager of the cloud
     * @throws Throwable            Will be thrown if any exception occurs
     */
    public ReformCloudLibraryServiceProvider(LoggerProvider loggerProvider, String key, String controllerIP, EventManager eventManager, String lang) throws Throwable {
        if (instance == null)
            instance = this;
        else
            throw new InstanceAlreadyExistsException();

        if (lang != null)
            this.taskScheduler = new TaskScheduler();

        this.key = key;
        this.controllerIP = controllerIP;
        this.loggerProvider = loggerProvider;
        this.eventManager = eventManager;
        this.loaded = new LanguageManager(lang).getLoaded();
    }
}
