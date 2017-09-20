package org.caojun.decidophobia.utils

import android.content.Context
import android.widget.EditText
import org.caojun.decidophobia.ormlite.Options
import org.caojun.decidophobia.ormlite.OptionsDatabase
import org.caojun.decidophobia.ormlite.SerializedList

/**
 * Created by CaoJun on 2017/8/16.
 */
object OptionsUtils {

    fun insert(context: Context, etTitle: EditText, etOption: List<EditText>, size: Int): Boolean {
        var options = createOptions(etTitle, etOption, size)
        var database = OptionsDatabase.getInstance(context)
        return database.insert(options);
    }

    fun update(context: Context, etTitle: EditText, etOption: List<EditText>, size: Int): Boolean {
        var options = createOptions(etTitle, etOption, size)
        var database = OptionsDatabase.getInstance(context)
        return database.update(options);
    }

    fun query(context: Context): List<Options>? {
        var database = OptionsDatabase.getInstance(context)
        return database.query()
    }

    fun query(options: List<Options>?): Array<String> {
        var size = options?.size?:0
        var strings = Array(size, { index -> index.toString() })
        if (options != null) {
            for (i in options.indices) {
                strings[i] = (options[i].title) + "(" + options[i].option.size + ")"
            }
        }
        return strings
    }

    private fun createOptions(etTitle: EditText, etOption: List<EditText>, size: Int): Options {
        var title = etTitle.text.toString()
        var option = SerializedList<String>()
        for (i in 0..(size-1)) {
            option.add(etOption[i].text.toString())
        }
        return Options(title, option)
    }

    fun delete(context: Context, options: Options): Boolean {
        var database = OptionsDatabase.getInstance(context)
        return database.delete(options)
    }
}