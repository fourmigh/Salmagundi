package org.caojun.signman.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.signman.Constant
import org.caojun.signman.R
import org.caojun.signman.adapter.AppAdapter
import org.caojun.signman.room.App
import org.caojun.signman.room.AppDatabase
import org.caojun.signman.utils.ActivityUtils
import org.caojun.signman.utils.AppSortComparator
import org.caojun.signman.utils.TimeUtils
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.util.Collections

class MainActivity : AppCompatActivity() {

//    private val list: ArrayList<App> = ArrayList()
    private var canceled: Boolean = false
    private var adapter: AppAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTips?.visibility = View.GONE

        listView?.setOnItemClickListener({ parent, view, position, id -> doListViewItemClick(position) })

        btnSetup?.setOnClickListener({
            gotoSetupApps()
        })

//        list.clear()
        Constant.Apps.clear()
    }

    private fun doListViewItemClick(position: Int) {
        val app = adapter?.getItem(position)
        val time = app?.time!!
        val size = time.size
        val strings = arrayOfNulls<String>(size)
        for (i in 0..(size - 1)) {
            strings[i] = TimeUtils.getTime("yyyy-MM-dd HH:mm:ss", time[i].time)
        }
        AlertDialog.Builder(this)
                .setTitle(app.name)
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
        loadApps()
    }

    private fun loadApps() {
        doAsync {
            val apps = AppDatabase.getDatabase(baseContext).getAppDao().queryAll()
            uiThread {
                if (!apps.isEmpty()) {
                    Constant.Apps.clear()
                    Collections.sort(apps, AppSortComparator())
                    Constant.Apps.addAll(apps)
                    for (app in Constant.Apps) {
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
                        adapter = AppAdapter(this@MainActivity, Constant.Apps)
                        listView?.adapter = adapter
                    } else {
                        adapter?.setData(Constant.Apps)
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
//        intent.putParcelableArrayListExtra("apps", list)
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

    fun doUninstalledAlert(app: App) {
        alert(getString(R.string.uninstalled_alert, app.name)) {
            positiveButton(R.string.reinstall) {
                ActivityUtils.gotoMarket(this@MainActivity, app.packageName!!)
            }
            negativeButton(R.string.remove) {
                progressBar?.visibility = View.VISIBLE
                doAsync {
                    AppDatabase.getDatabase(this@MainActivity).getAppDao().delete(app)
                    loadApps()
                }
            }
            neutralPressed(android.R.string.cancel, {})
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity<SettingsActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
