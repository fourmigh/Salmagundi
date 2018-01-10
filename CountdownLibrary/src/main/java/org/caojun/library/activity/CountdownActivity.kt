package org.caojun.library.activity

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_countdown.*
import org.caojun.library.R
import org.caojun.widget.CountdownMovie
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2018-1-10.
 */
class CountdownActivity : Activity() {

    companion object {
        val Key_Time = "Key_Time"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        val time = intent.getIntExtra(Key_Time, 10)
        countdownMovie.init(time)
        countdownMovie.setOnCountdownListener(object : CountdownMovie.OnCountdownListener {
            override fun finished() {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        setFinishOnTouchOutside(false)
    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        super.onResume()
        doAsync {
            Thread.sleep(500)
            uiThread {
                countdownMovie.start()
            }
        }
    }
}