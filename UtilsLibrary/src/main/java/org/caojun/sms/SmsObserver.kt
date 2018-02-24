package com.allinpay.jinfutong.sms

import android.database.ContentObserver
import android.content.ContentResolver
import android.net.Uri
import android.os.Handler
import java.util.regex.Pattern

/**
 * Created by CaoJun on 2018-2-24.
 */
class SmsObserver: ContentObserver {

    private var mResolver: ContentResolver
    private var listener: SmsListener
    private var pattern: Pattern? = null

    constructor(mResolver: ContentResolver, listener: SmsListener, pattern: Pattern?): super(Handler()) {
        this.mResolver = mResolver
        this.listener = listener
        this.pattern = pattern
    }

    override fun onChange(selfChange: Boolean) {
        var mCursor = mResolver.query(Uri.parse("content://sms/inbox"),
                arrayOf("_id", "address", "read", "body", "thread_id"),
                "read=?", arrayOf("0"), "date desc")

        if (mCursor == null) {
            return
        } else {
            while (mCursor.moveToNext()) {
                val _smsInfo = SmsInfo()

                val _inIndex = mCursor.getColumnIndex("_id")
                if (_inIndex != -1) {
                    _smsInfo._id = mCursor.getString(_inIndex)
                }

                val thread_idIndex = mCursor.getColumnIndex("thread_id")
                if (thread_idIndex != -1) {
                    _smsInfo.thread_id = mCursor.getString(thread_idIndex)
                }

                val addressIndex = mCursor.getColumnIndex("address")
                if (addressIndex != -1) {
                    _smsInfo.address = mCursor.getString(addressIndex)
                }

                val bodyIndex = mCursor.getColumnIndex("body")
                if (bodyIndex != -1) {
                    _smsInfo.body = mCursor.getString(bodyIndex)
                }

                val readIndex = mCursor.getColumnIndex("read")
                if (readIndex != -1) {
                    _smsInfo.read = mCursor.getString(readIndex)
                }

                if (pattern == null) {
                    listener.onReadSms(_smsInfo)
                } else {
                    val matcher = pattern!!.matcher(_smsInfo.body)//进行匹配
                    if (matcher.find()) {//匹配成功
                        val code = matcher.group(0)
                        listener.onReadPattern(code)
                    }
                }
            }

            mCursor.close()
        }
    }
}