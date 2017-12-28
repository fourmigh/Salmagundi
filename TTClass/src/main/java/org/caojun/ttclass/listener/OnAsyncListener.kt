package org.caojun.ttclass.listener

import org.caojun.ttclass.room.Sign

/**
 * Created by CaoJun on 2017-12-28.
 */
interface OnAsyncListener {
    fun onFinish(list: List<Sign>)
}