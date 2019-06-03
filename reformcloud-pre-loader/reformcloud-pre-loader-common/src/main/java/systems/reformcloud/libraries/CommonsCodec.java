/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libraries;

import systems.reformcloud.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 29.01.2019
 */

public final class CommonsCodec extends Dependency implements Serializable {

    public CommonsCodec() {
        super(null);
    }

    @Override
    public String getGroupID() {
        return "commons-codec";
    }

    @Override
    public String getName() {
        return "commons-codec";
    }

    @Override
    public String getVersion() {
        return "1.12";
    }
}
