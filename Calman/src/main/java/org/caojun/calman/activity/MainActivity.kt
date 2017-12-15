package org.caojun.calman.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_calculator.*
import org.caojun.calman.R
import org.caojun.calman.room.Level
import org.caojun.calman.room.LevelDatabase
import org.caojun.calman.utils.GameUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class MainActivity : BaseActivity() {

    private var string = ""
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        setContentView(R.layout.activity_calculator)

//        btn00.setOnClickListener {
//            string += "" + index++
//            btn01.setText(string)
//            tvGoal.setText(string)
//            tvMoves.setText(string)
//            tvNumber.setText(string)
//        }
        GameUtils.initialize()
        doAsync {
            GameUtils.newLevel(this@MainActivity)
            uiThread {
                GameUtils.initButtons(this@MainActivity)
            }
        }

        startActivity<TestActivity>()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCButtonClicked(level: Level) {
        tvMoves.text = getString(R.string.moves, level.moves.toString())
        if (GameUtils.status == GameUtils.Status.Error) {
            tvNumber.setText(R.string.error)
        } else {
            tvNumber.text = level.currentValue.toString()
        }
    }

    override fun onGameInited(level: Level) {
        tvMoves.text = getString(R.string.moves, level.moves.toString())
        tvGoal.text = getString(R.string.goal, level.goal.toString())
        tvNumber.text = level.initial.toString()
    }

    override fun onGameRestart(level: Level) {
        doAsync {
            val list = LevelDatabase.getDatabase(this@MainActivity).getDao().queryAll()
            for (i in 0 until list.size) if (list[i].level == level.level) {
                level.initial = list[i].initial
                level.goal = list[i].goal
                level.moves = list[i].moves
                level.currentHints = list[i].currentHints
                level.currentValue = list[i].currentValue
                level.currentStep = list[i].currentStep
                break
            }
        }
    }
}
