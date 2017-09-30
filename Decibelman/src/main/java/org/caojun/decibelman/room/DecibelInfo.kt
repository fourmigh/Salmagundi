package org.caojun.decibelman.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.os.Parcel
import android.os.Parcelable

/**
 * 数据库第一条数据仅用于保存用户ID
 * Created by CaoJun on 2017/9/25.
 */
@Entity(primaryKeys = arrayOf("database_time","imei","random_id","time"))
open class DecibelInfo: Parcelable {
    var database_time: Long = 0//数据库建立时间
    var imei: String = ""
    var random_id: String = ""//四位随机数
    var time: Long = 0

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var decibel_min: Float = 0f
    var decibel_max: Float = 0f
    var decibel_average: Float = 0f

    constructor()
    constructor(_in: Parcel): this() {
        database_time = _in.readLong()
        imei = _in.readString()
        random_id = _in.readString()

        latitude = _in.readDouble()
        longitude = _in.readDouble()
        decibel_min = _in.readFloat()
        decibel_average = _in.readFloat()
        decibel_max = _in.readFloat()
        time = _in.readLong()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(database_time)
        dest.writeString(imei)
        dest.writeString(random_id)

        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
        dest.writeFloat(decibel_min)
        dest.writeFloat(decibel_average)
        dest.writeFloat(decibel_max)
        dest.writeLong(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<DecibelInfo> = object : Parcelable.Creator<DecibelInfo> {
            override fun createFromParcel(_in: Parcel): DecibelInfo {
                return DecibelInfo(_in)
            }

            override fun newArray(size: Int): Array<DecibelInfo?> {
                return arrayOfNulls(size)
            }
        }
    }
}