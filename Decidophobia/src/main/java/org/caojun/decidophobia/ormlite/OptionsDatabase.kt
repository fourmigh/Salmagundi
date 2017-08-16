package org.caojun.decidophobia.ormlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import java.sql.SQLException

/**
 * Created by CaoJun on 2017/8/16.
 */
class OptionsDatabase: OrmLiteSqliteOpenHelper {

    companion object {
        var instance: OptionsDatabase? = null
        var dao: Dao<Options, Int>? = null
        fun getInstance(context: Context): OptionsDatabase {
            if (instance == null) {
                instance = OptionsDatabase(context)
                dao = instance?.getDao(Options::class.java)
            }
            return instance!!
        }
    }

    constructor(context: Context?) : super(context, "options-db", null, 1)

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        try {
            TableUtils.createTableIfNotExists<Options>(connectionSource, Options::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        try {
            TableUtils.dropTable<Options, Any>(connectionSource, Options::class.java, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        onCreate(database, connectionSource)
    }

    fun insert(options: Options): Boolean {
        try {
            return dao?.create(options) == 1
        } catch(e: Exception) {
            return false
        }
    }

    fun update(options: Options): Boolean {
        try {
            return dao?.update(options) == 1
        } catch(e: Exception) {
            return false
        }
    }

    fun delete(options: Options): Boolean {
        try {
            return dao?.delete(options) == 1
        } catch(e: Exception) {
            return false
        }
    }

    fun query(): List<Options>? {
        return dao?.queryBuilder()?.query()
    }
}