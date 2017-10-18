package org.caojun.morseman.utils

import org.caojun.morseman.Morse
import java.util.Hashtable

/**
 * Created by CaoJun on 2017/10/9.
 */
object MorseUtils {

    val Time = 500.toLong()

    val Type_Symbol = 0//符号表示
    val Type_Number = 1//数字表示
    val Type_Word = 2//单词表示

    val CharMorse = charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', ':', ',', ';', '?', '=', '\'', '/', '!', '-', '_', '"', '(', ')', '$', '&', '@', '+')
    val Dit = arrayOf('.', 1.toByte(), "Dit")
    val Dah = arrayOf('-', 3.toByte(), "Dah")
    val Space1 = arrayOf(' ', (-1).toByte(), ' ')//点划间隔
    val Space3 = arrayOf(',', (-3).toByte(), ',')//字符间隔
    val Space7 = arrayOf('/', (-7).toByte(), '/')//单词间隔

    /**
     * 字符转莫尔斯码
     */
    fun toMorse(c: Char): String {
        when (c) {
            CharMorse[0], CharMorse[26] -> return ".-"//a
            CharMorse[1], CharMorse[27] -> return "-..."//b
            CharMorse[2], CharMorse[28] -> return "-.-."//c
            CharMorse[3], CharMorse[29] -> return "-.."//d
            CharMorse[4], CharMorse[30] -> return "."//e
            CharMorse[5], CharMorse[31] -> return "..-."//f
            CharMorse[6], CharMorse[32] -> return "--."//g
            CharMorse[7], CharMorse[33] -> return "...."//h
            CharMorse[8], CharMorse[34] -> return ".."//i
            CharMorse[9], CharMorse[35] -> return ".---"//j
            CharMorse[10], CharMorse[36] -> return "-.-"//k
            CharMorse[11], CharMorse[37] -> return ".-.."//l
            CharMorse[12], CharMorse[38] -> return "--"//m
            CharMorse[13], CharMorse[39] -> return "-."//n
            CharMorse[14], CharMorse[40] -> return "---"//o
            CharMorse[15], CharMorse[41] -> return ".--."//p
            CharMorse[16], CharMorse[42] -> return "--.-"//q
            CharMorse[17], CharMorse[43] -> return ".-."//r
            CharMorse[18], CharMorse[44] -> return "..."//s
            CharMorse[19], CharMorse[45] -> return "-"//t
            CharMorse[20], CharMorse[46] -> return "..-"//u
            CharMorse[21], CharMorse[47] -> return "...-"//v
            CharMorse[22], CharMorse[48] -> return ".--"//w
            CharMorse[23], CharMorse[49] -> return "-..-"//x
            CharMorse[24], CharMorse[50] -> return "-.--"//y
            CharMorse[25], CharMorse[51] -> return "--.."//z
            CharMorse[52] -> return "-----"//0
            CharMorse[53] -> return ".----"//1
            CharMorse[54] -> return "..---"//2
            CharMorse[55] -> return "...--"//3
            CharMorse[56] -> return "....-"//4
            CharMorse[57] -> return "....."//5
            CharMorse[58] -> return "-...."//6
            CharMorse[59] -> return "--..."//7
            CharMorse[60] -> return "---.."//8
            CharMorse[61] -> return "----."//9
            CharMorse[62] -> return ".-.-.-"//.
            CharMorse[63] -> return "---..."//:
            CharMorse[64] -> return "--..--"//,
            CharMorse[65] -> return "-.-.-."//;
            CharMorse[66] -> return "..--.."//?
            CharMorse[67] -> return "-...-"//=
            CharMorse[68] -> return ".---."//'
            CharMorse[69] -> return "-..-."///
            CharMorse[70] -> return "-.-.--"//!
            CharMorse[71] -> return "-....-"//-
            CharMorse[72] -> return "..--.-"//_
            CharMorse[73] -> return ".-..-."//"
            CharMorse[74] -> return "-.--."//(
            CharMorse[75] -> return "-.--.-"//)
            CharMorse[76] -> return "...-..-"//$
            CharMorse[77] -> return "...."//&
            CharMorse[78] -> return ".--.-."//@
            CharMorse[79] -> return ".-.-."//+
            else -> return ""
        }
    }

    private val htMorse = Hashtable<String, Char>()
    /**
     * 莫尔斯码转字符
     */
    private fun toChar(morse: String): Char {
        if (htMorse.containsKey(morse)) {
            return htMorse[morse]!!
        }
        for (i in 0 until CharMorse.size) {
            if (toMorse(CharMorse[i]).equals(morse)) {
                htMorse.put(morse, CharMorse[i])
                return CharMorse[i]
            }
        }
        htMorse.put(morse, ' ')
        return ' '
    }

    private fun toStringArray(string: String, separator: String): Array<String> {
        return if (!string.contains(separator)) {
            arrayOf<String>(string)
//        } else string.split(separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        } else string.split(separator).toTypedArray()
    }

    /**
     * 莫尔斯字符串转莫尔斯字符串数组
     */
    private fun toMorseStringArray(string: String): Array<String> {
        val strings = toStringArray(string, "/")
        val list = ArrayList<String>()
//        for (i in strings.indices) {
//            val s = toMorseStrings(strings[i], " ")
//            for (j in s.indices) {
//                list.add(s[j])
//            }
//        }

//        for (i in strings.indices) {
//            val s = toMorseStrings(strings[i], " ")
//            s.indices.mapTo(list) { s[it] }
//        }

        strings.indices
                .asSequence()
                .map { toStringArray(strings[it], " ") }
                .forEach { s -> s.indices.mapTo(list) { s[it] } }
        return list.toTypedArray()
    }

    /**
     * 莫尔斯码字符转byte数组
     */
    private fun morse2ByteArray(morse: String): ByteArray {
        val bytes = ByteArray(morse.length * 2)
        for (i in 0 until morse.length) {
            val c = morse[i]
            if (c == Dit[Type_Symbol]) {
                bytes[i * 2] = Dit[Type_Number] as Byte
            } else if (c == Dah[Type_Symbol]) {
                bytes[i * 2] = Dah[Type_Number] as Byte
            }
            if (i < morse.length - 1) {
                bytes[i * 2 + 1] = Space1[Type_Number] as Byte
            } else {
                bytes[i * 2 + 1] = Space3[Type_Number] as Byte
            }
        }
        return bytes
    }

    private fun addBytes(data1: ByteArray, data2: ByteArray): ByteArray {
        val data3 = ByteArray(data1.size + data2.size)
        System.arraycopy(data1, 0, data3, 0, data1.size)
        System.arraycopy(data2, 0, data3, data1.size, data2.size)
        return data3
    }

    /**
     * 原文转莫尔斯码
     */
    fun byteArray2Morse(byteArray: ByteArray): String {
        val sb = StringBuffer()
        for (i in byteArray.indices) {
            when (byteArray[i]) {
                Dit[Type_Number] -> sb.append(Dit[Type_Symbol])
                Dah[Type_Number] -> sb.append(Dah[Type_Symbol])
                Space1[Type_Number] -> sb.append(Space1[Type_Symbol])
                Space3[Type_Number] -> sb.append(Space3[Type_Symbol])
                Space7[Type_Number] -> sb.append(Space7[Type_Symbol])
            }
        }
        return sb.toString()
    }

    /**
     * 原文转byte数组
     */
    fun string2ByteArray(string: String): ByteArray {
        val stringArray = toStringArray(string, " ")
        val list = ArrayList<ByteArray>()
        for (i in 0 until stringArray.size) {
            for (j in 0 until stringArray[i].length) {
                val c = stringArray[i][j]
                if (c in CharMorse) {
                    val morse = toMorse(c)
                    val morseByteArray = morse2ByteArray(morse)
                    list.add(morseByteArray)
                }
            }
            //每个单词后加一个Space7
            list.add(byteArrayOf(Space7[Type_Number] as Byte))
        }
        var bytes = list[0]
        for (i in 1 until list.size) {
            bytes = addBytes(bytes, list[i])
        }
        return trim(bytes)
    }

    fun string2Morse(string: String): String {
        val stringArray = toStringArray(string, " ")
        val sb = StringBuffer()
        for (i in 0 until stringArray.size) {
            for (j in 0 until stringArray[i].length) {
                val c = stringArray[i][j]
                if (c in CharMorse) {
                    val morse = toMorse(c)
                    sb.append(morse)
                    //每个字符后加一个Space3
                    if (j < stringArray[i].length - 1) {
                        sb.append(Space3[Type_Word] as Char)
                    }
                }
            }
            //每个单词后加一个Space7
            if (i < stringArray.size - 1) {
                sb.append(Space7[Type_Word] as Char)
            }
        }
        return sb.toString()
    }

    /**
     * 去除多余的间隔符
     */
    private fun trim(byteArray: ByteArray): ByteArray {
        val list = ArrayList<Byte>()
        var i = 0
        while (i < byteArray.size - 1) {
            if (byteArray[i] == Space1[Type_Number] && byteArray[i + 1] == Space3[Type_Number]) {
                list.add(Space3[Type_Number] as Byte)
                i += 2
            } else if (byteArray[i] == Space3[Type_Number] && byteArray[i + 1] == Space7[Type_Number]) {
                list.add(Space7[Type_Number] as Byte)
                i += 2
            } else {
                list.add(byteArray[i])
                i ++
            }
        }
        return list.toByteArray()
    }

    /**
     * byte数组转原文
     */
    fun byteArray2String(byteArray: ByteArray): String {
        val stringBuffer = StringBuffer()
        val sb = StringBuffer()
        for (i in byteArray.indices) {
            when (byteArray[i]) {
                Dit[Type_Number] -> {
                    sb.append(Dit[Type_Symbol] as Char)
                }
                Dah[Type_Number] -> {
                    sb.append(Dah[Type_Symbol] as Char)
                }
                Space3[Type_Number], Space7[Type_Number] -> {
                    val char = toChar(sb.toString())
                    stringBuffer.append(char)
                    if (byteArray[i] == Space7[Type_Number]) {
                        stringBuffer.append(Space1[Type_Symbol] as Char)
                    }
                    sb.delete(0, sb.length)
                }
            }
        }
        if (!sb.isEmpty()) {
            val char = toChar(sb.toString())
            stringBuffer.append(char)
            sb.delete(0, sb.length)
        }
        return stringBuffer.toString()
    }

    //////////////////////////////////////////////////////////////////////////////
    //解析，通过摄像头获取颜色值，转化成byte数组

    private val colors = ArrayList<Int>()
    private var average = 0f
    private var morses = ArrayList<Morse>()
    private val morse: Morse = Morse()
    private var onMorseListener: OnMorseListener? = null

    interface OnMorseListener {
        fun onMorse(array: Array<String?>)
    }

    fun addColor(color: Int, onMorseListener: OnMorseListener) {
        colors.add(color)
        average = AverageUtils.add(color)

        val lightOn = color > average
        if (morse.inited) {
            //记时
            morse.time ++;
            if (lightOn != morse.on) {
                morse.setCode()
                morses.add(morse)
                val char = byteArray2String(morse.getCode())
                if (char != null) {
                    onMorseListener.onMorse(char)
                }
                morse.on = lightOn
                morse.time = 0
            }
        } else {
            //初始化
            morse.inited = true
            morse.on = lightOn
            morse.time = 0;
        }
    }

    private val sb = StringBuffer()
    /**
     * 实时解析莫尔斯码信号
     * 0：摩尔斯码
     * 1：原文
     */
    private fun byteArray2String(byte: Byte): Array<String?> {
        var value = byte

        if (value > 0) {
            if (value <= Dit[Type_Number] as Byte * 2) {
                value = Dit[Type_Number] as Byte
            } else if (value >= Dah[Type_Number] as Byte) {
                value = Dah[Type_Number] as Byte
            }
        } else if (value < 0) {
            if (value >= Space1[Type_Number] as Byte * 2) {
                value = Space1[Type_Number] as Byte
            } else if (value <= Space7[Type_Number] as Byte) {
                value = Space7[Type_Number] as Byte
            } else {
                value = Space3[Type_Number] as Byte
            }
        }

        when (value) {
            Dit[Type_Number] -> {
                val char = Dit[Type_Symbol] as Char
                sb.append(char)
                return arrayOf(char.toString(), null)
            }
            Dah[Type_Number] -> {
                val char = Dah[Type_Symbol] as Char
                sb.append(char)
                return arrayOf(char.toString(), null)
            }
            Space3[Type_Number], Space7[Type_Number] -> {
                var char2 = toChar(sb.toString()).toString()
                var char1 = Space3[Type_Word]
                sb.delete(0, sb.length)
                if (value == Space7[Type_Number]) {
                    char2 += Space1[Type_Word] as Char
                    char1 = Space7[Type_Word]
                }
                return arrayOf(char1.toString(), char2)
            }
            else -> {
                return arrayOf(null, null)
            }
        }
    }
}