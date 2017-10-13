package org.caojun.morseman

/**
 * Created by CaoJun on 2017/10/12.
 */
class Morse {
    var on = false//亮或灭
    var time = 0//持续时间
    var inited = false//未初始化
    private var code = 0//Dit,Dah,Space1,Space3,Space7的byte值

    fun setCode() {
        code = time;
        if (!on) {
            code = -code
        }
    }

    fun getCode(): Byte {
        return code.toByte()
    }
}