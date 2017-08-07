package org.caojun.rcn.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by CaoJun on 2017/8/7.
 */

public class DiaryDatabase extends OrmLiteSqliteOpenHelper {

    private static DiaryDatabase diaryDatabase;
    private static Dao<Diary, Integer> diaryDao;

    public static DiaryDatabase getInstance(Context context) {
        if(diaryDatabase == null) {
            diaryDatabase = new DiaryDatabase(context);
            try {
                diaryDao = diaryDatabase.getDao(Diary.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return diaryDatabase;
    }

    public DiaryDatabase(Context context) {
        super(context, "diary-db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Diary.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Diary.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreate(db, connectionSource);
    }

    public boolean insert(Diary diary) {
        try {
            return diaryDao.create(diary) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Diary diary) {
        try {
            return diaryDao.update(diary) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Diary diary) {
        try {
            return diaryDao.delete(diary) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Diary> query() {
        try {
            return diaryDao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
