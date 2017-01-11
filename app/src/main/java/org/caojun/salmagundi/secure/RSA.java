package org.caojun.salmagundi.secure;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
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
            if(isPublicKey) {
                PublicKey publicKey = getPublicKey(key);
                cipher.init(opmode, publicKey);
            }
            else {
                PrivateKey privateKey = getPrivateKey(key);
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

    private static PrivateKey getPrivateKey(byte[] key) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static PublicKey getPublicKey(byte[] key) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥签名
     * @param key
     * @param data
     * @return
     */
    public static byte[] sign(byte[] key, byte[] data) {
        try {
            PrivateKey privateKey = getPrivateKey(key);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥验签
     * @param key
     * @param data
     * @return
     */
    public static boolean verify(byte[] key, byte[] data, byte[] sign) {
        try {
            PublicKey publicKey = getPublicKey(key);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
