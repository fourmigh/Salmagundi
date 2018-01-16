package org.caojun.ttschulte.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_score_list.*
import kotlinx.android.synthetic.main.layout_type.*
import org.caojun.ttschulte.Constant
import org.caojun.ttschulte.R
import org.caojun.ttschulte.adapter.ScoreAdapter
import org.caojun.ttschulte.room.TTSDatabase
import org.caojun.ttschulte.utils.Schulte
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2018-1-16.
 */
class ScoreListActivity : AppCompatActivity() {

    private var layout = 0
    private var type = 0
    private var isLocal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_list)

        this.title = intent.getStringExtra(Constant.Key_Title)

        layout = intent.getIntExtra(Constant.Key_Layout, Schulte.Layout_9)
        type = intent.getIntExtra(Constant.Key_Type, Schulte.Type_Natural)
        isLocal = intent.getBooleanExtra(Constant.Key_IsLocal, true)
        readData()

        rgLayout.setOnCheckedChangeListener { radioGroup, i ->
            layout = radioGroup.indexOfChild(radioGroup.findViewById(i))
            readData()
        }

        rgType.setOnCheckedChangeListener { radioGroup, i ->
            type = radioGroup.indexOfChild(radioGroup.findViewById(i))
            readData()
        }
    }

    private fun readData() {
        (rgLayout.getChildAt(layout) as RadioButton).isChecked = true
        (rgType.getChildAt(type) as RadioButton).isChecked = true

        doAsync {
            val list = TTSDatabase.getDatabase(this@ScoreListActivity).getScore().query(layout, type)
            uiThread {
                listView.adapter = ScoreAdapter(this@ScoreListActivity, list)
            }
        }
    }
}