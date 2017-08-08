package org.caojun.rcn

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.socks.library.KLog
import org.caojun.rcn.utils.ChineseNameUtils

class MainActivity : AppCompatActivity() {

    private var isSurnameChecked: Boolean = false
    private var isNameChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnSurname: Button = findViewById(R.id.btnSurname)
        var btnName: Button = findViewById(R.id.btnName)
        var etSurname: EditText = findViewById(R.id.etSurname)
        var etName: EditText = findViewById(R.id.etName)
        var rgSurname: RadioGroup = findViewById(R.id.rgSurname)
        var rgName: RadioGroup = findViewById(R.id.rgName)
        var webView: WebView = findViewById(R.id.webView)
        var btnGenerate: Button = findViewById(R.id.btnGenerate)

        val surnameType: Array<out String> = resources.getStringArray(R.array.surname_type)
        var rbSurnames: Array<RadioButton?> = arrayOfNulls<RadioButton>(surnameType.size)
        for (i in surnameType.indices) {
            rbSurnames[i] = RadioButton(this)
            rbSurnames[i]?.text = surnameType[i]
            rgSurname.addView(rbSurnames[i])
        }

        val nameType: Array<out String> = resources.getStringArray(R.array.name_type)
        var rbNames: Array<RadioButton?> = arrayOfNulls<RadioButton>(nameType.size)
        for (i in nameType.indices) {
            rbNames[i] = RadioButton(this)
            rbNames[i]?.text = nameType[i]
            rgName.addView(rbNames[i])
        }

        rgSurname.setOnCheckedChangeListener({ group, _ ->
            isSurnameChecked = true
//            btnGenerate.isEnabled = isSurnameChecked and isNameChecked
            checkButtonEnable(group, rgName, btnGenerate)
            val index = group.indexOfChild(group.findViewById(group.checkedRadioButtonId))
            etSurname.isEnabled = index == group.childCount - 1
        })

        rgName.setOnCheckedChangeListener({ group, _ ->
            isNameChecked = true
//            btnGenerate.isEnabled = isSurnameChecked and isNameChecked
            checkButtonEnable(rgSurname, group, btnGenerate)
            val index = group.indexOfChild(group.findViewById(group.checkedRadioButtonId))
            etName.isEnabled = index == group.childCount - 1
        })

        btnSurname.setOnClickListener({ showExplain(webView, etSurname.text.toString()) })

        btnSurname.setOnLongClickListener({
            showFullName(etSurname, etName, webView)
            true
        })

        btnName.setOnClickListener({ showExplain(webView, etName.text.toString()) })

        btnName.setOnLongClickListener({
            showExplain(webView, etName.text.toString(), true)
            true
        })

        etSurname.isEnabled = false
        etName.isEnabled = false
        btnGenerate.isEnabled = isSurnameChecked and isNameChecked
        btnGenerate.setOnClickListener({ doGenerate(rgSurname, etSurname, rgName, etName) })
    }

    private fun doGenerate(rgSurname:RadioGroup, etSurname:EditText, rgName:RadioGroup, etName:EditText) {
        val surnameType = rgSurname.indexOfChild(rgSurname.findViewById(rgSurname.checkedRadioButtonId))
        if (surnameType != ChineseNameUtils.Type_Surname_Custom) {
            val surname = ChineseNameUtils.getSurname(this, surnameType)
            etSurname.setText(surname)
        }
        val nameType = rgName.indexOfChild(rgName.findViewById(rgName.checkedRadioButtonId))
        if (nameType != ChineseNameUtils.Type_Name_Custom) {
            val name = ChineseNameUtils.getName(nameType)
            etName.setText(name)
        }
    }

    private fun showExplain(webView: WebView, text: String) {
        showExplain(webView, text, false)
    }

    private fun showExplain(webView: WebView, text: String, isFullName: Boolean) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        var url = "http://hanyu.baidu.com/zici/s?wd=" + text
        if (isFullName) {
            val urls = resources.getStringArray(R.array.search_url)
//            val index = ChineseNameUtils.getRandom(0, urls.size - 1)
            val index = 0
            url = urls[index] + text
            KLog.d("url", url)
        }
        webView.loadUrl(url)
    }

    private fun showFullName(etSurname: EditText, etName: EditText, webView: WebView) {
        val surname = etSurname.text.toString()
        if (TextUtils.isEmpty(surname)) {
            return
        }
        val name = etName.text.toString()
        if (TextUtils.isEmpty(name)) {
            return
        }
        showExplain(webView, surname + name, true)
    }

    private fun checkButtonEnable(rgSurname:RadioGroup, rgName:RadioGroup, btnGenerate:Button) {
        btnGenerate.isEnabled = isSurnameChecked and isNameChecked
        if (btnGenerate.isEnabled) {
            val indexSurname = rgSurname.indexOfChild(rgSurname.findViewById(rgSurname.checkedRadioButtonId))
            val indexName = rgName.indexOfChild(rgName.findViewById(rgName.checkedRadioButtonId))
            if (indexSurname == rgSurname.childCount - 1 && indexName == rgName.childCount - 1) {
                //姓和名都选中自定义
                btnGenerate.isEnabled = false
            }
        }
    }
}
