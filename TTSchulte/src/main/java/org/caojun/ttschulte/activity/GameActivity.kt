package org.caojun.ttschulte.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

        btnPlay.setOnClickListener {
            val layout = rgLayout.indexOfChild(rgLayout.findViewById(rgLayout.checkedRadioButtonId))
            val type = rgType.indexOfChild(rgType.findViewById(rgType.checkedRadioButtonId))
            startActivityForResult<SchulteActivity>(RequestCode_Game, SchulteActivity.Key_Layout to layout, SchulteActivity.Key_Type to type)
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