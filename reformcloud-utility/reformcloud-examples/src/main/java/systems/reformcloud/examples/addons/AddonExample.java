/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.addons;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.event.Listener;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.IncomingPacketEvent;
import systems.reformcloud.event.events.LoadSuccessEvent;
import systems.reformcloud.event.events.ProcessUnregistersEvent;
import systems.reformcloud.utility.ControllerAddonImpl;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class AddonExample extends ControllerAddonImpl {
    @Override
    public void onAddonClazzPrepare() {
        //Called when Addon main class is prepared and configuration is ready
        //Loads the Addon, Cloud is still loading
        this.getInternalReformCloudSystem().getEventManager().registerListener(new Listener("A cool Event", EventTargetType.LOAD_SUCCESS) {
            @Override
            public void handle(LoadSuccessEvent event) {
                ReformCloudController.getInstance().getLoggerProvider().info("Already loaded this cloud?!");
            }
        });
    }

    @Override
    public void onAddonLoading() {
        //Called when Cloud is finished with loading all other stuff
        //Enables the Addon, Cloud is now ready to use
        this.getInternalReformCloudSystem().getEventManager().registerListener(new Listener("Another cool event", EventTargetType.INCOMING_PACKET) {
            @Override
            public void handle(IncomingPacketEvent event) {
                ReformCloudController.getInstance().getLoggerProvider().info("Is there a packet incoming (" + event.getPacket().getPacketSender().name() + ")");
            }
        });
    }

    @Override
    public void onAddonReadyToClose() {
        //Called when Cloud stops or get reloaded
        //Starts again by onAddonClazzPrepare() if cloud gets reloaded
        this.getInternalReformCloudSystem().getEventManager().registerListener(new Listener("Close Event", EventTargetType.PROCESS_UNREGISTERED) {
            @Override
            public void handle(ProcessUnregistersEvent event) {
                ReformCloudController.getInstance().getLoggerProvider().info("HUH? Where is my process? " + event.getName());
            }
        });
    }
}
