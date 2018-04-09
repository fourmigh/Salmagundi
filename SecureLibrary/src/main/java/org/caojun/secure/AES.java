package org.caojun.secure;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES
 * Created by CaoJun on 2016/12/29.
 */

public class AES {

    private static byte[] getSecureKey(byte[] seed) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            sr.setSeed(seed);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            return skey.getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] doAES(byte[] key, byte[] data, int opmode) {
        try {
            SecretKeySpec sks = new SecretKeySpec(getSecureKey(key), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(opmode, sks, new IvParameterSpec(new byte[cipher.getBlockSize()]));
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
    private static byte[] encrypt(byte[] key, byte[] data) {
        return doAES(key, data, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     * @param key
     * @param data
     * @return
     */
    private static byte[] decrypt(byte[] key, byte[] data) {
        return doAES(key, data, Cipher.DECRYPT_MODE);
    }

    public static byte[] encrypt(String key, byte[] data) {
        try {
            return encrypt(key.getBytes(), data);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    public static byte[] decrypt(String key, byte[] data) {
        try {
            return decrypt(key.getBytes(), data);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }
}
