package org.caojun.decibelman.activity

import android.support.v4.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.baidu.mapapi.SDKInitializer
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.decibelman.R
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.socks.library.KLog
import org.caojun.decibelman.Constant
import org.caojun.decibelman.room.DecibelInfo
import org.caojun.decibelman.room.DecibelInfoDatabase
import org.caojun.decibelman.utils.AlgorithmUtils
import org.caojun.decibelman.utils.DeviceUtils
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import java.util.Date
import android.net.Uri.fromParts
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import org.caojun.decibelman.utils.ActivityUtils


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_decibel -> {
                doDecibelFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                doMapFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SDKInitializer.initialize(applicationContext)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        initDecibelInfoDatabase()
    }

    private fun setFragment(/*hide: Fragment, */show: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
//        transaction.hide(hide)
        transaction.show(show)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
//        if (navigation.selectedItemId != R.id.navigation_decibel) {
//            doMapFragment()
//        } else {
//            doDecibelFragment()
//        }
        navigation.selectedItemId = R.id.navigation_decibel
    }

    private fun doDecibelFragment() {
        setFragment(/*mapFragment, */decibelFragment)
    }

    private fun doMapFragment() {
//        setFragment(decibelFragment, mapFragment)
        startActivity<GDMapActivity>()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
//                val intent = Intent(this, SettingsActivity::class.java)
//                startActivity(intent)
                startActivity<SettingsActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initDecibelInfoDatabase() {
        doAsync {
            var list = DecibelInfoDatabase.getDatabase(this@MainActivity).getDao().queryAll()
            if (list.isEmpty()) {
                val time = Date().time
                Constant.decibelInfo0 = DecibelInfo()
                Constant.decibelInfo0?.imei = DeviceUtils.getIMEI(this@MainActivity)
                Constant.decibelInfo0?.random_id = AlgorithmUtils.getBase64String(4)
                Constant.decibelInfo0?.database_time = time
                Constant.decibelInfo0?.time = time
                DecibelInfoDatabase.getDatabase(this@MainActivity).getDao().insert(Constant.decibelInfo0!!)
            } else {
                Constant.decibelInfo0 = list[0]
            }
        }
    }

//    private fun checkSelfPermission() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Constant.Sound_Permission) != PackageManager.PERMISSION_GRANTED) {
////                if (shouldShowRequestPermissionRationale(Constant.Sound_Permission)) {
////                }
//                requestPermissions(arrayOf(Constant.Sound_Permission), Constant.RequestCode_Permissions);
//            }
//        }
//    }

    fun askRecordAudioPermission() {
        alert(R.string.ask_record_audio_permission) {
            positiveButton(android.R.string.ok) {
                ActivityUtils.gotoPermission(this@MainActivity)
            }
            negativeButton(R.string.quit) {
                finish()
            }
            onCancelled {
                finish()
            }
        }.show()
    }
}
