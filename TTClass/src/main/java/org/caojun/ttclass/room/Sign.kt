package org.caojun.ttclass.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.util.Hashtable
import java.util.Date

/**
 * Created by CaoJun on 2017-12-12.
 */
@Entity(tableName = "sign")
@ForeignKey(onDelete = ForeignKey.CASCADE, entity = IClass::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idClass"))
class Sign: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var idClass: Int = -1
    var time: Date = Date()
    var note: String = ""

//    var image0: Bitmap? = null
//    var image1: Bitmap? = null
//    var image2: Bitmap? = null
//    var image3: Bitmap? = null
//    var image4: Bitmap? = null
//    var image5: Bitmap? = null
//    var image6: Bitmap? = null
//    var image7: Bitmap? = null
//    var image8: Bitmap? = null
//
//    @Ignore
//    val images: Array<Bitmap?> = arrayOf(image0, image1, image2, image3, image4, image5, image6, image7, image8)

    var images = Hashtable<String, Bitmap>()

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readInt()
        idClass = _in.readInt()
        note = _in.readString()

        var dataConverter = DataConverter()
        time = dataConverter.long2Date(_in.readLong())

//        val size = _in.readInt()
//        if (size > 0) {
//            for (i in 0 until size) {
//                val length = _in.readInt()
//                if (length > 0) {
//                    val icons = ByteArray(length)
//                    _in.readByteArray(icons)
//                    images[i] = dataConverter.toBitmap(icons)
//                }
//            }
//        }
        images = dataConverter.toHashtable(_in.readString())
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(idClass)
        dest.writeString(note)

        var dataConverter = DataConverter()
        dest.writeLong(dataConverter.date2Long(time))

//        dest.writeInt(images.size)
//        for (i in images.indices) {
//            if (images[i] == null) {
//                dest.writeInt(0)
//            } else {
//                val image = dataConverter.toByteArray(images[i]!!)
//                if (image != null) {
//                    dest.writeInt(image.size)
//                    dest.writeByteArray(image)
//                }
//            }
//        }
        dest.writeString(dataConverter.toString(images))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<Sign> = object : Parcelable.Creator<Sign> {
            override fun createFromParcel(_in: Parcel): Sign {
                return Sign(_in)
            }

            override fun newArray(size: Int): Array<Sign?> {
                return arrayOfNulls(size)
            }
        }
    }
}