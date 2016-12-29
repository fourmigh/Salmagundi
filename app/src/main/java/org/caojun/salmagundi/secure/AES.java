package org.caojun.salmagundi.secure;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES
 * Created by CaoJun on 2016/12/29.
 */

public class AES {

    private static byte[] doAES(byte[] key, byte[] data, int opmode) {
        try {
            Key k = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(opmode, k);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     * @param key
     * @param data
     * @return
     */
    public static byte[] encrypt(byte[] key, byte[] data) {
        return doAES(key, data, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     * @param key
     * @param data
     * @return
     */
    public static byte[] decrypt(byte[] key, byte[] data) {
        return doAES(key, data, Cipher.DECRYPT_MODE);
    }
}
