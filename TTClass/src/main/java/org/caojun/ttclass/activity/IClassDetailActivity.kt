package org.caojun.ttclass.activity

import android.app.Activity
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
import org.caojun.ttclass.dialog.PaymentDetailDialog
import org.caojun.ttclass.room.*
import org.jetbrains.anko.*
import java.util.*

class IClassDetailActivity : AppCompatActivity() {

    private var iClass: IClass? = null
    private val signs = ArrayList<Sign>()
    private var isAdd = false

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
            PaymentDetailDialog(this, NumberPicker.OnValueChangeListener { _, amount, number ->
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
            startActivity<TeacherDetailActivity>()
        }

        btnSchool.setOnClickListener {
            startActivity<SchoolDetailActivity>()
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
                    iClass?.schedule = data.getParcelableExtra(Constant.Key_Schedule)
                }
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()

        isAdd = intent.getBooleanExtra(Constant.Key_IsNew, false)
        iClass = intent.getParcelableExtra(Constant.Key_Class)
        refreshUI(isAdd)
    }

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
            uiThread {
                btnNote.isEnabled = signs.size > 0
                etName.setText(iClass?.name)
                etGrade.setText(iClass?.grade)
                btnSign.isEnabled = iClass?.reminder?:0 > 0
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_class -> {
                iClass = IClass()
                refreshUI(true)
                true
            }
            R.id.action_delete_class -> {
                val name = etName.text.toString()
                var grade = etGrade.text.toString()
                if (!TextUtils.isEmpty(grade)) {
                    grade += " "
                }
                val msg = getString(R.string.delete_class_tips, name, grade)
                alert(msg) {
                    title = getString(R.string.alert)
                    yesButton {
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

    private fun doCancel() {
        doAsync {
            if (isAdd) {
                TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().delete(iClass!!)
            }
            finish()
        }
    }

    private fun doOK() {
        if (iClass == null) {
            finish()
            return
        }
        iClass?.name = etName.text.toString()
        iClass?.grade = etGrade.text.toString()
        doAsync {
            TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().insert(iClass!!)
            finish()
        }
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
            val iclass = iClass!!
            val bill = Bill()
            bill.idClass = iclass.id
            bill.amount = (amount / 100).toFloat()
            bill.times = number
            TTCDatabase.getDatabase(this@IClassDetailActivity).getBill().insert(bill)
            iclass.reminder += number
            TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().update(iclass)
            refreshUI(isAdd)
        }
    }
}
