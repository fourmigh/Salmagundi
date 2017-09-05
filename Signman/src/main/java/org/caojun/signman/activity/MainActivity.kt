package org.caojun.signman.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.caojun.signman.Constant
import org.caojun.signman.R
import org.caojun.signman.adapter.AppAdapter
import org.caojun.signman.room.App
import org.caojun.signman.room.AppDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

class MainActivity : AppCompatActivity() {

    private val list: ArrayList<App> = ArrayList()
    private var listView: StickyListHeadersListView? = null
    private var canceled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSetup: Button = findViewById(R.id.btnSetup)
        listView = findViewById(R.id.listView)

        btnSetup.setOnClickListener({
            gotoSetupApps()
        })

        list.clear()
    }

    override fun onResume() {
        super.onResume()
        //读取数据库，若列表为空，则跳转到AppsActivity
        if (canceled) {
            return
        }
        doAsync {
            val apps = AppDatabase.getDatabase(baseContext).getAppDao().queryAll()
            uiThread {
                if (!apps.isEmpty()) {
                    list.addAll(apps)
                    val adapter = AppAdapter(baseContext, list)
                    listView?.adapter = adapter
                } else {
                    gotoSetupApps()
                }
            }
        }
    }

    private fun gotoSetupApps() {
        canceled = false
        val intent = Intent(this, AppsActivity::class.java)
        startActivityForResult(intent, Constant.RequestCode_AppList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.RequestCode_AppList) {
            if (resultCode != Activity.RESULT_OK) {
                canceled = true
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
