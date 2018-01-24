package org.caojun.yujiyizidi.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2018-1-22.
 */
@Dao
interface GoodsDao {
    @Query("SELECT * FROM goods ORDER BY name")
    fun query(): List<Goods>

    @Insert
    fun insert(vararg goods: Goods)

    @Update
    fun update(vararg goods: Goods): Int
}