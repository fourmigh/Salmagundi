package org.caojun.decibelman.activity

import android.app.Activity
import android.preference.PreferenceManager
import org.caojun.decibelman.Constant
import org.caojun.decibelman.R
import org.caojun.decibelman.room.DecibelInfo
import org.caojun.decibelman.room.DecibelInfoDatabase
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import java.util.*

/**
 * Created by CaoJun on 2017/9/25.
 */
open class BaseActivity: Activity() {

    private val SP_CONFIRM_DIALOG = "sp_confirm_dialog"

    interface OnDatabaseListener {
        fun onSuccess()
        fun onError()
    }

    private var onDatabaseListener: OnDatabaseListener? = null
    fun setOnDatabaseListener(onDatabaseListener: OnDatabaseListener) {
        this.onDatabaseListener = onDatabaseListener
    }

    fun getDecibelInfos(): List<DecibelInfo> {
        return DecibelInfoDatabase.getDatabase(this).getDao().queryAll()
    }

    fun alertSaveDecibelInfo() {
        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        if (settings.getBoolean(SP_CONFIRM_DIALOG, true)) {
            alert(R.string.alert_save_data) {
                positiveButton(R.string.yes) {
                    saveDecibelInfo()
                }
                negativeButton(R.string.no) {
                    onDatabaseListener?.onSuccess()
                }
                neutralPressed(R.string.yes_no_alert, {
                    saveDecibelInfo()
                    saveSharedPreferences()
                })
            }.show()
        } else {
            saveDecibelInfo()
        }
    }

    private fun saveDecibelInfo() {
        doAsync {
            var di = DecibelInfo()
            di.latitude = Constant.latitude
            di.longitude = Constant.longitude
            di.imei = Constant.decibelInfo0!!.imei
            di.random_id = Constant.decibelInfo0!!.random_id
            di.database_time = Constant.decibelInfo0!!.database_time
            di.time = Date().time
            di.decibel_min = Constant.min
            di.decibel_max = Constant.max
            di.decibel_average = Constant.average
            DecibelInfoDatabase.getDatabase(this@BaseActivity).getDao().insert(di)
            onDatabaseListener?.onSuccess()
        }
    }

    private fun saveSharedPreferences() {
        doAsync {
            val settings = PreferenceManager.getDefaultSharedPreferences(this@BaseActivity).edit()
            settings.putBoolean(SP_CONFIRM_DIALOG, false)
            settings.commit()
        }
    }
}