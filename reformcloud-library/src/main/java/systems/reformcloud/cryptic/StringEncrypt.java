/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.cryptic;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.02.2019
 */

public final class StringEncrypt implements Serializable {
    private static final long serialVersionUID = -960370385552735958L;

    public static String encrypt(final String in) {
        return DigestUtils.sha512Hex(in);
    }

    public static String encryptSHA256(final String in) {
        return DigestUtils.sha256Hex(in);
    }
}
