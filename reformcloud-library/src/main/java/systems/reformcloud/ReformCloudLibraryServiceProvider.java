/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.event.EventManager;
import systems.reformcloud.language.LanguageManager;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import lombok.Getter;
import lombok.Setter;

import javax.management.InstanceAlreadyExistsException;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@Getter
@Setter
public final class ReformCloudLibraryServiceProvider {
    @Getter
    private static ReformCloudLibraryServiceProvider instance;
    private InternalCloudNetwork internalCloudNetwork;

    public Language loaded;

    private EventManager eventManager;

    private LoggerProvider loggerProvider;
    private String key, controllerIP;

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
