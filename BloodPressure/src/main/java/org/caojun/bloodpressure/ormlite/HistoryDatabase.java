package org.caojun.bloodpressure.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by CaoJun on 2017/7/10.
 */

public class HistoryDatabase extends OrmLiteSqliteOpenHelper {
    private static HistoryDatabase historyDatabase;
    private static Dao<History, Integer> historyDao;

    public static HistoryDatabase getInstance(Context context) {
        if(historyDatabase == null) {
            historyDatabase = new HistoryDatabase(context);
            try {
                historyDao = historyDatabase.getDao(History.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return historyDatabase;
    }

    public HistoryDatabase(Context context) {
        super(context, "history-db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, History.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, History.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }

    public boolean insert(String title, String year, String month, String day, String content) {
        History history = new History(title, year, month, day, content);
        try {
            return historyDao.create(history) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<History> query(String month, String day) {
        try {
            return historyDao.queryBuilder().where().eq("month", month).and().eq("day", day).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<History> query(String key) {
        try {
            return historyDao.queryBuilder().where().in("title", key).or().eq("content", key).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
