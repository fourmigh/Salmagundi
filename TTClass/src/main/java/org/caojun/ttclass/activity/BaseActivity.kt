package org.caojun.ttclass.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_confirm.*

/**
 * Created by CaoJun on 2017-12-12.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        btnCancel?.setOnClickListener {
            onBackPressed()
        }
    }
}