package org.caojun.ttschulte.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
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
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import kotlinx.android.synthetic.main.activity_schulte.*
import org.caojun.library.activity.CountdownActivity
import org.caojun.ttschulte.Constant
import org.caojun.ttschulte.room.Score
import org.caojun.ttschulte.room.ScoreBmob
import org.caojun.ttschulte.room.TTSDatabase
import org.caojun.utils.DataStorageUtils
import org.caojun.utils.DeviceUtils
import org.caojun.utils.DigitUtils
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class SchulteActivity : AppCompatActivity() {

    private val RequestCode_Countdown = 1

    private var LayoutIndex = Schulte.Layout_9
    private var LayoutName = ""
    private var TypeIndex = Schulte.Type_Natural
    private var TypeName = ""
    private var indexButton = 0
    private val buttons = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schulte)

        var name = DataStorageUtils.loadString(this, Constant.Key_MyName, "")
        if (!TextUtils.isEmpty(name)) {
            this.title = getString(R.string.app_title, name)
        }

        LayoutIndex = intent.getIntExtra(Constant.Key_Layout, Schulte.Layout_9)
        TypeIndex = intent.getIntExtra(Constant.Key_Type, Schulte.Type_Natural)
        LayoutName = intent.getStringExtra(Constant.Key_LayoutName)
        TypeName = intent.getStringExtra(Constant.Key_TypeName)

        if (LayoutIndex < Schulte.Layout_9) {
            LayoutIndex = Schulte.Layout_9
            LayoutName = getString(R.string.layout9)
        }
        if (TypeIndex < Schulte.Type_Natural) {
            TypeIndex = Schulte.Type_Natural
            TypeName = getString(R.string.natural)
        }

        initLayoutSchulte()
        gameStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCode_Countdown && resultCode == Activity.RESULT_OK) {
            //开始计时
            stopwatch.start()
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

    private fun isGameWin(): Boolean {
        return (0 until buttons.size).none { buttons[it].visibility != View.INVISIBLE }
    }

    private fun gameStart() {
        buttons.clear()
        indexButton = 0
        val root = getRoot()
        ViewUtils.findButtons(root, buttons)
        stopwatch.reset()
        Collections.shuffle(buttons)
        val chars = Schulte.getChars(LayoutIndex, TypeIndex)
        for (i in 0 until chars.size) {
            buttons[i].isEnabled = true
            buttons[i].tag = i.toString()
            buttons[i].text = chars[i]
            buttons[i].visibility = View.VISIBLE
            buttons[i].setOnClickListener ({ v ->
                val tag = v.tag.toString().toInt()
                if (tag == indexButton) {
                    v.visibility = View.INVISIBLE
                    indexButton ++

                    if (isGameWin()) {
                        //游戏完成
                        doGameWin()
                    }
                } else {
                    startAnimation(v)
                }
            })
        }

        val time = DataStorageUtils.loadInt(this, Constant.Key_Countdown_Time, 3)
        startActivityForResult<CountdownActivity>(RequestCode_Countdown, CountdownActivity.Key_Time to time)
    }

    private fun doGameWin() {
        stopwatch.stop()
        val time = Date().time
        val score = stopwatch.getScore()
        val name = DataStorageUtils.loadString(this, Constant.Key_MyName, getString(R.string.my_name))
        doSaveScore(name, score, time)

        val info = getString(R.string.game_win_info, DigitUtils.getRound(score, 2), LayoutName, TypeName)
        @Suppress("DEPRECATION")
        val msg = Html.fromHtml(info)
        alert {
            message = msg
            positiveButton(R.string.game_win_again, {
                gameStart()
            })
            negativeButton(R.string.game_win_upload, {
                doUploadScore()
            })
            neutralPressed(R.string.game_win_quit, {
                finish()
            })
            isCancelable = false
        }.show()
    }

    private fun doUploadScore() {

        doAsync {
            val list = TTSDatabase.getDatabase(this@SchulteActivity).getScore().queryAll(LayoutIndex, TypeIndex)
            val s = list[list.size - 1]
            val sb = ScoreBmob(s, DeviceUtils.getImei(this@SchulteActivity))
            sb.save(object : SaveListener<String>() {

                override fun done(o: String, e: BmobException?) {
                    if (e == null) {
                        toast(R.string.upload_ok)
                    } else {
                        toast(getString(R.string.upload_error, e.toString()))
                    }
                }
            })

            finish()
        }
    }

    private fun doSaveScore(name: String, score: Float, time: Long) {
        doAsync {
            val s = Score()
            s.layout = LayoutIndex
            s.type = TypeIndex
            s.name = name
            s.score = score
            s.time = time
            TTSDatabase.getDatabase(this@SchulteActivity).getScore().insert(s)
        }
    }
}
