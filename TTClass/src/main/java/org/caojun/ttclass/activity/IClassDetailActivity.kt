package org.caojun.ttclass.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.NumberPicker
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_iclass_detail.*
import kotlinx.android.synthetic.main.layout_confirm.*
import org.caojun.dialog.NumberPickerDialog
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.Utilities
import org.caojun.ttclass.dialog.BillDetailDialog
import org.caojun.ttclass.room.*
import org.jetbrains.anko.*
import java.util.*

class IClassDetailActivity : AppCompatActivity() {

    private var iClass: IClass? = null
    private val signs = ArrayList<Sign>()
    private var isAdd = false
    private var isInfoChanged = false
    private var scheduleWeekdays: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iclass_detail)

        btnRemainder.setOnClickListener {
            var current = 10
            if (TextUtils.isDigitsOnly(btnRemainder.text)) {
                current = Integer.parseInt(btnRemainder.text.toString())
            }
            NumberPickerDialog(this, NumberPicker.OnValueChangeListener { _, _, newValue ->
                btnRemainder.text = newValue.toString()
            }, 99, 1, current).show()
        }

        btnBillList.setOnClickListener {
            startActivity<BillListActivity>(Constant.Key_ClassID to iClass?.id)
        }

        btnBillDetail.setOnClickListener {
            BillDetailDialog(this, NumberPicker.OnValueChangeListener { _, amount, number ->
                addBill(amount, number)
            }, 99, 1, 10).show()
        }

        btnName.setOnClickListener {
            setNameEdit(true)
        }

        btnGrade.setOnClickListener {
            setGradeEdit(true)
        }

        btnSchedule.setOnClickListener {
            startActivityForResult<ScheduleDetailActivity>(Constant.RequestCode_Schedule, Constant.Key_Schedule to iClass?.schedule)
        }

        btnTeacher.setOnClickListener {
            startActivityForResult<TeacherDetailActivity>(Constant.RequestCode_Teacher, Constant.Key_TeacherID to iClass?.idTeacher)
        }

        btnSchool.setOnClickListener {
            startActivityForResult<SchoolDetailActivity>(Constant.RequestCode_School, Constant.Key_Class to iClass)
        }

        btnSign.setOnClickListener {
            startActivityForResult<ScheduleListActivity>(Constant.RequestCode_ScheduleList, Constant.Key_ClassID to iClass?.id, Constant.Key_ScheduleWeekdays to scheduleWeekdays)
        }

        btnNote.setOnClickListener {
            startActivity<SignListActivity>(Constant.Key_Class to iClass)
        }

        btnOK.setOnClickListener {
            doOK()
        }

        btnCancel.setOnClickListener {
            doCancel()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constant.RequestCode_Schedule -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val lastSchedule = iClass?.schedule
                    iClass?.schedule = data.getParcelableExtra(Constant.Key_Schedule)
                    if (iClass?.schedule != null && iClass?.schedule!!.isChanged(lastSchedule)) {
                        isInfoChanged = true
                    }
                }
                return
            }
            Constant.RequestCode_Teacher -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val lastTeachID = iClass?.idTeacher
                    iClass?.idTeacher = data.getIntExtra(Constant.Key_TeacherID, -1)
                    if (lastTeachID != iClass?.idTeacher) {
                        isInfoChanged = true
                    }
                }
                return
            }
            Constant.RequestCode_School -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val lastSchoolID = iClass?.idSchool
                    iClass?.idSchool = data.getIntExtra(Constant.Key_SchoolID, -1)
                    if (lastSchoolID != iClass?.idSchool) {
                        isInfoChanged = true
                    }
                }
                return
            }
            Constant.RequestCode_ScheduleList -> {
                //签到选中某天
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val time = data.getLongExtra(Constant.Key_Day, 0)
                    val date = Date(time)
                    doSign(date)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun doSign(date: Date) {
        doAsync {
            val idClass = iClass?.id?:-1
            var list = TTCDatabase.getDatabase(this@IClassDetailActivity).getSign().query(idClass)
            val lastSize = list.size
            if (Utilities.dateInSigns(date, list)) {
                return@doAsync
            }
            val sign = Sign()
            sign.idClass = iClass!!.id
            sign.time = date
            TTCDatabase.getDatabase(this@IClassDetailActivity).getSign().insert(sign)
            list = TTCDatabase.getDatabase(this@IClassDetailActivity).getSign().query(idClass)
            if (list.size - lastSize == 1) {
                //新增一条签到记录
                iClass!!.reminder --
                if (iClass!!.reminder < 0) {
                    iClass!!.reminder = 0
                }
                TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().update(iClass!!)
            }
            uiThread {
                btnNote.isEnabled = list.isNotEmpty()
                if (list.size - lastSize == 1) {
                    btnRemainder.text = iClass!!.reminder.toString()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isAdd = intent.getBooleanExtra(Constant.Key_IsNew, false)
        if (iClass == null) {
            iClass = intent.getParcelableExtra(Constant.Key_Class)
        }
        refreshUI(isAdd)
    }

    override fun onPause() {
        super.onPause()
        val edit = this.getSharedPreferences(this.javaClass.name, Context.MODE_PRIVATE).edit()
        edit.putString(Constant.Key_Name, etName.text.toString())
        edit.putString(Constant.Key_Grade, etGrade.text.toString())
        edit.commit()
    }



//    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        super.onRestoreInstanceState(savedInstanceState)
//        KLog.d("IClassDetailActivity", "onRestoreInstanceState")
//        etName.setText(savedInstanceState?.getString(Key_Name, iClass?.name))
//        etGrade.setText(savedInstanceState?.getString(Key_Grade, iClass?.grade))
//    }
//
//    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
//        KLog.d("IClassDetailActivity", "onSaveInstanceState")
//        outState?.putString(Key_Name, etName.text.toString())
//        outState?.putString(Key_Grade, etGrade.text.toString())
//    }

    private fun refreshUI(isEdit: Boolean) {
        if (iClass == null) {
            finish()
            return
        }

        doAsync {
            val idClass = iClass?.id?:-1
            val list = TTCDatabase.getDatabase(this@IClassDetailActivity).getSign().query(idClass)
            signs.clear()
            if (list.isNotEmpty()) {
                signs.addAll(list)
            }
            val bills = TTCDatabase.getDatabase(this@IClassDetailActivity).getBill().query(idClass)
            scheduleWeekdays = getScheduleWeekdays()
            uiThread {

                val sp = this@IClassDetailActivity.getSharedPreferences(this@IClassDetailActivity.javaClass.name, Context.MODE_PRIVATE)
                etName.setText(sp.getString(Constant.Key_Name, iClass?.name))
                etGrade.setText(sp.getString(Constant.Key_Grade, iClass?.grade))

                btnNote.isEnabled = signs.size > 0
                btnSign.isEnabled = iClass?.reminder?:0 > 0 && scheduleWeekdays != null
                btnSchool.isEnabled = iClass?.idTeacher?:-1 >= 0
                btnRemainder.text = iClass?.reminder.toString()

                btnBillList.text = bills.size.toString()
                btnBillList.isEnabled = bills.isNotEmpty()

                setNameEdit(isEdit)
                setGradeEdit(isEdit)
            }
        }
    }

    private fun setNameEdit(isEdit: Boolean) {
        val params = TableRow.LayoutParams()
        if (isEdit) {
            params.span = 3
            etName.requestFocus()
        } else {
            params.span = 2
        }
        etName.isEnabled = isEdit
        btnName.visibility = if (isEdit) View.GONE else View.VISIBLE
        tilName.layoutParams = params
    }

    private fun setGradeEdit(isEdit: Boolean) {
        val params = TableRow.LayoutParams()
        if (isEdit) {
            params.span = 3
            etGrade.requestFocus()
        } else {
            params.span = 2
        }
        etGrade.isEnabled = isEdit
        btnGrade.visibility = if (isEdit) View.GONE else View.VISIBLE
        tilGrade.layoutParams = params
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!isAdd) {
            menuInflater.inflate(R.menu.iclass, menu)
        }
        return true
    }

    private fun addClass() {
        val intent = Intent()
        intent.putExtra(Constant.Key_AddClass, true)
        setResult(Activity.RESULT_OK, intent)
        isAdd = false
        finish()
    }

    private fun doAddClass() {
        if (checkInfoChanged()) {
            alert(R.string.abort_save) {
                title = getString(R.string.alert)
                positiveButton(R.string.abort) {
                    addClass()
                }
                negativeButton(R.string.save) {
                    doAsync {
                        doSave()
                        addClass()
                    }
                }
            }.show()
            return
        }
        addClass()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_class -> {
                doAddClass()
                true
            }
            R.id.action_delete_class -> {
                var name = etName.text.toString()
                var grade = etGrade.text.toString()
                var msg: String
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(grade)) {
                    msg = getString(R.string.delete_class_tips, "")
                } else {
                    if (!TextUtils.isEmpty(name)) {
                        name = " " + name
                    }
                    if (!TextUtils.isEmpty(grade)) {
                        grade += " "
                    }
                    val content = getString(R.string.delete_msg, name, grade)
                    msg = getString(R.string.delete_class_tips, content)
                }
                alert(msg) {
                    title = getString(R.string.alert)
                    positiveButton(R.string.delete) {
                        doAsync {
                            TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().delete(iClass!!)
                            finish()
                        }
                    }
                    noButton {}
                }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkInfoChanged(): Boolean {
        if (isInfoChanged) {
            return true
        }
        if (etName.text.toString() != iClass?.name) {
            return true
        }
        if (etGrade.text.toString() != iClass?.grade) {
            return true
        }
        return false
    }

    private fun doCancel() {
        //检查是否有内容变化过
        if (checkInfoChanged()) {
            alert(R.string.abort_save) {
                title = getString(R.string.alert)
                positiveButton(R.string.abort) {
                    finish()
                }
                negativeButton(R.string.save) {
                    doOK()
                }
            }.show()
            return
        }
        finish()
    }

    override fun finish() {
        doAsync {
            if (isAdd) {
                TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().delete(iClass!!)
            }
        }
        super.finish()
    }

    override fun onDestroy() {
        doAsync {
            if (isAdd) {
                TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().delete(iClass!!)
            }
        }
        val edit = this.getSharedPreferences(this.javaClass.name, Context.MODE_PRIVATE).edit()
        edit.clear()
        edit.commit()
        super.onDestroy()
    }

    private fun doSave() {
        if (iClass == null) {
            finish()
            return
        }
        iClass?.name = etName.text.toString()
        iClass?.grade = etGrade.text.toString()
        isAdd = false
        doAsync {
            TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().insert(iClass!!)
        }
    }

    private fun doOK() {
        doSave()
        finish()
    }

    override fun onBackPressed() {
        doCancel()
    }

    /**
     * 续费
     * amount：金额，单位分
     * number：次数
     */
    private fun addBill(amount: Int, number: Int) {
        doAsync {
            val bill = Bill()
            bill.idClass = iClass!!.id
            bill.amount = (amount / 100).toFloat()
            bill.times = number
            TTCDatabase.getDatabase(this@IClassDetailActivity).getBill().insert(bill)
            iClass!!.reminder += number
            TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().update(iClass!!)
            refreshUI(isAdd)
        }
    }

    /**
     * 获取课程安排星期N
     */
    private fun getScheduleWeekdays(): IntArray? {
        if (iClass == null || iClass?.schedule == null) {
            return null
        }
        val weekdays = ArrayList<Int>()
        for (i in iClass!!.schedule!!.checked.indices) {
            if (!iClass!!.schedule!!.checked[i]) {
                continue
            }
            if (iClass!!.schedule!!.time[i][0] == getString(R.string.start_time)) {
                continue
            }
            if (iClass!!.schedule!!.time[i][1] == getString(R.string.end_time)) {
                continue
            }
            weekdays.add(i)
        }
        var intArray = IntArray(weekdays.size)
        for (i in weekdays.indices) {
            intArray[i] = weekdays[i]
        }
        return intArray
    }
}
