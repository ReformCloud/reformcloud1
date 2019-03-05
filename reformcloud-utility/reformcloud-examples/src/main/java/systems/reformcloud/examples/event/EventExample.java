/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.event;

import systems.reformcloud.event.Listener;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.*;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.channel.ChannelReader;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class EventExample extends Listener {

    /**
     * Creates a new Event Class
     * Name is not used, use any string
     */
    public EventExample(String name, EventTargetType eventTargetType) {
        super(name, eventTargetType);
    }

    /**
     * Returns the eventTargetType, use the given above or use your own
     * EventTargetType#NOT_DEFINED will throw an illegal access exception
     * You don't have to implement this method
     * @see Listener#getEventTargetType ()
     * or let the super call
     */
    @Override
    public EventTargetType getEventTargetType() {
        return super.getEventTargetType();
    }

    /**
     * Returns the Listener name
     * Use any String you want
     */
    @Override
    public String getName() {
        return super.getName();
    }

    /*
     * There are a lot of self-explaining events, check it out yourself
     *
     * Creating own events is not possible that easy, sorry
     */

    /**
     * Event get called when Instance of Controller or Client is ready
     * !! Not usable in Spigot/Bukkit API !!
     */
    @Override
    public void handle(LoadSuccessEvent event) {
        super.handle(event);
    }

    /**
     * Get called when a packet comes
     * !! Get called all the time not only when the packet is handled by a {@link NetworkInboundHandler} ;
     * But only when it is a packet random objects won't work !!
     */
    @Override
    public void handle(IncomingPacketEvent event) {
        super.handle(event);
    }

    /**
     * Get always called when the {@link ChannelHandler} sends a packet
     */
    @Override
    public void handle(OutGoingPacketEvent event) {
        super.handle(event);
    }

    /**
     * Get called when a process was registered in the cloud and the register packet comes
     * !! NOTE: If you cancel this event, you can't join this serve because it will not be registered in the BungeeCord !!
     */
    @Override
    public void handle(ProcessRegisterEvent event) {
        super.handle(event);
    }

    /**
     * Get called when a process was unregistered and the packet comes
     * !! NOTE: Not cancelable ; Method is @deprecated, too !!
     */
    @Override
    public void handle(ProcessUnregistersEvent event) {
        super.handle(event);
    }

    /**
     * Get called when the {@link ChannelReader} could find a valid {@link NetworkInboundHandler}
     * !! NOTE: Not cancelable ; Method is @deprecated, too !!
     */
    @Override
    public void handle(PacketHandleSuccessEvent event) {
        super.handle(event);
    }
}
