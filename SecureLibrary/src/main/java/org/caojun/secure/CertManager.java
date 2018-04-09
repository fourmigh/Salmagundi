package org.caojun.secure;

import android.content.Context;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

public class CertManager {

    public static final String ALG = "SHA1WithRSA";
    public static final String KEYALIAS = "TerminalCert";
    public static final String KSFILE = "cert.ks";

    private KeyStore ks;
    private KeyPair tempKey;

    private static final CertManager cm = new CertManager();

    private CertManager() {
        init();
    }

    public static CertManager getInstance() {
        return cm;
    }

    private static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public Boolean hasCertFile(Context context, String ksPwd) {
        try {
            String path = context.getFilesDir().getAbsolutePath() + "/" + KSFILE;
            FileInputStream stream = new FileInputStream(path);
            boolean flag = load(stream, ksPwd);
            stream.close();
            return flag;
        } catch (Exception e) {
        }
        return false;
    }

    public Boolean load(InputStream is, String ksPwd) {
        try {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(is, ksPwd.toCharArray());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public String createCertificateRequest(String subjectOU, String subjectCN) {
        try {
            tempKey = generateKeyPair();
            String dn = String.format(subjectLocal, subjectO, subjectOU, subjectCN);
            X500Principal subjectName = new X500Principal(dn);

            PKCS10CertificationRequest request = new PKCS10CertificationRequest(
                    ALG,
                    subjectName,
                    tempKey.getPublic(),
                    new DERSet(),
                    tempKey.getPrivate());

            return new String(Base64.encode(request.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createPKCS12(byte[] caResponse, OutputStream os, String pwd) {
        try {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(null, pwd.toCharArray());
            Certificate[] chain = new Certificate[1];
            ByteArrayInputStream bais = new ByteArrayInputStream(caResponse);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            chain[0] = cf.generateCertificate(bais);
            ks.setKeyEntry(CertManager.KEYALIAS, tempKey.getPrivate(),
                    pwd.toCharArray(), chain);
            ks.store(os, pwd.toCharArray());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public byte[] signData(Context context, byte[] data, String pwd) {
        try {
            String path = context.getFilesDir().getAbsolutePath() + "/" + KSFILE;
            FileInputStream fileInputStream = new FileInputStream(path);
            ks = KeyStore.getInstance("PKCS12");
            ks.load(fileInputStream, pwd.toCharArray());
            Signature sig = Signature.getInstance(ALG);
            PrivateKey key = (PrivateKey) ks.getKey(KEYALIAS, pwd.toCharArray());
            sig.initSign(key);
            sig.update(data);
            return sig.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCert() {
        try {
            Certificate cert = ks.getCertificate(KEYALIAS);
            return new String(Base64.encode(cert.getEncoded()));
        } catch (Exception e) {
        }
        return null;
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024, new SecureRandom());
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

}
