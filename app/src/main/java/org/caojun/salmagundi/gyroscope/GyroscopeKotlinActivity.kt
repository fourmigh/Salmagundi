package org.caojun.salmagundi.gyroscope

import android.app.Activity
import android.os.Bundle
import org.caojun.salmagundi.R

/**
 * Created by CaoJun on 2017/9/28.
 */
class GyroscopeKotlinActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyroscope)
    }
}