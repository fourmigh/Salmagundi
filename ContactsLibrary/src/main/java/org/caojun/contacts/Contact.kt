package org.caojun.contacts

import org.caojun.pinyin.PinyinUtils

class Contact: Comparable<Contact> {

    private var id = ""
    private var title = ""
    private var content: String?
    private var sort = ""//全拼
    private var initials = ""//首字母

    constructor(id: String, title: String, content: String?) {
        this.id = id
        this.title = title
        this.content = content
        sort = PinyinUtils.toPinyinString(title)
        initials = PinyinUtils.toPinyinInitials(title)
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