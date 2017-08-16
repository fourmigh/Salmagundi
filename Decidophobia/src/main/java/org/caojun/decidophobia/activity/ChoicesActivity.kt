package org.caojun.decidophobia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TableRow
import com.socks.library.KLog
import org.caojun.decidophobia.R
import org.caojun.decidophobia.ormlite.Options
import org.caojun.decidophobia.utils.OptionsUtils
import org.caojun.library.Constant
import org.caojun.library.activity.DiceActivity

class ChoicesActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    val MinNumber = 2//最小选项数
    val ResIdGif = intArrayOf(R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    val ResIdTableRow = intArrayOf(R.id.trOption1, R.id.trOption2, R.id.trOption3, R.id.trOption4, R.id.trOption5, R.id.trOption6)
    val ResIdEditText = intArrayOf(R.id.etOption1, R.id.etOption2, R.id.etOption3, R.id.etOption4, R.id.etOption5, R.id.etOption6)

    var etTitle: EditText? = null
    var seekBar: SeekBar? = null
    var btnRandom: Button? = null
    val etOption = arrayListOf<EditText>()
    val trOption = arrayListOf<TableRow>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choices)
//        setSupportActionBar(toolbar)
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        etTitle = findViewById(R.id.etTitle)
        etTitle?.addTextChangedListener(textWatcher)

        for (i in ResIdTableRow.indices) {
            trOption.add(findViewById(ResIdTableRow[i]))
        }

        for (i in ResIdEditText.indices) {
            var editText: EditText = findViewById(ResIdEditText[i])
            editText.addTextChangedListener(textWatcher)
            KLog.d("etOption", i.toString())
            etOption.add(editText)
        }

        btnRandom = findViewById(R.id.btnRandom)
        btnRandom?.setOnClickListener {
            doRandom()
        }

        seekBar = findViewById(R.id.seekBar)
        seekBar?.setOnSeekBarChangeListener(this)
        seekBar?.progress = 0

    }

    override fun onResume() {
        super.onResume()
        var list = OptionsUtils.query(this)
        var size = list?.size?:0
        if (size == 0) {
            var example = resources.getStringArray(R.array.option_example)
            seekBar?.progress = example!!.size - 1 - MinNumber
            etTitle?.setText(example[0])
            for (i in 0..(seekBar!!.progress + 1)) {
                etOption[i].setText(example[i + 1])
            }
        } else {
            //最后一个记录赋值
            var record = list?.get(size - 1)
            seekBar?.progress = record?.option!!.size - MinNumber
            etTitle?.setText(record.title)
            for (i in 0..(record?.option!!.size - 1)) {
                etOption[i].setText(record?.option[i])
            }
        }

        doCheckTableRow(0)
        doCheckRandomButton()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_choices, menu)
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

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        doCheckTableRow(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    private fun doCheckTableRow(progress: Int) {
        for (i in ResIdTableRow.indices) {
            if (i < progress + 2) {
                trOption[i].visibility = View.VISIBLE
            } else {
                trOption[i].visibility = View.GONE
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            doCheckRandomButton()
        }
    }

    private fun doCheckRandomButton() {
        btnRandom?.isEnabled = false
        var title = etTitle?.text.toString()
        if (TextUtils.isEmpty(title)) {
            return
        }
        for (i in ResIdEditText.indices) {
            if (i >= seekBar!!.progress + MinNumber) {
                break;
            }
            var text = etOption[i].text.toString()
            if (TextUtils.isEmpty(text)) {
                return
            }
        }
        btnRandom?.isEnabled = true
    }

    private fun doRandom() {
        val size = seekBar!!.progress + MinNumber
        if (OptionsUtils.insert(this, etTitle!!, etOption, size)) {
            KLog.d("doRandom", "insert ok")
            doDice(1)
        } else {
            KLog.d("doRandom", "insert failed")
            //TODO 提示有重复记录，经确认后再覆盖
            if (OptionsUtils.update(this, etTitle!!, etOption, size)) {
                KLog.d("doRandom", "update ok")
                doDice(1)
            } else {
                KLog.d("doRandom", "update failed")
            }
        }

    }

    private fun doDice(number:Int) {
        val intent = Intent(this, DiceActivity::class.java)
        intent.putExtra(Constant.Key_Number, number)
        startActivityForResult(intent, Constant.RequestCode_Dice)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.RequestCode_Dice && resultCode == Activity.RESULT_OK && data != null) {
            var dice = data.getIntArrayExtra(Constant.Key_Dice)
            for (i in ResIdTableRow.indices) {
                trOption[i].isEnabled = (dice[0] - 1 == i)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
