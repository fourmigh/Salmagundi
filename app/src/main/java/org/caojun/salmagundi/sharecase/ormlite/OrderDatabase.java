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

public class OrderDatabase extends OrmLiteSqliteOpenHelper {
    private static OrderDatabase database;
    private static Dao<Order, Integer> dao;

    public static OrderDatabase getInstance(Context context) {
        if(database == null) {
            database = new OrderDatabase(context);
            try {
                dao = database.getDao(Order.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return database;
    }

    public OrderDatabase(Context context) {
        super(context, "order-db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Order.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Order.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreate(db, connectionSource);
    }

    public Order insert(Order order) {
        try {
            return dao.createIfNotExists(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order update(Order order) {
        try {
            if (dao.update(order) > 0) {
                return dao.queryForId(order.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> query() {
        try {
            return dao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> query(String columnName, Object value) {
        try {
            return dao.queryBuilder().where().eq(columnName, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(Order order) {
        try {
            if (dao.delete(order) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
