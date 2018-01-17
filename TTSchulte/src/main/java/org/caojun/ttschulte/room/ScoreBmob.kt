package org.caojun.ttschulte.room

import cn.bmob.v3.BmobObject

/**
 * Created by CaoJun on 2018-1-17.
 */
class ScoreBmob: BmobObject {

    var name: String = ""
    var score: Float = 0f
    var layout: Int = 0
    var type: Int = 0
    var time: Long = 0
    var imei: String = ""

    constructor(score: Score, imei: String) {
        this.name = score.name
        this.score = score.score
        this.layout = score.layout
        this.type = score.type
        this.time = score.time
        this.imei = imei
    }
}