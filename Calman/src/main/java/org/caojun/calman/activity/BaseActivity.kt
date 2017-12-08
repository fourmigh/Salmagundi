package org.caojun.calman.activity

import android.support.v7.app.AppCompatActivity
import org.caojun.calman.listener.OnGameListener
import org.caojun.calman.room.Level

/**
 * Created by CaoJun on 2017/11/20.
 */
open class BaseActivity: AppCompatActivity(), OnGameListener {
    override fun onCButtonClicked(level: Level) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGameInited(level: Level) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGameRestart(level: Level) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}