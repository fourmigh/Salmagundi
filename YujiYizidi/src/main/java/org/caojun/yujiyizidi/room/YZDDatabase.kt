package org.caojun.yujiyizidi.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by CaoJun on 2018-1-22.
 */
@Database(entities = arrayOf(Goods::class, Customer::class, Order::class, OrderGoods::class), version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class YZDDatabase : RoomDatabase {
    constructor() : super()

    abstract fun getGoods(): GoodsDao
    abstract fun getCustomer(): CustomerDao
    abstract fun getOrder(): OrderDao
    abstract fun getOrderGoods(): OrderGoodsDao

    companion object {
        private var INSTANCE: YZDDatabase? = null

        @JvmStatic fun getDatabase(context: Context): YZDDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, YZDDatabase::class.java, "tzd_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}