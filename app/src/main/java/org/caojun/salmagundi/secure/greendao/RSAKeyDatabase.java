package org.caojun.salmagundi.secure.greendao;

import android.content.Context;

import java.util.List;

/**
 * RSA公私钥数据库
 * Created by CaoJun on 2017/1/13.
 */

public class RSAKeyDatabase {

    private DaoMaster daoMaster;
    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoSession daoSession;
    private RSAKeyDao rsaKeyDao;

    public RSAKeyDatabase(Context context) {
        devOpenHelper = new DaoMaster.DevOpenHelper(context, "rsakey-db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
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
