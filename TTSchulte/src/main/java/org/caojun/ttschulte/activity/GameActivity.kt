package org.caojun.ttschulte.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.RadioButton
import android.widget.RadioGroup
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_game.*
import org.caojun.ttschulte.R
import org.jetbrains.anko.startActivityForResult

/**
 * Created by CaoJun on 2018-1-11.
 */
class GameActivity : AppCompatActivity() {

    private val RequestCode_Game = 1

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
            KLog.d("btnPlay", "layout: " + layout)
            KLog.d("btnPlay", "layoutName: " + layoutName)
            KLog.d("btnPlay", "type: " + type)
            KLog.d("btnPlay", "typeName: " + typeName)
            startActivityForResult<SchulteActivity>(RequestCode_Game, SchulteActivity.Key_Layout to layout, SchulteActivity.Key_Type to type, SchulteActivity.Key_LayoutName to layoutName, SchulteActivity.Key_TypeName to typeName)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCode_Game && resultCode == Activity.RESULT_OK && data != null) {
            //TODO
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}