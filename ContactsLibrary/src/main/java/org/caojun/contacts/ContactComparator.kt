package org.caojun.contacts

class ContactComparator: Comparator<Contact> {

    override fun compare(o1: Contact, o2: Contact): Int {
        return o1.getSort().compareTo(o2.getSort())
    }
}