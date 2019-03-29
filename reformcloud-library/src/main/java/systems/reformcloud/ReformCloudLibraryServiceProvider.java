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
    @Getter
    private static ReformCloudLibraryServiceProvider instance;
    private InternalCloudNetwork internalCloudNetwork;

    public Language loaded;

    private EventManager eventManager;

    private ChannelHandler channelHandler;

    private LoggerProvider loggerProvider;
    private String key, controllerIP;

    private final NettyHandler nettyHandler = new NettyHandler();
    private final TaskScheduler taskScheduler = new TaskScheduler();

    /**
     * Creates a new Instance of the {ReformCloudLibraryServiceProvider}
     *
     * @param loggerProvider
     * @param key
     * @param controllerIP
     * @param eventManager
     * @throws Throwable
     */
    public ReformCloudLibraryServiceProvider(LoggerProvider loggerProvider, String key, String controllerIP, EventManager eventManager, String lang) throws Throwable {
        if (instance == null)
            instance = this;
        else
            throw new InstanceAlreadyExistsException();

        this.key = key;
        this.controllerIP = controllerIP;
        this.loggerProvider = loggerProvider;
        this.eventManager = eventManager;
        this.loaded = new LanguageManager(lang).getLoaded();
    }
}
