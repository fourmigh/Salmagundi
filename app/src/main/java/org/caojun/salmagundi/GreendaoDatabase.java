package org.caojun.salmagundi;

import android.content.Context;
import org.caojun.salmagundi.passwordstore.greendao.DaoMaster;
import org.caojun.salmagundi.passwordstore.greendao.DaoSession;

/**
 * Greendao数据库基类
 * Created by CaoJun on 2017/2/15.
 */

public class GreendaoDatabase {
    protected DaoMaster daoMaster;
    protected DaoMaster.DevOpenHelper devOpenHelper;
    protected DaoSession daoSession;

    public GreendaoDatabase() {

    }

    public GreendaoDatabase(Context context, String name) {
        devOpenHelper = new DaoMaster.DevOpenHelper(context, name, null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }
}
