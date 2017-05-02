package org.caojun.salmagundi.passwordstore.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * 密码仓库数据库
 * Created by CaoJun on 2017/2/15.
 */

public class PasswordDatabase extends OrmLiteSqliteOpenHelper {

    private static PasswordDatabase passwordDatabase;
    private static Dao<Password, Integer> passwordDao;

    public static PasswordDatabase getInstance(Context context) {
        if(passwordDatabase == null) {
            passwordDatabase = new PasswordDatabase(context);
            try {
                passwordDao = passwordDatabase.getDao(Password.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return passwordDatabase;
    }

    public PasswordDatabase(Context context) {
        super(context, "password-db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Password.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Password.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreate(db, connectionSource);
    }

    public long insert(String company, String url, byte type, byte length, String account, String password) {
        Password psd = new Password(null, company, url, type, length, account, PasswordUtils.getEncodePassword(company, url, type, length, account, password), (byte)password.length());
        try {
            return passwordDao.create(psd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void update(Password password) {
        try {
            passwordDao.update(password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, String company, String url, byte type, byte length, String account, String password) {
        update(new Password(id, company, url, type, length, account, PasswordUtils.getEncodePassword(company, url, type, length, account, password), (byte)password.length()));
    }

    public void delete(int id) {
        try {
            passwordDao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Password> query() {
        try {
            return passwordDao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
