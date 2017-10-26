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
import cn.bmob.v3.BmobQuery
import kotlinx.android.synthetic.main.activity_choices.*
import org.caojun.decidophobia.R
import org.caojun.decidophobia.bmob.BOptions
import org.caojun.decidophobia.ormlite.Options
import org.caojun.decidophobia.utils.OptionsUtils
import org.caojun.library.Constant
import org.caojun.library.activity.DiceActivity
import org.caojun.library.utils.RandomUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import rx.Subscriber
import org.jetbrains.anko.startActivityForResult

class ChoicesActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private val MinNumber = 2//最小选项数
    private val ResIdTableRow = intArrayOf(R.id.trOption1, R.id.trOption2, R.id.trOption3, R.id.trOption4, R.id.trOption5, R.id.trOption6)
    private val ResIdEditText = intArrayOf(R.id.etOption1, R.id.etOption2, R.id.etOption3, R.id.etOption4, R.id.etOption5, R.id.etOption6)
    private val ResIdExample = intArrayOf(R.array.example2, R.array.example3, R.array.example4)
    private val RequestCode_Cloud = 2
    private var isOnActivityResult = false

    private val etOption = arrayListOf<EditText>()
    private val trOption = arrayListOf<TableRow>()
    private var menu: Menu? = null

    private var defaultTextSize: Float = 0f;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choices)

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

        btnRandom?.setOnClickListener {
            doRandom()
        }

        seekBar?.setOnSeekBarChangeListener(this)

//        startActivity<CarouselActivity>()
    }

    override fun onResume() {
        super.onResume()
        if (isOnActivityResult) {
            isOnActivityResult = false
            return
        }
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
            etOption[i].setText(record.option[i])
        }
    }

    private fun setSelection(editText: EditText) {
        var text = editText.text.toString()
        editText.setSelection(text.length)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_choices, menu)

        menu.add(0, R.id.action_history, 0, R.string.history)
        menu.add(1, R.id.action_details, 1, R.string.details)
        menu.add(2, R.id.action_cloud, 2, R.string.cloud)
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
            R.id.action_cloud -> {
                startActivityForResult<CloudChoicesListActivity>(RequestCode_Cloud)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        readCloud()
        return super.onPrepareOptionsMenu(menu)
    }

    private fun doCheckMenu() {
        for (i in 0 until menu!!.size()) {
            if (menu?.getItem(i)?.itemId == R.id.action_cloud) {
                menu?.setGroupVisible(i, CloudChoicesListActivity.list.size > 0)
            } else {
                var list = OptionsUtils.query(this)
                var size = list?.size ?: 0
                menu?.setGroupVisible(i, size > 0)
            }
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
            isOnActivityResult = true
        } else if (requestCode == RequestCode_Cloud && resultCode == Activity.RESULT_OK && data != null) {
            val options = data.getSerializableExtra("options") as Options
            doSelectOptions(options)
            doCheckMenu()
            isOnActivityResult = true
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun selectHistory() {

        var options = OptionsUtils.query(this)
        var items = OptionsUtils.query(options)

        AlertDialog.Builder(this)
                .setTitle(R.string.history)
                .setItems(items) { _, which ->
                    doSelectOptions(options!![which])
                }
                .create().show()
    }

    private fun doSelectOptions(record: Options) {
        initOptions(record)
        resizeOptions(-1)
        etTitle?.requestFocus()
        setSelection(etTitle!!)
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

    private fun readCloud() {
        doAsync {
            val bmobQuery = BmobQuery<BOptions>()
//            val isCache = bmobQuery.hasCachedResult(BOptions::class.java)
//            if (isCache) {
//                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK)    // 先从缓存取数据，如果没有的话，再从网络取。
//            } else {
//                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE)    // 如果没有缓存的话，则先从网络中取
//            }
            bmobQuery.cachePolicy = BmobQuery.CachePolicy.NETWORK_ELSE_CACHE
            bmobQuery.findObjectsObservable(BOptions::class.java!!)
                    .subscribe(object : Subscriber<List<BOptions>>() {
                        override fun onCompleted() {
                            doCheckMenu()
                        }

                        override fun onError(e: Throwable) {
                            doCheckMenu()
                        }

                        override fun onNext(persons: List<BOptions>) {
                            CloudChoicesListActivity.list.clear()

                            val cache = ArrayList<String>()
                            for (i in 0 until persons.size) {
                                var sb = StringBuffer(persons[i].title)
                                for (j in 0 until persons[i].option.size) {
                                    sb.append(persons[i].option[j])
                                }
                                val person = sb.toString()
                                if (person in cache) {
                                    continue
                                }
                                CloudChoicesListActivity.list.add(persons[i])
                                cache.add(person)
                            }
                            doCheckMenu()
                        }
                    })
        }
    }
}
