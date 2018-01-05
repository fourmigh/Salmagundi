package org.caojun.ttshulte.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.caojun.ttshulte.R
import android.view.ViewGroup
import android.widget.Button
import com.socks.library.KLog
import org.caojun.ttshulte.utils.ViewUtils
import java.util.*


class ShulteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_shulte)
        setContentView(R.layout.layout_shulte_9)

        val root = (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0) as ViewGroup
        val buttons = ArrayList<Button>()
        ViewUtils.findButtons(root, buttons)
//        for (i in 0 until buttons.size) {
//            buttons[i].text = (i + 1).toString()
//        }
        Collections.shuffle(buttons)
        for (i in 0 until buttons.size) {
            buttons[i].text = (i + 1).toString()
        }
    }
}
