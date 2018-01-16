package org.caojun.ttschulte.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.layout_type.*
import org.caojun.ttschulte.Constant
import org.caojun.ttschulte.R
import org.jetbrains.anko.startActivityForResult
import java.util.*

/**
 * Created by CaoJun on 2018-1-11.
 */
class GameActivity : AppCompatActivity() {

    private val RequestCode_Game = 1
    private val RequestCode_ScoreList = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        rgLayout.check(rbLayout9.id)
        rgType.check(rbNatural.id)

        btnPlay.setOnClickListener {
            val layout = rgLayout.indexOfChild(rgLayout.findViewById(rgLayout.checkedRadioButtonId))
            val type = rgType.indexOfChild(rgType.findViewById(rgType.checkedRadioButtonId))
            val layoutName = (rgLayout.getChildAt(layout) as RadioButton).text.toString()
            val typeName = (rgType.getChildAt(type) as RadioButton).text.toString()
            startActivityForResult<SchulteActivity>(RequestCode_Game, Constant.Key_Layout to layout, Constant.Key_Type to type, Constant.Key_LayoutName to layoutName, Constant.Key_TypeName to typeName)
        }

        btnMyScore.setOnClickListener {
            gotoScoreList(true)
        }

        btnOnlineScore.setOnClickListener {
            gotoScoreList(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCode_Game && resultCode == Activity.RESULT_OK && data != null) {
            val layout = intent.getIntExtra(Constant.Key_Layout, rgLayout.indexOfChild(rgLayout.findViewById(rgLayout.checkedRadioButtonId)))
            val type = intent.getIntExtra(Constant.Key_Type, rgType.indexOfChild(rgType.findViewById(rgType.checkedRadioButtonId)))
            val nickname = intent.getStringExtra(Constant.Key_Nickname)
            val score = intent.getFloatExtra(Constant.Key_Score, 99.99f)
            val time = intent.getLongExtra(Constant.Key_Time, Date().time)
            //上传成绩
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun gotoScoreList(isLocal: Boolean) {
        val layout = rgLayout.indexOfChild(rgLayout.findViewById(rgLayout.checkedRadioButtonId))
        val type = rgType.indexOfChild(rgType.findViewById(rgType.checkedRadioButtonId))
        val title = if (isLocal) getString(R.string.my_score) else getString(R.string.online_score)
        startActivityForResult<ScoreListActivity>(RequestCode_ScoreList, Constant.Key_Layout to layout, Constant.Key_Type to type, Constant.Key_IsLocal to isLocal, Constant.Key_Title to title)
    }
}