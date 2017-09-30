package org.caojun.decibelman.room

import cn.bmob.v3.BmobObject

/**
 * Created by CaoJun on 2017/9/30.
 */
class DIBmob: BmobObject {
    var database_time: Long = 0//数据库建立时间
    var imei: String = ""
    var random_id: String = ""//四位随机数
    var time: Long = 0

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var decibel_min: Float = 0f
    var decibel_max: Float = 0f
    var decibel_average: Float = 0f

    constructor(di: DecibelInfo) {
        database_time = di.database_time
        imei = di.imei
        random_id = di.random_id
        time = di.time

        latitude = di.latitude
        longitude = di.longitude
        decibel_min = di.decibel_min
        decibel_max = di.decibel_max
        decibel_average = di.decibel_average
    }
}