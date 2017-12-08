package org.caojun.calman.utils

import android.widget.Button
import com.socks.library.KLog
import org.caojun.calman.R
import org.caojun.calman.activity.BaseActivity
import org.caojun.calman.room.CButton
import org.caojun.calman.room.Level
import org.caojun.calman.room.LevelDatabase
import java.util.*

/**
 * Created by CaoJun on 2017/11/17.
 */
object GameUtils {

    const val Max_Button = 6//最大按键数

    /**
     * 游戏状态
     */
    enum class Status {
        Start, Error, Win
    }

    var MinDigital = 1
    var MaxDigital = 9
    var MinPlus = 1
    var MaxPlus = 9
    var MinMinus = 1
    var MaxMinus = 9
    var MinMultiple = 1
    var MaxMultiple = 9
    var MinDivide = 1
    var MaxDivide = 9
    var MinChange = 1
    var MaxChange = 9
    var MinTransfer = 1
    var MaxTransfer = 99
    var MinGate = 1
    var MaxGate = 5

    var MinInitial = -999
    var MaxInitial = 999
    var MinMoves = 1
    var MaxMoves = 9

    var status = Status.Start

    val IdButton = Hashtable<Int, CButton>()
    val PosiontId = TreeMap<String, Int>()
    var Buttons = arrayOfNulls<Button>(9)
    var level: Level? = null
    val buttonIds = ArrayList<Int>()

    /**
     * 按键数是否合法
     */
    fun isNumButtonLegal(): Boolean {
        var number = 0
        for (i in 0 until NumButton.size) {
            if (NumButton[i] < 0) {
                return false
            }
            number += NumButton[i]
            if (number > Max_Button) {
                return false
            }
        }
        return true
    }

    fun initialize() {
        if (PosiontId.isNotEmpty()) {
            return
        }
        PosiontId.put("00", R.id.btn00)
        PosiontId.put("01", R.id.btn01)
        PosiontId.put("02", R.id.btn02)
        PosiontId.put("10", R.id.btn10)
        PosiontId.put("11", R.id.btn11)
        PosiontId.put("12", R.id.btn12)
        PosiontId.put("20", R.id.btn20)
        PosiontId.put("21", R.id.btn21)
        PosiontId.put("22", R.id.btn22)
    }

    /**
     * 生成新关卡
     */
    fun newLevel(activity: BaseActivity) {
        level = Level()
        level!!.init()
        initGame(activity, true)
    }

    private fun initGame(activity: BaseActivity, isNew: Boolean) {
        val level = this.level!!
//        initButtons(activity)
        if (isNew) {
            calGoal()
            level.level ++
            LevelDatabase.getDatabase(activity).getDao().insert(level)
        } else {
            activity.onGameRestart(level)
        }
        activity.onGameInited(level)
        status = Status.Start
    }

    /**
     * 计算目标值
     * 随机按按钮，记录按钮ID
     */
    private fun calGoal() {
        buttonIds.clear()
        //TODO

    }

    /**
     * 初始化已生成的关卡
     */
    private fun initLevel(activity: BaseActivity) {
        initGame(activity, false)
    }

    fun initButtons(activity: BaseActivity) {
        val level = this.level!!
        var index = 0
        for (i in PosiontId.entries) {
            Buttons[index] = activity.findViewById(i.value)
            when (index) {
                0 -> {
                    //00提示数
                    if (level.currentHints < 1) {
                        Buttons[index]?.setText(R.string.add_hints)
                    } else {
                        Buttons[index]?.text = activity.getString(R.string.hints, level.currentHints.toString())
                    }
                    Buttons[index]?.setOnClickListener {
                        doHint()
                    }
                }
                2 -> {
                    //02CLR（当前关卡初始化）
                    Buttons[index]?.text = "CLR"
                    Buttons[index]?.setOnClickListener {
                        initLevel(activity)
                        initButtons(activity)
                    }
                }
                6 -> {
                    //20设置
                    Buttons[index]?.setText(R.string.action_settings)
                    Buttons[index]?.setOnClickListener {
                        gotoSettings()
                    }
                }
                else -> {
                    val cButton = IdButton[Buttons[index]?.id]
                    if (cButton == null) {
                        Buttons[index]?.text = null
                        Buttons[index]?.isEnabled = false
                    } else {
                        Buttons[index]?.text = cButton.getText()
                        Buttons[index]?.isEnabled = true
                        Buttons[index]?.setOnClickListener {
                            doButton(cButton)
                            activity.onCButtonClicked(level)
                        }
                    }
                }
            }
            index ++;
        }
    }

    private fun gotoSettings() {
        //TODO
    }

    private fun doHint() {
        //TODO
    }

