package org.caojun.ttschulte.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.dialog_ask_name.view.*
import kotlinx.android.synthetic.main.layout_type.*
import org.caojun.ttschulte.Constant
import org.caojun.ttschulte.R
import org.caojun.ttschulte.utils.Schulte
import org.caojun.utils.DataStorageUtils
import org.caojun.utils.RandomUtils
import org.jetbrains.anko.*

/**
 * Created by CaoJun on 2018-1-11.
 */
class GameActivity : AppCompatActivity() {

    private val RequestCode_Game = 1
    private val RequestCode_ScoreList = 2
    private var ChineseIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        btnPlay.setOnClickListener {
            val layout = rgLayout.indexOfChild(rgLayout.findViewById(rgLayout.checkedRadioButtonId))
            val type = rgType.indexOfChild(rgType.findViewById(rgType.checkedRadioButtonId))
            val layoutName = (rgLayout.getChildAt(layout) as RadioButton).text.toString()
            val typeName = (rgType.getChildAt(type) as RadioButton).text.toString()
            startActivityForResult<SchulteActivity>(RequestCode_Game, Constant.Key_Layout to layout, Constant.Key_Type to type, Constant.Key_LayoutName to layoutName, Constant.Key_TypeName to typeName, Constant.Key_Chinese to ChineseIndex)
        }

        btnMyScore.setOnClickListener {
            gotoScoreList(true)
        }

        btnOnlineScore.setOnClickListener {
            gotoScoreList(false)
        }

        rgType.setOnCheckedChangeListener { _, _ ->
            val type = rgType.indexOfChild(rgType.findViewById(rgType.checkedRadioButtonId))
            if (type == Schulte.Type_Chinese) {
                initChinese()
            } else {
                tvChinese.visibility = View.GONE
            }
        }

        rgLayout.setOnCheckedChangeListener { _, _ ->
            val type = rgType.indexOfChild(rgType.findViewById(rgType.checkedRadioButtonId))
            if (type == Schulte.Type_Chinese) {
                initChinese()
            }
        }

        tvChinese.setOnClickListener {
            initChinese()
        }
    }

    private fun initChinese() {
        tvChinese.visibility = View.VISIBLE
        val layout = rgLayout.indexOfChild(rgLayout.findViewById(rgLayout.checkedRadioButtonId))
        val strings = resources.getStringArray(Constant.ChineseArrays[layout])
        ChineseIndex = RandomUtils.getRandom(0, strings.size - 1)
        tvChinese.text = strings[ChineseIndex]
    }

    private fun gotoScoreList(isLocal: Boolean) {
        val layout = rgLayout.indexOfChild(rgLayout.findViewById(rgLayout.checkedRadioButtonId))
        val type = rgType.indexOfChild(rgType.findViewById(rgType.checkedRadioButtonId))
        val title = if (isLocal) getString(R.string.my_score) else getString(R.string.online_score)
        startActivityForResult<ScoreListActivity>(RequestCode_ScoreList, Constant.Key_Layout to layout, Constant.Key_Type to type, Constant.Key_IsLocal to isLocal, Constant.Key_Title to title)
    }

    private fun doAskName() {
        var name = DataStorageUtils.loadString(this, Constant.Key_MyName, "")
        if (!TextUtils.isEmpty(name)) {
            this.title = getString(R.string.app_title, name)
            return
        }
        name = getString(R.string.my_name)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_ask_name, null)
        @Suppress("DEPRECATION")
        view.tvName.text = Html.fromHtml(getString(R.string.ask_name, name))
        alert {
            customView = view
            positiveButton(android.R.string.ok, {
                doSaveMyName(view.etName.text.toString())
            })
            isCancelable = false
        }.show()
    }

    private fun doSaveMyName(name: String) {
        var nickname = name
        if (TextUtils.isEmpty(name)) {
            nickname = getString(R.string.my_name)
        }
        DataStorageUtils.saveString(this, Constant.Key_MyName, nickname)
        this.title = getString(R.string.app_title, nickname)
    }

    override fun onPause() {
        super.onPause()
        val layout = rgLayout.indexOfChild(rgLayout.findViewById(rgLayout.checkedRadioButtonId))
        val type = rgType.indexOfChild(rgType.findViewById(rgType.checkedRadioButtonId))
        DataStorageUtils.saveInt(this, Constant.Key_Layout, layout)
        DataStorageUtils.saveInt(this, Constant.Key_Type, type)
    }

    override fun onResume() {
        super.onResume()
        val layout = DataStorageUtils.loadInt(this, Constant.Key_Layout, Schulte.Layout_9)
        val type = DataStorageUtils.loadInt(this, Constant.Key_Type, Schulte.Type_Natural)
        (rgLayout.getChildAt(layout) as RadioButton).isChecked = true
        (rgType.getChildAt(type) as RadioButton).isChecked = true
        doAskName()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity<SettingsActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}