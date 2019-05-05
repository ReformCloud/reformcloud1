/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.language;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.language.languages.defaults.English;
import systems.reformcloud.language.languages.defaults.German;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.utility.StringUtil;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 04.02.2019
 */

public final class LanguageManager implements Serializable {
    private static final long serialVersionUID = 6566354787998684748L;

    /**
     * The currently loaded language
     */
    public final Language loaded;

    /**
     * Loads the language
     *
     * @param lang The name of the language which should be loaded
     */
    public LanguageManager(final String lang) {
        if (lang == null || lang.equals(StringUtil.NULL)) {
            this.loaded = new English();
            return;
        }

        new File("reformcloud/language").mkdirs();

        if (!Files.exists(Paths.get("reformcloud/language/" + lang.toLowerCase() + ".json"))) {
            if (lang.equalsIgnoreCase("german"))
                new Configuration().addValue("lang", new German()).write(Paths.get("reformcloud/language/german.json"));
            else if (lang.equalsIgnoreCase("english"))
                new Configuration().addValue("lang", new English()).write(Paths.get("reformcloud/language/english.json"));
            else {
                this.loaded = new English();
                return;
            }
        }

        loaded = Configuration.parse(Paths.get("reformcloud/language/" + lang.toLowerCase() + ".json")).getValue("lang", new TypeToken<Language>() {
        }.getType());
    }

    public Language getLoaded() {
        return this.loaded;
    }
}
