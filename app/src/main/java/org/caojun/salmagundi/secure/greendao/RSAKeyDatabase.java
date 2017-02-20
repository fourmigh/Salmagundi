package org.caojun.salmagundi.secure.greendao;

import android.content.Context;

import org.caojun.salmagundi.GreendaoDatabase;
import java.util.List;

/**
 * RSA公私钥数据库
 * Created by CaoJun on 2017/1/13.
 */

public class RSAKeyDatabase extends GreendaoDatabase {

    private RSAKeyDao rsaKeyDao;

    public RSAKeyDatabase(Context context) {
        super(context, "rsakey-db");
        rsaKeyDao = daoSession.getRSAKeyDao();
    }

    public long insert(byte[] publicKey, byte[] privateKey) {
        RSAKey rsaKey = new RSAKey(publicKey, privateKey);
        return rsaKeyDao.insert(rsaKey);
    }

    public List<RSAKey> query() {
        return rsaKeyDao.queryBuilder().build().list();
    }
}
