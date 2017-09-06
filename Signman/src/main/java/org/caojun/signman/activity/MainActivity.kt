package org.caojun.signman.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.socks.library.KLog
import org.caojun.signman.Constant
import org.caojun.signman.R
import org.caojun.signman.adapter.AppAdapter
import org.caojun.signman.room.App
import org.caojun.signman.room.AppDatabase
import org.caojun.signman.utils.TimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

class MainActivity : AppCompatActivity() {

    private val list: ArrayList<App> = ArrayList()
    private var listView: StickyListHeadersListView? = null
    private var canceled: Boolean = false
    private var adapter: AppAdapter? = null
    private var progressBar:ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvTips: TextView = findViewById(R.id.tvTips)
        tvTips.visibility = View.GONE
        
        progressBar = findViewById(R.id.progressBar)

        val btnSetup: Button = findViewById(R.id.btnSetup)
        listView = findViewById(R.id.listView)
        listView?.setOnItemClickListener({ parent, view, position, id -> doListViewItemClick(position) })

        btnSetup.setOnClickListener({
            gotoSetupApps()
        })

        list.clear()
    }

    private fun doListViewItemClick(position: Int) {
        KLog.d("doListViewItemClick", "position: " + position)
        val app = adapter?.getItem(position)
        val time = app?.time!!
        val size = time.size
        val strings = arrayOfNulls<String>(size)
        for (i in 0..(size - 1)) {
            strings[i] = TimeUtils.getTime("yyyy-MM-dd HH:mm:ss", time[i].time)
        }
        AlertDialog.Builder(this)
                .setTitle(R.string.sign_time_title)
                .setItems(strings, null)
                .create().show()
    }

    override fun onResume() {
        super.onResume()
        progressBar?.visibility = View.VISIBLE
        //读取数据库，若列表为空，则跳转到AppsActivity
        if (canceled) {
            progressBar?.visibility = View.GONE
            return
        }
        doAsync {
            val apps = AppDatabase.getDatabase(baseContext).getAppDao().queryAll()
            uiThread {
                if (!apps.isEmpty()) {
                    list.clear()
                    list.addAll(apps)
                    for (app in list) {
                        if (app.time.size < 1) {
                            app.isSigned = false
                        } else {
                            val time = app.getLastTime()
                            if (!TimeUtils.isToday(time)) {
                                app.isSigned = false
                            }
                        }
                    }
                    if (adapter == null) {
                        adapter = AppAdapter(baseContext, list)
                        listView?.adapter = adapter
                    } else {
                        adapter?.setData(list)
                        adapter?.notifyDataSetChanged()
                    }
                } else {
                    gotoSetupApps()
                }
                progressBar?.visibility = View.GONE
            }
        }
    }

    private fun gotoSetupApps() {
        canceled = false
        val intent = Intent(this, AppsActivity::class.java)
        intent.putParcelableArrayListExtra("apps", list)
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
