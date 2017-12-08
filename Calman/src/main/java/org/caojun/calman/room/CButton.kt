package org.caojun.calman.room

import android.text.TextUtils

/**
 * Created by CaoJun on 2017/11/17.
 */
class CButton {
    companion object {
        const val Type_Digital = 0//数字键
        const val Type_Plus = 1//加数字
        const val Type_Minus = 2//减数字
        const val Type_Multiple = 3//乘数字
        const val Type_Divide = 4//除数字
        const val Type_Delete = 5//删除键
        const val Type_Transfer = 6//转换键
        const val Type_Square = 7//平方
        const val Type_Cube = 8//立方
        const val Type_Sign = 9//正负号
        const val Type_Reverse = 10//颠倒键
        const val Type_Sum = 11//求和键
        const val Type_SquareRoot = 12//平方根
        const val Type_CubeRoot = 13//立方根
        const val Type_MoveLeft = 14//左移
        const val Type_MoveRight = 15//右移
        const val Type_Mirror = 16//镜像键
        const val Type_Change = 17//改变按钮值
        const val Type_Store = 18//存储键
        const val Type_Lnv10 = 19//Mod10
        const val Type_Gate = 20//传送门
    }

    //类型
    var type = Type_Digital
    //键值
    var digital = 0
    //副值
    var vice = 0
    //存储值
    var store: String? = null

    //按键坐标
    //0,0：购买提示
    //0,2：CLR（当前关卡初始化）
    //2,0：设置
    //其他六个位置随机按键
    var position = intArrayOf(0,0)//行号,列号

    /**
     * 0：数字键
     * 1：加数字
     * 2：减数字
     * 3：乘数字
     * 4：除数字
     * 17：改变按钮值
     */
    constructor(digital: Int, type: Int) {
        this.type = type
        this.digital = digital
    }

    /**
     * 6：转换键
     * 20：传送门
     */
    constructor(digital: Int, vice: Int, type: Int) {
        this.type = type
        this.digital = digital
        this.vice = vice
    }

    /**
     * 5：删除键
     * 7：平方
     * 8：立方
     * 9：正负号
     * 10：颠倒键
     * 11：求和键
     * 12：平方根
     * 13：立方根
     * 14：左移
     * 15：右移
     * 16：镜像键
     * 18：存储键
     * 19：Mod10
     */
    constructor(type: Int) {
        this.type = type
        store = null
    }

    fun getText(): String? {
        when (type) {
            Type_Digital -> {
                return digital.toString()
            }//数字键
            Type_Plus -> {
                return "+ " + digital
            }//加数字
            Type_Minus -> {
                return "- " + digital
            }//减数字
            Type_Multiple -> {
                return "× " + digital
            }//乘数字
            Type_Divide -> {
                return "÷ " + digital
            }//除数字
            Type_Delete -> {
                return "<<"
            }//删除键
            Type_Transfer -> {
                return digital.toString() + " => " + vice
            }//转换键
            Type_Square -> {
                return "x²"
            }//平方
            Type_Cube -> {
                return "x³"
            }//立方
            Type_Sign -> {
                return "+/-"
            }//正负号
            Type_Reverse -> {
                return "Reverse"
            }//颠倒键
            Type_Sum -> {
                return "SUM"
            }//求和键
            Type_SquareRoot -> {
                return "√￣"
            }//平方根
            Type_CubeRoot -> {
                return "³√￣"
            }//立方根
            Type_MoveLeft -> {
                return "<Shift"
            }//左移
            Type_MoveRight -> {
                return "Shift>"
            }//右移
            Type_Mirror -> {
                return "Mirror"
            }//镜像键
            Type_Change -> {
                return "[+]" + digital
            }//改变按钮值
            Type_Store -> {
                return if (TextUtils.isEmpty(store)) {
                    "Store"
                } else store + "\nStore"
            }//存储键
            Type_Lnv10 -> {
                return "lnv10"
            }//Mod10
            Type_Gate -> {
                return vice.toString() + " to " + digital
            }//传送门
        }
        return null
    }
}