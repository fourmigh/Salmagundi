package com.allinpay.jinfutong.sms

/**
 * Created by CaoJun on 2018-2-24.
 */
interface SmsListener {
    fun onReadSms(smsInfo: SmsInfo)
    fun onReadPattern(code: String)
}