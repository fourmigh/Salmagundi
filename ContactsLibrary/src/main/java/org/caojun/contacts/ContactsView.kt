package org.caojun.contacts

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.contacts.view.*
import java.util.*

class ContactsView: LinearLayout {

    private var mAllContactsList: List<Contact> = ArrayList()
    private var adapter: ContactAdapter? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.contacts,this)

        sideBar.setTextView(textView)

        ivClearText.setOnClickListener {
            etSearch.setText("")
        }

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(e: Editable) {

                val content = etSearch.text.toString()
                if (TextUtils.isEmpty(content)) {
                    ivClearText.visibility = View.INVISIBLE

                    adapter?.update(mAllContactsList)
                } else {
                    ivClearText.visibility = View.VISIBLE

                    val filterList = search(content)
                    adapter?.update(filterList)
                }
            }

        })

        //设置右侧[A-Z]快速导航栏触摸监听
        sideBar.setOnTouchLetterChangedListener(object : SideBar.OnTouchLetterChangedListener {

            override fun onTouchLetterChanged(c: Char) {
                //该字母首次出现的位置
                val position = adapter?.getPositionForSection(c)?:-1
                if (position != -1) {
                    listView.setSelection(position)
                }
            }
        })
        listView.setOnItemClickListener { _, view, position, _ ->
            val viewHolder = view.tag as ContactAdapter.ViewHolder
            viewHolder.cbChecked?.performClick()
            adapter?.toggleChecked(position)
        }
    }

    fun init(list: List<Contact>) {
        mAllContactsList = list
        Collections.sort(mAllContactsList)
        adapter = ContactAdapter(context, mAllContactsList)
        listView.adapter = adapter

        sideBar.init(mAllContactsList)
    }

    /**
     * 模糊查询
     * @param str
     * @return
     */
    private fun search(str: String): List<Contact> {
        val filterList = ArrayList<Contact>()// 过滤后的list
        for (contact in mAllContactsList) {
            if (TextUtils.isEmpty(contact.getTitle())) {
                continue
            }
            //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
            val isTitleContains = contact.getTitle().toLowerCase(Locale.CHINESE)
                    .contains(str.toLowerCase(Locale.CHINESE))

            val isInitialsContains = contact.getInitials().toLowerCase(Locale.CHINESE)
                    .contains(str.toLowerCase(Locale.CHINESE))

            val isWholeSpellContains = contact.getSort().toLowerCase(Locale.CHINESE)
                    .contains(str.toLowerCase(Locale.CHINESE))

            if (isTitleContains || isInitialsContains || isWholeSpellContains) {
                filterList.add(contact)
            }
        }
        return filterList
    }
}