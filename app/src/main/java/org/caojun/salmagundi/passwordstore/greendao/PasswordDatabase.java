package org.caojun.salmagundi.passwordstore.greendao;

import android.content.Context;
import org.caojun.salmagundi.GreendaoDatabase;

import java.util.List;

/**
 * 密码仓库数据库
 * Created by CaoJun on 2017/2/15.
 */

public class PasswordDatabase extends GreendaoDatabase {
    private PasswordDao passwordDao;
    private static PasswordDatabase passwordDatabase;

    public static PasswordDatabase getInstance(Context context) {
        if(passwordDatabase == null) {
            passwordDatabase = new PasswordDatabase(context);
        }
        return passwordDatabase;
    }

    public PasswordDatabase(Context context) {
        super(context, "password-db");
        passwordDao = daoSession.getPasswordDao();
    }

    public long insert(String company, String url, byte type, byte length, String account, String password) {
        Password psd = new Password(null, company, url, type, length, account, Password.getEncodePassword(company, url, type, length, account, password), (byte)password.length());
        return passwordDao.insert(psd);
    }

    public void update(Password password) {
        passwordDao.update(password);
    }

    public void update(Long id, String company, String url, byte type, byte length, String account, String password) {
        update(new Password(id, company, url, type, length, account, Password.getEncodePassword(company, url, type, length, account, password), (byte)password.length()));
    }

    public void delete(Long id) {
        passwordDao.deleteByKey(id);
    }

    public List<Password> query() {
        return passwordDao.queryBuilder().build().list();
    }
}
