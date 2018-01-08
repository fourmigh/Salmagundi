package org.caojun.ttshulte.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import org.caojun.ttshulte.R
import android.widget.Button
import kotlinx.android.synthetic.main.layout_shulte_9.*
import kotlinx.android.synthetic.main.layout_shulte_16.*
import kotlinx.android.synthetic.main.layout_shulte_25.*
import kotlinx.android.synthetic.main.layout_shulte_36.*
import org.caojun.ttshulte.utils.ViewUtils
import java.util.*


class ShulteActivity : AppCompatActivity() {

    private val LayoutIds = arrayOf(R.layout.layout_shulte_9, R.layout.layout_shulte_16, R.layout.layout_shulte_25, R.layout.layout_shulte_36)
    private var LayoutIndex = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LayoutIds[LayoutIndex])

        val root = getRoot()

        val buttons = ArrayList<Button>()
        ViewUtils.findButtons(root, buttons)

        Collections.shuffle(buttons)
        for (i in 0 until buttons.size) {
            buttons[i].text = (i + 1).toString()
        }
    }

    private fun getRoot(): ViewGroup {
        when (LayoutIndex) {
            0 -> return tlShulte9
            1 -> return tlShulte16
            2 -> return tlShulte25
            else -> return tlShulte36
        }
    }
}
