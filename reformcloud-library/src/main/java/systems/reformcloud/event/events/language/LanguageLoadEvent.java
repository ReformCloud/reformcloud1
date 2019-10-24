/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.language;

import systems.reformcloud.language.utility.Language;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class LanguageLoadEvent extends LanguageEvent implements Serializable {

    public LanguageLoadEvent(Language language) {
        super(language);
    }
}