    fun createCButton(type: Int): CButton? {
        when (type) {
            CButton.Type_Digital -> {
                return createDigital()
            }//数字键
            CButton.Type_Plus -> {
                return createPlus()
            }//加数字
            CButton.Type_Minus -> {
                return createMinus()
            }//减数字
            CButton.Type_Multiple -> {
                return createMultiple()
            }//乘数字
            CButton.Type_Divide -> {
                return createDivide()
            }//除数字
            CButton.Type_Delete -> {
                return createDelete()
            }//删除键
            CButton.Type_Transfer -> {
                return createTransfer()
            }//转换键
            CButton.Type_Square -> {
                return createSquare()
            }//平方
            CButton.Type_Cube -> {
                return createCube()
            }//立方
            CButton.Type_Sign -> {
                return createSign()
            }//正负号
            CButton.Type_Reverse -> {
                return createReverse()
            }//颠倒键
            CButton.Type_Sum -> {
                return createSum()
            }//求和键
            CButton.Type_SquareRoot -> {
                return createSquareRoot()
            }//平方根
            CButton.Type_CubeRoot -> {
                return createCubeRoot()
            }//立方根
            CButton.Type_MoveLeft -> {
                return createMoveLeft()
            }//左移
            CButton.Type_MoveRight -> {
                return createMoveRight()
            }//右移
            CButton.Type_Mirror -> {
                return createMirror()
            }//镜像键
            CButton.Type_Change -> {
                return createChange()
            }//改变按钮值
            CButton.Type_Store -> {
                return createStore()
            }//存储键
            CButton.Type_Lnv10 -> {
                return createLnv10()
            }//Mod10
            CButton.Type_Gate -> {
                return createGate()
            }//传送门
        }
        return null
    }

    fun getRandomPosition(): IntArray {
        val x = getRandom(0, 2)
        val y = getRandom(0, 2)
        return intArrayOf(x, y)
    }

    private fun setCurrentValue(level: Level, value: String) {
        try {
            level.currentValue = Integer.parseInt(value)
        } catch (e: Exception) {
            status = Status.Error
        }
    }

    private fun doButton(cButton: CButton) {
        if (status != Status.Start) {
            return
        }
        val level = this.level!!
        if (level.moves > 0) {
            level.moves--
        } else {
            return
        }
        var value = level.currentValue.toString()
        when (cButton.type) {
            CButton.Type_Digital -> {
                value += cButton.digital
                setCurrentValue(level, value)
            }//数字键
            CButton.Type_Plus -> {
                level.currentValue += cButton.digital
            }//加数字
            CButton.Type_Minus -> {
                level.currentValue -= cButton.digital
            }//减数字
            CButton.Type_Multiple -> {
                level.currentValue *= cButton.digital
            }//乘数字
            CButton.Type_Divide -> {
                if (level.currentValue % cButton.digital > 0) {
                    status = Status.Error
                } else {
                    level.currentValue /= cButton.digital
                }
            }//除数字
            CButton.Type_Delete -> {
                level.currentValue /= 10
            }//删除键
            CButton.Type_Transfer -> {
                value = value.replace(cButton.digital.toString(), cButton.vice.toString())
                setCurrentValue(level, value)
            }//转换键
            CButton.Type_Square -> {
                try {
                    level.currentValue = Math.pow(level.currentValue.toDouble(), 2.toDouble()).toInt()
                } catch (e: Exception) {
                    status = Status.Error
                }
            }//平方
            CButton.Type_Cube -> {
                try {
                    level.currentValue = Math.pow(level.currentValue.toDouble(), 3.toDouble()).toInt()
                } catch (e: Exception) {
                    status = Status.Error
                }
            }//立方
            CButton.Type_Sign -> {
                level.currentValue = - level.currentValue
            }//正负号
            CButton.Type_Reverse -> {
                val negative = level.currentValue < 0
                value = getReverse(value)
                setCurrentValue(level, value)
                if (negative) {
                    level.currentValue = - level.currentValue;
                }
            }//颠倒键
            CButton.Type_Sum -> {
                val negative = level.currentValue < 0
                var charArray = value.toCharArray()
                val start = if (negative) 1 else 0
                var sum = (start until charArray.size).sumBy { charArray[it].toInt() }
                if (negative) {
                    sum = - sum
                }
                level.currentValue = sum
            }//求和键
            CButton.Type_SquareRoot -> {
                if (level.currentValue < 0) {
                    status = Status.Error
                    return
                }
                val sqrt = Math.sqrt(level.currentValue.toDouble())
                if (sqrt.toString().contains(".")) {
                    //有小数
                    status = Status.Error
                    return
                }
                level.currentValue = sqrt.toInt()
            }//平方根
            CButton.Type_CubeRoot -> {
                val sqrt = Math.pow(level.currentValue.toDouble(), 1.0/3)
                if (sqrt.toString().contains(".")) {
                    status = Status.Error
                    return
                }
                level.currentValue = sqrt.toInt()
            }//立方根
            CButton.Type_MoveLeft -> {
                value = getMove(value, true)
                setCurrentValue(level, value)
            }//左移
            CButton.Type_MoveRight -> {
                value = getMove(value, false)
                setCurrentValue(level, value)
            }//右移
            CButton.Type_Mirror -> {
                var mirror = getReverse(value)
                value += mirror
                setCurrentValue(level, value)
            }//镜像键
            CButton.Type_Change -> {
                //TODO
            }//改变按钮值
            CButton.Type_Store -> {
                cButton.store = level.currentValue.toString()
            }//存储键
            CButton.Type_Lnv10 -> {
                var charArray = value.toCharArray()
                var newValue = StringBuffer()
                (0 until charArray.size)
                        .map { charArray[it] }
                        .forEach {
                            if (it in '0' .. '9') {
                                newValue.append(10 - it.toInt())
                            } else {
                                newValue.append(it)
                            }
                        }
                level.currentValue = Integer.parseInt(newValue.toString())
            }//Mod10
            CButton.Type_Gate -> {
                //TODO
            }//传送门
        }
    }

