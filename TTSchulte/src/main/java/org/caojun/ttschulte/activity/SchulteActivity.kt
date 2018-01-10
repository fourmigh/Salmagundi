package org.caojun.ttschulte.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.layout_schulte_9.*
import kotlinx.android.synthetic.main.layout_schulte_16.*
import kotlinx.android.synthetic.main.layout_schulte_25.*
import kotlinx.android.synthetic.main.layout_schulte_36.*
import org.caojun.ttschulte.R
import org.caojun.ttschulte.utils.Schulte
import org.caojun.ttschulte.utils.ViewUtils
import java.util.*
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_schulte.*
import org.caojun.library.activity.CountdownActivity
import org.jetbrains.anko.startActivityForResult

class SchulteActivity : AppCompatActivity() {

    private val RequestCode_Countdown = 1

//    private val LayoutIds = arrayOf(R.layout.layout_schulte_9, R.layout.layout_schulte_16, R.layout.layout_schulte_25, R.layout.layout_schulte_36)
    private var LayoutIndex = Schulte.Layout_9
    private var indexButton = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(LayoutIds[LayoutIndex])
        setContentView(R.layout.activity_schulte)

        initLayoutSchulte()

        val root = getRoot()

        val buttons = ArrayList<Button>()
        ViewUtils.findButtons(root, buttons)

        Collections.shuffle(buttons)
        val chars = Schulte.getChars(LayoutIndex, Schulte.Type_Natural)
//        val chars = Schulte.getChars(LayoutIndex, Schulte.Type_Square)
//        val chars = Schulte.getChars(LayoutIndex, Schulte.Type_Cubic)
//        val chars = Schulte.getChars(LayoutIndex, Schulte.Type_Odd)
//        val chars = Schulte.getChars(LayoutIndex, Schulte.Type_Even)
//        val chars = Schulte.getChars(LayoutIndex, Schulte.Type_Lowercase)
//        val chars = Schulte.getChars(LayoutIndex, Schulte.Type_Uppercase)
//        val chars = Schulte.getChars(LayoutIndex, Schulte.Type_Alphabet)
        for (i in 0 until chars.size) {
            buttons[i].tag = i.toString()
            buttons[i].text = chars[i]
            buttons[i].setOnClickListener ({ v ->
                val tag = v.tag.toString().toInt()
                if (tag == indexButton) {
                    v.visibility = View.INVISIBLE
                    indexButton ++
                } else {
                    startAnimation(v)
                }
            })
        }

        button.setOnClickListener {
            startActivityForResult<CountdownActivity>(RequestCode_Countdown, CountdownActivity.Key_Time to 10)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCode_Countdown && resultCode == Activity.RESULT_OK) {
            //TODO 开始计时
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startAnimation(v: View) {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)//加载动画资源文件
        v.startAnimation(shake) //给组件播放动画效果
    }

    private fun initLayoutSchulte() {
        val layouts = arrayOf(tlSchulte9, tlSchulte16, tlSchulte25, tlSchulte36)
        for (i in layouts.indices) {
            if (i == LayoutIndex) {
                layouts[i].visibility = View.VISIBLE
            } else {
                layouts[i].visibility = View.GONE
            }
        }
    }

    private fun getRoot(): ViewGroup {
        return when (LayoutIndex) {
            Schulte.Layout_9 -> tlSchulte9
            Schulte.Layout_16 -> tlSchulte16
            Schulte.Layout_25 -> tlSchulte25
            else -> tlSchulte36
        }
    }
}
