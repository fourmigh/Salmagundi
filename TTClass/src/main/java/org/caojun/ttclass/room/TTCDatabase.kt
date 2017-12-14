package org.caojun.ttclass.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by CaoJun on 2017-12-13.
 */
@Database(entities = arrayOf(Bill::class, IClass::class, School::class, Sign::class, Teacher::class), version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class TTCDatabase : RoomDatabase {
    constructor() : super()

    abstract fun getBill(): BillDao

    abstract fun getIClass(): IClassDao

    abstract fun getSchool(): SchoolDao

    abstract fun getSign(): SignDao

    abstract fun getTeacher(): TeacherDao

    companion object {
        private var INSTANCE: TTCDatabase? = null

        @JvmStatic fun getDatabase(context: Context): TTCDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, TTCDatabase::class.java, "ttclass_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}