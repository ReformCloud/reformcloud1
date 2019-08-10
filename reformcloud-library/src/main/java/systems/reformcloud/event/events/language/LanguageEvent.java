/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.language;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.language.utility.Language;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class LanguageEvent extends Event implements Serializable {

    public LanguageEvent(Language language) {
        this.language = language;
    }

    private final Language language;

    public Language getLanguage() {
        return language;
    }
}
