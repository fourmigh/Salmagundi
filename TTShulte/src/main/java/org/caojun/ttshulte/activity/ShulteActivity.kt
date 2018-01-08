package org.caojun.ttshulte.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import org.caojun.ttshulte.R
import android.widget.Button
import kotlinx.android.synthetic.main.layout_shulte_9.*
import kotlinx.android.synthetic.main.layout_shulte_16.*
import kotlinx.android.synthetic.main.layout_shulte_25.*
import org.caojun.ttshulte.utils.ViewUtils
import java.util.*


class ShulteActivity : AppCompatActivity() {

    private val LayoutIds = arrayOf(R.layout.layout_shulte_9, R.layout.layout_shulte_16, R.layout.layout_shulte_25)
//    private val Roots = arrayOf(tlShulte9, tlShulte16, tlShulte25)
    private var LayoutIndex = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_shulte)
        setContentView(LayoutIds[LayoutIndex])

//        val root = (findViewById<ViewGroup>(R.id.tlShulte)).getChildAt(0) as ViewGroup
        val root = getRoot()
//        layoutParams.width = min
//        layoutParams.height = min

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

    private fun getRoot(): ViewGroup {
        when (LayoutIndex) {
            0 -> return tlShulte9
            1 -> return tlShulte16
            else -> return tlShulte25
        }
    }
}
