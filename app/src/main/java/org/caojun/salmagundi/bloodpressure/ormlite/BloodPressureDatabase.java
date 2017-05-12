package org.caojun.salmagundi.bloodpressure.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by fourm on 2017/5/9.
 */

public class BloodPressureDatabase extends OrmLiteSqliteOpenHelper {

    private static BloodPressureDatabase bloodPressureDatabase;
    private static Dao<BloodPressure, Integer> bloodPressureDao;

    public static BloodPressureDatabase getInstance(Context context) {
        if(bloodPressureDatabase == null) {
            bloodPressureDatabase = new BloodPressureDatabase(context);
            try {
                bloodPressureDao = bloodPressureDatabase.getDao(BloodPressure.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bloodPressureDatabase;
    }

    public BloodPressureDatabase(Context context) {
        super(context, "bloodpressure-db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, BloodPressure.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, BloodPressure.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }

    /**
     * 服药记录
     * @param time
     * @return
     */
    public int insert(long time) {
        BloodPressure bloodPressure = new BloodPressure(time);
        try {
            return bloodPressureDao.create(bloodPressure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 体重记录
     * @param time
     * @param weight
     * @return
     */
    public int insert(long time, float weight) {
        BloodPressure bloodPressure = new BloodPressure(time, weight);
        try {
            return bloodPressureDao.create(bloodPressure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 血压记录
     * @param time
     * @param high
     * @param low
     * @param pulse
     * @param isLeft
     * @param device
     * @return
     */
    public int insert(long time, int high, int low, int pulse, boolean isLeft, int device) {
        BloodPressure bloodPressure = new BloodPressure(time, high, low, pulse, isLeft, device);
        try {
            return bloodPressureDao.create(bloodPressure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int update(BloodPressure bloodPressure) {
        try {
            return bloodPressureDao.update(bloodPressure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<BloodPressure> query() {
        try {
            return bloodPressureDao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
