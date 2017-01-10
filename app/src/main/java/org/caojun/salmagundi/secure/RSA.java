package org.caojun.salmagundi.secure;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * RSA
 * Created by CaoJun on 2016/12/29.
 */

public class RSA {

    public static byte[][] genKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            byte[][] key = new byte[2][];
            key[0] = rsaPublicKey.getEncoded();
            key[1] = rsaPrivateKey.getEncoded();
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] doRSA(byte[] key, boolean isPublicKey, byte[] data, int opmode) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            if(isPublicKey) {
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
                PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
                cipher.init(opmode, publicKey);
            }
            else {
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
                PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                cipher.init(opmode, privateKey);
            }
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
    public static byte[] encrypt(byte[] key, boolean isPublicKey, byte[] data) {
        return doRSA(key, isPublicKey, data, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     * @param key
     * @param data
     * @return
     */
    public static byte[] decrypt(byte[] key, boolean isPublicKey, byte[] data) {
        return doRSA(key, isPublicKey, data, Cipher.DECRYPT_MODE);
    }
}
