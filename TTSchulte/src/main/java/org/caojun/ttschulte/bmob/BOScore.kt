package org.caojun.ttschulte.bmob

import cn.bmob.v3.BmobObject
import org.caojun.ttschulte.room.Score

/**
 * Created by CaoJun on 2018-1-16.
 */
class BOScore: BmobObject {

    var name: String = ""
    var score: Float = 0f
    var layout: Int = 0
    var type: Int = 0
    var time: Long = 0
    var imei: String = ""

    constructor(name: String, score: Float, layout: Int, type: Int, time: Long, imei: String) {
        this.name = name
        this.score = score
        this.layout = layout
        this.type = type
        this.time = time
        this.imei = imei
    }
}