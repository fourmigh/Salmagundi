package org.caojun.rotaryphone.activity

import android.Manifest
import android.os.Bundle
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.rotaryphone.R
import org.caojun.rotaryphone.widget.RotaryView
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.TextUtils
import org.caojun.activity.BaseAppCompatActivity
import android.widget.ArrayAdapter

class MainActivity : BaseAppCompatActivity() {

    private val SEPARATOR = "，"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rotaryView.setOnRotaryListener(object : RotaryView.OnRotaryListener {
            override fun onDial(number: String) {
                val text = autoCompleteTextView.text.toString() + number
                autoCompleteTextView.setText(text)
                autoCompleteTextView.setSelection(autoCompleteTextView.text.length)
            }

            override fun onRotating() {
                KLog.d("OnRotaryListener", "onRotating")
            }

            override fun onDialing() {
                KLog.d("OnRotaryListener", "onDialing")
            }

            override fun onStopDialing() {
                KLog.d("OnRotaryListener", "onStopDialing")
            }
        })

        checkSelfPermission(Manifest.permission.READ_CONTACTS, object : RequestPermissionListener {
            override fun onSuccess() {
                val list = getPhoneContacts()
                val adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_dropdown_item_1line, list)
                autoCompleteTextView.setAdapter(adapter)
            }

            override fun onFail() {
            }
        })
    }

    private fun getPhoneContacts(): List<String> {
        /**联系人显示名称**/
        val PHONES_DISPLAY_NAME_INDEX = 0
        /**电话号码**/
        val PHONES_NUMBER_INDEX = 1;
        val PHONES_PROJECTION = arrayOf(Phone.DISPLAY_NAME, Phone.NUMBER)

        val resolver = this.contentResolver

        // 获取手机联系人
        val phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null)

        val list = ArrayList<String>()
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                val phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX)
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue

                //得到联系人名称
                val contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX)

                list.add(phoneNumber + SEPARATOR + contactName)
            }

            phoneCursor.close()
        }
        return list
    }
}
