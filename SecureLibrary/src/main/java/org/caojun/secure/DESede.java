package org.caojun.secure;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * DESede
 * Created by CaoJun on 2016/12/29.
 */

public class DESede {

    private static SecretKey getSecureKey(byte[] key) {
        try {
            DESedeKeySpec desKey = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            return keyFactory.generateSecret(desKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] doDESede(byte[] key, byte[] data, int opmode) {
        try {
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(opmode, getSecureKey(key), new SecureRandom());
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
        return doDESede(key, data, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     * @param key
     * @param data
     * @return
     */
    private static byte[] decrypt(byte[] key, byte[] data) {
        return doDESede(key, data, Cipher.DECRYPT_MODE);
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
