/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@Getter
public class AccessChecker {
    private boolean accepted = false;

    /**
     * Checks if the given Channel is valid
     *
     * @param channelHandlerContexts
     * @param channelHandlerContext
     * @return
     * @see List#contains(Object)
     * @see AccessChecker#accepted
     * <p>
     * Sets {AccessChecker#accepted} to {@code true} if the given {@link List<ChannelHandlerContext>} contains the given {@link ChannelHandlerContext}
     * or {@code false} if the given {@link List<ChannelHandlerContext>} doesn't contains the given {@link ChannelHandlerContext}
     */
    public AccessChecker checkChannel(List<ChannelHandlerContext> channelHandlerContexts, ChannelHandlerContext channelHandlerContext) {
        this.accepted = channelHandlerContexts.contains(channelHandlerContext);
        return this;
    }

    /**
     * Checks if the given {@link String} is valid
     *
     * @param key
     * @param in
     * @return this
     * @see String#equals(Object)
     * @see AccessChecker#accepted
     * <p>
     * Sets {AccessChecker#accepted} to {@code true} if the given {@link String} {@param key} equals the given {@link String} {@param in}
     * or {@code false} if the given {@link String} {@param key} doesn't equals the given {@link String} {@param in}
     */
    public AccessChecker checkString(String key, String in) {
        this.accepted = key.equals(in);
        return this;
    }

    /**
     * Checks if the given {@link String} is valid
     *
     * @param key
     * @param in
     * @return this
     * @see String#equalsIgnoreCase(String)
     * @see AccessChecker#accepted
     * <p>
     * Sets {AccessChecker#accepted} to {@code true} if the given {@link String} {@param key} equalsIgnoreCase the given {@link String} {@param in}
     * or {@code false} if the given {@link String} {@param key} doesn't equals the given {@link String} {@param in}
     */
    public AccessChecker checkStringIgnored(String key, String in) {
        this.accepted = key.equalsIgnoreCase(in);
        return this;
    }

    /**
     * Checks if the given {@link Integer} is valid
     *
     * @param key
     * @param in
     * @return this
     * @see AccessChecker#accepted
     * <p>
     * Sets {AccessChecker#accepted} to {@code true} if the given {@link Integer} {@param key} equals the given {@link Integer} {@param in}
     * or {@code false} if the given {@link Integer) {@param key} doesn't equals the given {@link Integer} {@param in}
     */
    public AccessChecker checkInt(int key, int in) {
        this.accepted = (key == in);
        return this;
    }

    /**
     * Checks if the given {@link Boolean} is valid
     *
     * @param key
     * @param in
     * @return this
     * @see AccessChecker#accepted
     * <p>
     * Sets {AccessChecker#accepted} to {@code true} if the given {@link Boolean} {@param key} equals the given {@link Boolean} {@param in}
     * or {@code false} if the given {@link Boolean) {@param key} doesn't equals the given {@link Boolean} {@param in}
     */
    public AccessChecker checkBoolean(boolean key, boolean in) {
        this.accepted = (key == in);
        return this;
    }

    /**
     * Checks if the given {@link Double} is valid
     *
     * @param key
     * @param in
     * @return this
     * @see AccessChecker#accepted
     * <p>
     * Sets {AccessChecker#accepted} to {@code true} if the given {@link Double} {@param key} equals the given {@link Double} {@param in}
     * or {@code false} if the given {@link Double) {@param key} doesn't equals the given {@link Double} {@param in}
     */
    public AccessChecker checkDouble(double key, double in) {
        this.accepted = (key == in);
        return this;
    }

    /**
     * Checks if the given {@link Object} is valid
     *
     * @param in
     * @return this
     * @see AccessChecker#accepted
     * <p>
     * Sets {AccessChecker#accepted} to {@code true} if the given {@link Object} {@param in} is not null
     * or to {@code false} if the given {@link Object} {@param in} is null
     */
    public AccessChecker checkNonNull(Object in) {
        this.accepted = (in != null);
        return this;
    }

    /**
     * Checks if the given {@link Object} is valid
     *
     * @param key
     * @param in
     * @return this
     * @see AccessChecker#accepted
     * @see Object#equals(Object)
     * <p>
     * Sets {AccessChecker#accepted} to {@code true} if the given {@link Object} {@param key} equals the given {@link Object} {@param in}
     * or {@code false} if the given {@link Object) {@param key} doesn't equals the given {@link Object} {@param in}
     */
    public AccessChecker checkObject(Object key, Object in) {
        this.accepted = key.equals(in);
        return this;
    }

    /**
     * Throws an exception if the variable accepted is {@code false}
     *
     * @return this
     * @see AccessChecker#accepted
     */
    public AccessChecker throwExceptionIfNotAccepted() {
        if (!accepted)
            throw new IllegalArgumentException("Argument returned invalid code");

        return this;
    }

    /**
     * Returns the AccessChecker instance
     *
     * @return the AccessChecker instance
     */
    public AccessChecker getAccessChecker() {
        return this;
    }

    /**
     * Resets the {accepted} variable to {@code false}
     *
     * @see AccessChecker#accepted
     */
    public void reset() {
        this.accepted = false;
    }
}
