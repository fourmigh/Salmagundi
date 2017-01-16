package org.caojun.salmagundi.secure.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * RSA公私钥实体类
 * Created by CaoJun on 2017/1/13.
 */

@Entity
public class RSAKey {
    private byte[] privateKey;
    private byte[] publicKey;

    @Generated(hash = 1017545857)
    public RSAKey(byte[] privateKey, byte[] publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Generated(hash = 2117570767)
    public RSAKey() {
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
}
