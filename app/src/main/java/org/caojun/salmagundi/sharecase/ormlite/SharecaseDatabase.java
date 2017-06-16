package org.caojun.salmagundi.sharecase.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by CaoJun on 2017/6/14.
 */

public class SharecaseDatabase extends OrmLiteSqliteOpenHelper {
    private static SharecaseDatabase database;
    private static Dao<Sharecase, Integer> dao;

    public static SharecaseDatabase getInstance(Context context) {
        if(database == null) {
            database = new SharecaseDatabase(context);
            try {
                dao = database.getDao(Sharecase.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return database;
    }

    public SharecaseDatabase(Context context) {
        super(context, "sharecase-db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Sharecase.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Sharecase.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreate(db, connectionSource);
    }

    public int insert(Sharecase sharecase) {
        try {
            return dao.create(sharecase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int update(Sharecase sharecase) {
        try {
            return dao.update(sharecase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Sharecase> query() {
        try {
            return dao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Sharecase> query(String columnName, Object value) {
        try {
            return dao.queryBuilder().where().eq(columnName, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
