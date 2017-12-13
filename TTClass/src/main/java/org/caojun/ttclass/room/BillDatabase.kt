package org.caojun.ttclass.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by CaoJun on 2017-12-13.
 */
@Database(entities = arrayOf(Bill::class), version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class BillDatabase : RoomDatabase {
    constructor() : super()

    abstract fun getDao(): BillDao

    companion object {
        private var INSTANCE: BillDatabase? = null

        @JvmStatic fun getDatabase(context: Context): BillDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, BillDatabase::class.java, "bill_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}