package org.caojun.signman.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.caojun.signman.R
import android.app.Activity
import android.content.pm.ApplicationInfo
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.signman.Constant
import org.caojun.signman.adapter.AppSelectAdapter
import org.caojun.signman.room.App
import org.caojun.signman.room.AppDatabase
import org.caojun.signman.utils.AppSortComparator
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.Collections

/**
 * Created by CaoJun on 2017/8/31.
 */
class AppsActivity : AppCompatActivity() {

    private val list: ArrayList<App> = ArrayList()
    private var adapter:AppSelectAdapter? = null
//    private var apps: ArrayList<App> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        apps = intent.getParcelableArrayListExtra("apps")

        btnAppList.text = getString(android.R.string.ok)

        btnAppList.setOnClickListener({
            saveSelectedApps()
        })

        list.clear()

        btnAnimation.visibility = View.GONE
    }

    private fun saveSelectedApps() {
        progressBar?.visibility = View.VISIBLE
        var apps = adapter?.getSelectedApps()
        doAsync {
            for (app in apps!!) {
                AppDatabase.getDatabase(baseContext).getAppDao().insert(app)
            }
            uiThread {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        progressBar?.visibility = View.VISIBLE
        doAsync {
            if (list.isEmpty()) {

                val packages = packageManager.getInstalledPackages(0)
                for (i in packages.indices) {
                    val packageInfo = packages[i]
                    if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用
                        if (packageInfo.packageName.equals(packageName)) {
                            //不把自身应用加载到列表中
                            continue
                        }
                        // AppInfo 自定义类，包含应用信息
                        val app = App()
                        app.name = packageInfo.applicationInfo.loadLabel(packageManager).toString()//获取应用名称
                        app.packageName = packageInfo.packageName //获取应用包名，可用于卸载和启动应用
//                        app.setVersionName(packageInfo.versionName)//获取应用版本名
//                        app.setVersionCode(packageInfo.versionCode)//获取应用版本号
                        app.icon = packageInfo.applicationInfo.loadIcon(packageManager)//获取应用图标
                        list.add(app)
                    }
                }

                Collections.sort(list, AppSortComparator())

                for (app in list) {
                    if (Constant.Apps.contains(app)) {
                        app.isSelected = true
                    }
                }

                uiThread {
                    adapter = AppSelectAdapter(baseContext, list)
                    listView?.adapter = adapter
                }

            }

            uiThread {
                progressBar?.visibility = View.GONE
            }
        }
    }
}