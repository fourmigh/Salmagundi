package org.caojun.rotaryphone.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.rotaryphone.R
import org.caojun.rotaryphone.widget.RotaryView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rotaryView.setOnRotaryListener(object : RotaryView.OnRotaryListener {
            override fun onDial(number: String) {
                val text = tvNumbers.text.toString() + number
                tvNumbers.setText(text)
            }

            override fun onRotating() {
                KLog.d("OnRotaryListener", "onRotating")
            }

            override fun onDialing() {
                KLog.d("OnRotaryListener", "onDialing")
            }

            override fun onStopDialing() {
                KLog.d("OnRotaryListener", "onStopDialing")
            }
        })
    }
}
