package org.caojun.salmagundi.secure.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * RSA公私钥数据库
 * Created by CaoJun on 2017/1/6.
 */

public class RSAKeyDatabase extends SQLiteOpenHelper {

    public RSAKeyDatabase(Context context) {
        super(context, "rsakey.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RSAKey.TABLE_NAME + " ("
                + RSAKey._ID + " INTEGER PRIMARY KEY,"
                + RSAKey.Private_Key + " BLOB not null,"
                + RSAKey.Public_Key + " BLOB not null"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 插入数据成功后返回_ID
     * @param values
     * @return
     */
    public int insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        long result = db.insert(RSAKey.TABLE_NAME, null, values);
        if(result == -1)
        {
            return -1;
        }
        Cursor cursor = db.rawQuery("select LAST_INSERT_ROWID() ",null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        return db.delete(RSAKey.TABLE_NAME, RSAKey._ID + "=?", new String[] { String.valueOf(id) });
    }

    public Cursor query() {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return null;
        }
        return db.query(RSAKey.TABLE_NAME, null, null, null, null, null, null);
    }
}
