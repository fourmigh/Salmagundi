package org.caojun.calman.listener

import org.caojun.calman.room.Level

/**
 * Created by CaoJun on 2017/11/20.
 */
interface OnGameListener {
    fun onCButtonClicked(level: Level)
    fun onGameInited(level: Level)
    fun onGameRestart(level: Level)
}