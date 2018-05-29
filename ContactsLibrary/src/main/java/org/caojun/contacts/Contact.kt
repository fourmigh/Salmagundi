package org.caojun.contacts

import org.caojun.pinyin.PinyinUtils

class Contact: Comparable<Contact> {

    private var index = 0//原列表中的序号
    private var id = ""
    private var title = ""
    private var content: String?
    private var sort = ""//全拼
    private var initials = ""//首字母
    private var checked = false

    constructor(index: Int, id: String, title: String, content: String?) {
        this.index = index
        this.id = id
        this.title = title
        this.content = content
        sort = PinyinUtils.toPinyinString(title)
        initials = PinyinUtils.toPinyinInitials(title)
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