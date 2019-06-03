/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobs;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class SelectorMob implements Serializable {

    private UUID uniqueID;

    private String entityClassName;

    private String name;

    private String displayName;

    private SelectorMobPosition selectorMobPosition;

    @java.beans.ConstructorProperties({"uniqueID", "entityClassName", "name", "displayName",
        "selectorMobPosition"})
    public SelectorMob(UUID uniqueID, String entityClassName, String name, String displayName,
        SelectorMobPosition selectorMobPosition) {
        this.uniqueID = uniqueID;
        this.entityClassName = entityClassName;
        this.name = name;
        this.displayName = displayName;
        this.selectorMobPosition = selectorMobPosition;
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public String getEntityClassName() {
        return this.entityClassName;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public SelectorMobPosition getSelectorMobPosition() {
        return this.selectorMobPosition;
    }
}
