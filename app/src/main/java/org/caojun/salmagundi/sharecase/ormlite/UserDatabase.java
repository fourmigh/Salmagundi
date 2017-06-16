package org.caojun.salmagundi.sharecase.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by CaoJun on 2017/6/14.
 */

public class UserDatabase extends OrmLiteSqliteOpenHelper {
    private static UserDatabase database;
    private static Dao<User, Integer> dao;

    public static UserDatabase getInstance(Context context) {
        if(database == null) {
            database = new UserDatabase(context);
            try {
                dao = database.getDao(User.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return database;
    }

    public UserDatabase(Context context) {
        super(context, "user-db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreate(db, connectionSource);
    }

    public int insert(User user) {
        try {
            return dao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int update(User user) {
        try {
            return dao.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<User> query() {
        try {
            return dao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> query(String columnName, Object value) {
        try {
            return dao.queryBuilder().where().eq(columnName, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
