package org.caojun.calman.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.socks.library.KLog
import org.caojun.calman.utils.GameUtils

/**
 * 关卡
 * Created by CaoJun on 2017/11/20.
 */
@Entity
class Level {
    var initial = 0//初始值

    var goal = 0//目标值

    var moves = 0//步数

    @Ignore
    val hints = 3//提示数

    var currentHints = hints//当前提示数

    var currentValue = initial//当前值

    var currentStep = moves//当前步数

    @PrimaryKey
    var level = 0//当前等级，已生成的关卡数据保存在本地，不再随机生成

    fun init() {
        //NumButton已设置好，不在此处检测其合法性
        //生成初始值
        initial = GameUtils.getRandom(GameUtils.MinInitial, GameUtils.MaxInitial)
        currentValue = initial
        //生成按键
        val position = ArrayList<String>()
        position.add("00")//购买提示
        position.add("02")//CLR（当前关卡初始化）
        position.add("20")//设置
        GameUtils.IdButton.clear()
        for (i in 0 until GameUtils.NumButton.size) {
            if (GameUtils.NumButton[i] < 1) {
                continue
            }
            for (j in 0 until GameUtils.NumButton[i]) {
                var xy = GameUtils.getRandomPosition()
                var p = xy[0].toString() + xy[1].toString()
                while (p in position) {
                    xy = GameUtils.getRandomPosition()
                    p = xy[0].toString() + xy[1].toString()
                }
                val cButton = GameUtils.createCButton(i) ?: continue
                cButton.position = xy
                position.add(p)
                KLog.d("init", p + " : " + GameUtils.PosiontId[p])
                GameUtils.IdButton.put(GameUtils.PosiontId[p], cButton)
            }
        }
        //生成步数
        moves = GameUtils.getRandom(GameUtils.MinMoves, GameUtils.MaxMoves)
        currentStep = moves
    }
}