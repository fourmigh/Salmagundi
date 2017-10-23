package org.caojun.decidophobia.ormlite

import java.io.Serializable
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Created by CaoJun on 2017/8/16.
 */
@DatabaseTable
class Options(): Serializable {

    @DatabaseField(id = true)
    var title: String = ""

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    var option = SerializedList<String>()

    constructor(title: String, option: SerializedList<String>) : this() {
        this.title = title
        this.option = option
    }
}