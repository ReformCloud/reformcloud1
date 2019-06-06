/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.cryptic;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.security.MessageDigest;

/**
 * @author _Klaro | Pasqual K. / created on 08.02.2019
 */

public final class StringEncrypt implements Serializable {

    private static final long serialVersionUID = -960370385552735958L;

    /**
     * Encrypts the string in sha 1
     *
     * @param in The string which should be encrypted
     * @return The encrypted string
     */
    public static String encryptSHA1(final String in) {
        return DigestUtils.sha1Hex(in);
    }

    /**
     * Encrypts the string in sha 256
     *
     * @param in The string which should be encrypted
     * @return The encrypted string
     */
    public static String encryptSHA256(final String in) {
        return DigestUtils.sha256Hex(in);
    }

    /**
     * Encrypts the string in sha 384
     *
     * @param in The string which should be encrypted
     * @return The encrypted string
     */
    public static String encryptSHA384(final String in) {
        return DigestUtils.sha384Hex(in);
    }

    /**
     * Encrypts the string in sha 512
     *
     * @param in The string which should be encrypted
     * @return The encrypted string
     */
    public static String encryptSHA512(final String in) {
        return DigestUtils.sha512Hex(in);
    }

    /**
     * Encrypts the string by the given message digest
     *
     * @param messageDigest The message digest which should be used to encrypt the string
     * @param in The string which should be encrypted
     * @return The encrypted string
     */
    public static String encrypt(MessageDigest messageDigest, String in) {
        return new DigestUtils(messageDigest).digestAsHex(in);
    }
}
