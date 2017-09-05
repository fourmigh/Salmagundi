package org.caojun.signman.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import org.caojun.signman.R
import android.app.Activity
import android.content.pm.ApplicationInfo
import org.caojun.signman.adapter.AppSelectAdapter
import org.caojun.signman.room.App
import org.caojun.signman.room.AppDatabase
import org.caojun.signman.utils.AppSortComparator
import org.jetbrains.anko.doAsync
import se.emilsjolander.stickylistheaders.StickyListHeadersListView
import java.util.Collections


/**
 * Created by CaoJun on 2017/8/31.
 */
class AppsActivity : AppCompatActivity() {

    private val list: ArrayList<App> = ArrayList()
    private var listView: StickyListHeadersListView? = null
    private var adapter:AppSelectAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSetup: Button = findViewById(R.id.btnSetup)
        btnSetup.text = getString(android.R.string.ok)

        listView = findViewById(R.id.listView)

        btnSetup.setOnClickListener({
            saveSelectedApps()
            setResult(Activity.RESULT_OK)
            finish()
        })

        list.clear()
    }

    private fun saveSelectedApps() {
        var apps = adapter?.getSelectedApps()
        doAsync {
            AppDatabase.getDatabase(baseContext).getAppDao().insert(apps!!)
        }
    }

    override fun onResume() {
        super.onResume()

        if (list.isEmpty()) {
            val packages = packageManager.getInstalledPackages(0)
            for (i in packages.indices) {
                val packageInfo = packages[i]
                if (packageInfo.applicationInfo.flags == ApplicationInfo.FLAG_SYSTEM) { //非系统应用
                    // AppInfo 自定义类，包含应用信息
                    val app = App()
                    app.name = packageInfo.applicationInfo.loadLabel(packageManager).toString()//获取应用名称
                    app.packageName = packageInfo.packageName //获取应用包名，可用于卸载和启动应用
//                app.setVersionName(packageInfo.versionName)//获取应用版本名
//                app.setVersionCode(packageInfo.versionCode)//获取应用版本号
                    app.icon = packageInfo.applicationInfo.loadIcon(packageManager)//获取应用图标
                    list.add(app)
                }
            }

            Collections.sort(list, AppSortComparator())

            adapter = AppSelectAdapter(this, list)
            listView?.adapter = adapter
        }
    }
}