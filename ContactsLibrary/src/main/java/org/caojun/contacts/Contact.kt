package org.caojun.contacts

import org.caojun.pinyin.PinyinUtils
import java.util.*

class Contact: Comparable<Contact> {

    private var index = 0//原列表中的序号
    private var id = ""
    private var title = ""
    private var content: String?
    private var sort = ""//全拼
    private var initials = ""//首字母
    private var checked = false
    private var icon = 0

    constructor(index: Int, id: String, title: String, content: String?, icon: Int) {
        this.index = index
        this.id = id
        this.title = title
        this.content = content
        if (Locale.getDefault().language == "zh") {
            sort = PinyinUtils.toPinyinString(title)
            initials = PinyinUtils.toPinyinInitials(title)
        } else {
            sort = title
            initials = title[0].toString()
        }
        this.icon = icon
    }

    fun getIcon(): Int {
        return icon
    }

    fun setChecked(checked: Boolean) {
        this.checked = checked
    }

    fun isChecked(): Boolean {
        return checked
    }

    fun getIndex(): Int {
        return index
    }

    fun getSortLetter(): Char {
        return sort[0].toUpperCase()
    }

    fun getSort(): String {
        return sort
    }

    fun getTitle(): String {
        return title
    }

    fun getContent(): String? {
        return content
    }

    fun getInitials(): String {
        return initials
    }

    override fun compareTo(other: Contact): Int {
        return sort.compareTo(other.getSort())
    }
}