package org.caojun.decidophobia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_choices.*
import org.caojun.decidophobia.R
import org.caojun.decidophobia.ormlite.Options
import org.caojun.decidophobia.utils.OptionsUtils
import org.caojun.library.Constant
import org.caojun.library.activity.DiceActivity
import org.caojun.library.utils.RandomUtils

class ChoicesActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private val MinNumber = 2//最小选项数
    private val ResIdTableRow = intArrayOf(R.id.trOption1, R.id.trOption2, R.id.trOption3, R.id.trOption4, R.id.trOption5, R.id.trOption6)
    private val ResIdEditText = intArrayOf(R.id.etOption1, R.id.etOption2, R.id.etOption3, R.id.etOption4, R.id.etOption5, R.id.etOption6)
    private val ResIdExample = intArrayOf(R.array.example2, R.array.example3, R.array.example4)

//    private var etTitle: EditText? = null
//    private var seekBar: SeekBar? = null
//    private var btnRandom: Button? = null
    private val etOption = arrayListOf<EditText>()
    private val trOption = arrayListOf<TableRow>()
    private var menu: Menu? = null

    private var defaultTextSize: Float = 0f;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choices)

//        etTitle = findViewById(R.id.etTitle)
        etTitle?.addTextChangedListener(textWatcher)

        defaultTextSize = etTitle?.textSize?:0f

        for (i in ResIdTableRow.indices) {
            trOption.add(findViewById(ResIdTableRow[i]))
        }

        for (i in ResIdEditText.indices) {
            var editText: EditText = findViewById(ResIdEditText[i])
            editText.addTextChangedListener(textWatcher)
            etOption.add(editText)
        }

//        btnRandom = findViewById(R.id.btnRandom)
        btnRandom?.setOnClickListener {
            doRandom()
        }

//        seekBar = findViewById(R.id.seekBar)
        seekBar?.setOnSeekBarChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        var list = OptionsUtils.query(this)
        var size = list?.size?:0
        if (size == 0) {
            var index = RandomUtils.getRandom(0, ResIdExample.size - 1)
            var example = resources.getStringArray(ResIdExample[index])
            seekBar?.progress = example!!.size - 1 - MinNumber
            etTitle?.setText(example[0])
            for (i in 0..(seekBar!!.progress + 1)) {
                etOption[i].setText(example[i + 1])
            }
        } else {
            //最后一个记录赋值
            var record = list?.get(size - 1)
            initOptions(record!!)
        }

        setSelection(etTitle!!)

        doCheckTableRow(seekBar!!.progress)
        doCheckRandomButton()
    }

    private fun initOptions(record: Options) {
        seekBar?.progress = record.option.size - MinNumber
        etTitle?.setText(record.title)
        for (i in record.option.indices) {
            etOption[i].setText(record?.option[i])
        }
    }

    private fun setSelection(editText: EditText) {
        var text = editText.text.toString()
        editText.setSelection(text.length)
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
            R.id.action_history -> {
                selectHistory()
                true
            }
            R.id.action_details -> {
                val intent = Intent(this, ChoicesListActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        doCheckMenu()
        return super.onPrepareOptionsMenu(menu)
    }

    private fun doCheckMenu() {
        for (i in 0..(menu!!.size()-1)) {
//            if (menu?.getItem(i)?.itemId == R.id.action_history) {
                var list = OptionsUtils.query(this)
                var size = list?.size?:0
                menu?.setGroupVisible(i, size > 0)
//            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        doCheckTableRow(progress)
        doCheckRandomButton()
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
        resizeOptions(-1)

        val size = seekBar!!.progress + MinNumber
        if (!OptionsUtils.insert(this, etTitle!!, etOption, size)) {
            OptionsUtils.update(this, etTitle!!, etOption, size)
        }
        doDice()
    }

    private fun doDice() {
        val max = seekBar!!.progress + MinNumber
        val intent = Intent(this, DiceActivity::class.java)
        intent.putExtra(Constant.Key_MaxDice, max)
        startActivityForResult(intent, Constant.RequestCode_Dice)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.RequestCode_Dice && resultCode == Activity.RESULT_OK && data != null) {
            var dice = data.getIntArrayExtra(Constant.Key_Dice)
            val index = dice[0] - 1;
            etOption[index].requestFocus()
            resizeOptions(index)
            setSelection(etOption[dice[0] - 1])
            doCheckMenu()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun selectHistory() {

        var options = OptionsUtils.query(this)
        var items = OptionsUtils.query(options)

        AlertDialog.Builder(this)
                .setTitle(R.string.history)
                .setItems(items) { _, which ->
                    initOptions(options!![which])
                    resizeOptions(-1)
                    etTitle?.requestFocus()
                    setSelection(etTitle!!)
                }
                .create().show()
    }

    private fun resizeOptions(index: Int) {
        if (defaultTextSize <= 0) {
            return
        }
        for (i in etOption.indices) {
            if (index < 0) {
                etOption[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize)
            } else if (i == index) {
                etOption[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize * 2f)
            } else {
                etOption[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize * 0.5f)
            }
        }
    }
}
