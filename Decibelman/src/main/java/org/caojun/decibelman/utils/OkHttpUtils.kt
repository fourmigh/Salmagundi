package org.caojun.decibelman.utils

import okhttp3.*


/**
 * Created by CaoJun on 2017/9/18.
 */
object OkHttpUtils {
//    private val JSON = MediaType.parse("application/json; charset=utf-8")
//
//    fun post(url: String, json: String): Response {
//        val body = RequestBody.create(JSON, json)
//        val request = Request.Builder().url(url).post(body).build()
//        val client = OkHttpClient()
//        return client.newCall(request).execute()
//    }
//
//    fun ceratePoi(json: String): Response {
//        val url = "http://api.map.baidu.com/geodata/v3/poi/create"
//        return post(url, json)
//    }

    fun post(url: String, body: RequestBody): Response {
        val request = Request.Builder().url(url).post(body).build()
        val client = OkHttpClient()
        return client.newCall(request).execute()
    }

    fun ceratePoi(body: RequestBody): Response {
        val url = "http://api.map.baidu.com/geodata/v3/poi/create"
        return post(url, body)
    }
}