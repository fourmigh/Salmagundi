package org.caojun.ttshulte.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import org.caojun.ttschulte.R
import android.widget.Button
import kotlinx.android.synthetic.main.layout_schulte_9.*
import kotlinx.android.synthetic.main.layout_schulte_16.*
import kotlinx.android.synthetic.main.layout_schulte_25.*
import kotlinx.android.synthetic.main.layout_schulte_36.*
import org.caojun.ttshulte.utils.Schulte
import org.caojun.ttshulte.utils.ViewUtils
import java.util.*


class SchulteActivity : AppCompatActivity() {

    private val LayoutIds = arrayOf(R.layout.layout_schulte_9, R.layout.layout_schulte_16, R.layout.layout_schulte_25, R.layout.layout_schulte_36)
    private var LayoutIndex = Schulte.Layout_16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LayoutIds[LayoutIndex])

        val root = getRoot()

        val buttons = ArrayList<Button>()
        ViewUtils.findButtons(root, buttons)

        Collections.shuffle(buttons)
//        val chars = Shulte.getChars(LayoutIndex, Shulte.Type_Natural)
//        val chars = Shulte.getChars(LayoutIndex, Shulte.Type_Square)
//        val chars = Shulte.getChars(LayoutIndex, Shulte.Type_Cubic)
//        val chars = Shulte.getChars(LayoutIndex, Shulte.Type_Odd)
//        val chars = Shulte.getChars(LayoutIndex, Shulte.Type_Even)
//        val chars = Shulte.getChars(LayoutIndex, Shulte.Type_Lowercase)
//        val chars = Shulte.getChars(LayoutIndex, Shulte.Type_Uppercase)
        val chars = Schulte.getChars(LayoutIndex, Schulte.Type_Alphabet)
        for (i in 0 until chars.size) {
            buttons[i].text = chars[i]
        }
    }

    private fun getRoot(): ViewGroup {
        when (LayoutIndex) {
            Schulte.Layout_9 -> return tlShulte9
            Schulte.Layout_16 -> return tlShulte16
            Schulte.Layout_25 -> return tlShulte25
            else -> return tlShulte36
        }
    }
}