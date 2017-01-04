package org.caojun.salmagundi.secure;

import org.caojun.salmagundi.string.ConvertUtils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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

    private static byte[] getRawKey(byte[] seed) {
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
            byte[] rawKey = getRawKey(key);
            SecretKeySpec sks = new SecretKeySpec(rawKey, "AES");
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

//    public static String encrypt(String key, String data) {
//        try {
//            byte[] result = encrypt(key.getBytes(), data.getBytes());
//            return ConvertUtils.string2hex(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return data;
//        }
//    }
//
//    public static String decrypt(String key, String data) {
//        try {
//            byte[] enc = ConvertUtils.hex2bytes(data);
//            byte[] result = decrypt(key.getBytes(), enc);
//            return new String(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return data;
//        }
//    }
}
