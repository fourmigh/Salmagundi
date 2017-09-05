package org.caojun.signman.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by CaoJun on 2017/8/31.
 */
@Database(entities = arrayOf(App::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase {
    constructor() : super()

    abstract fun getAppDao(): AppDao

    companion object {
        private var INSTANCE: AppDatabase? = null

//        @JvmStatic fun getInMemoryDatabase(context: Context): AppDatabase {
//            if (INSTANCE == null) {
//                INSTANCE = Room.inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java).allowMainThreadQueries().build()
//            }
//            return INSTANCE!!
//        }

        @JvmStatic fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}