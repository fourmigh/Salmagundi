package org.caojun.salmagundi.taxicab.ormlite;

import android.content.Context;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by fourm on 2017/5/3.
 */

public class TaxicabDatabase extends OrmLiteSqliteOpenHelper {

    private static TaxicabDatabase taxicabDatabase;
    private static Dao<Taxicab, Integer> taxicabDao;

    public static TaxicabDatabase getInstance(Context context) {
        if(taxicabDatabase == null) {
            taxicabDatabase = new TaxicabDatabase(context);
            try {
                taxicabDao = taxicabDatabase.getDao(Taxicab.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return taxicabDatabase;
    }

    public TaxicabDatabase(Context context) {
        super(context, "taxicab-db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Taxicab.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Taxicab.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }

    public int insert(BigInteger a, BigInteger b) {
        Taxicab taxicab = new Taxicab(a, b);
        try {
            if (taxicab.getTaxicab().compareTo(BigInteger.ZERO) > 0) {
                return taxicabDao.create(taxicab);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Taxicab> query() {
        try {
//            return taxicabDao.queryBuilder().orderBy("taxicab", true).query();//false降序，true升序
            return taxicabDao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