    private fun getMove(value: String, left: Boolean): String {
        var charArray = value.toCharArray()
        val indexStart = if (value[0] == '-') 1 else 0
        val rest = if (left) {value.substring(indexStart + 1)} else {value.substring(indexStart, value.length - 2)}
        var char = if (left) {charArray[indexStart]} else {charArray[charArray.size - 1]}
        var sb = StringBuffer()
        if (indexStart > 0) {
            sb.append(charArray[0])
        }
        if (left) {
            sb.append(rest)
            sb.append(char)
        } else {
            sb.append(char)
            sb.append(rest)
        }
        return sb.toString()
    }

    private fun getReverse(value: String): String {
        var charArray = value.toCharArray()
        var newValue = StringBuffer()
        (0 until charArray.size).forEach { i ->
            val char = charArray[charArray.size - i - 1]
            if (char in '0' .. '9') {
                newValue.append(char)
            }
        }
        return newValue.toString()
    }

    //按键数
    var NumButton = intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    private fun createDigital(digital: Int): CButton = CButton(digital, CButton.Type_Digital)
    private fun createDigital(): CButton = createDigital(getDigital())

    private fun createPlus(digital: Int): CButton = CButton(digital, CButton.Type_Plus)
    private fun createPlus(): CButton = createPlus(getPlus())

    private fun createMinus(digital: Int): CButton = CButton(digital, CButton.Type_Minus)
    private fun createMinus(): CButton = createMinus(getMinus())

    private fun createMultiple(digital: Int): CButton = CButton(digital, CButton.Type_Multiple)
    private fun createMultiple(): CButton = createMultiple(getMultiple())

    private fun createDivide(digital: Int): CButton = CButton(digital, CButton.Type_Divide)
    private fun createDivide(): CButton = createDivide(getDivide())

    private fun createChange(digital: Int): CButton = CButton(digital, CButton.Type_Change)
    private fun createChange(): CButton = createChange(getChange())

    private fun createTransfer(digital: Int, vice: Int): CButton = CButton(digital, vice, CButton.Type_Transfer)
    private fun createTransfer(): CButton {
        val dv = getTransfer()
        return createTransfer(dv[0], dv[1])
    }

    private fun createGate(digital: Int, vice: Int): CButton = CButton(digital, vice, CButton.Type_Gate)
    private fun createGate(): CButton {
        val minmax = getGate()
        return createGate(minmax[0], minmax[1])
    }

    private fun createDelete(): CButton = CButton(CButton.Type_Delete)

    private fun createSquare(): CButton = CButton(CButton.Type_Square)

    private fun createCube(): CButton = CButton(CButton.Type_Cube)

    private fun createSign(): CButton = CButton(CButton.Type_Sign)

    private fun createReverse(): CButton = CButton(CButton.Type_Reverse)

    private fun createSum(): CButton = CButton(CButton.Type_Sum)

    private fun createSquareRoot(): CButton = CButton(CButton.Type_SquareRoot)

    private fun createCubeRoot(): CButton = CButton(CButton.Type_CubeRoot)

    private fun createMoveLeft(): CButton = CButton(CButton.Type_MoveLeft)

    private fun createMoveRight(): CButton = CButton(CButton.Type_MoveRight)

    private fun createMirror(): CButton = CButton(CButton.Type_Mirror)

    private fun createStore(): CButton = CButton(CButton.Type_Store)

    private fun createLnv10(): CButton = CButton(CButton.Type_Lnv10)

    fun getRandom(min: Int, max: Int): Int = (Math.random() * (max + 1 - min) + min).toInt()

    private fun getDigital(): Int = getRandom(MinDigital, MaxDigital)

    private fun getPlus(): Int = getRandom(MinPlus, MaxPlus)

    private fun getMinus(): Int = getRandom(MinMinus, MaxMinus)

    private fun getMultiple(): Int = getRandom(MinMultiple, MaxMultiple)

    private fun getDivide(): Int = getRandom(MinDivide, MaxDivide)

    private fun getChange(): Int = getRandom(MinChange, MaxChange)

    private fun getTransfer(): IntArray {
        val digital = getRandom(MinTransfer, MaxTransfer)
        val vice = getRandom(MinTransfer, MaxTransfer)
        return intArrayOf(digital, vice)
    }

    private fun getGate(): IntArray {
        val min = getRandom(MinGate, MaxGate / 2)//个位数,出口
        val max = getRandom(MaxGate / 2, MaxGate)//入口
        return intArrayOf(min, max)
    }
}