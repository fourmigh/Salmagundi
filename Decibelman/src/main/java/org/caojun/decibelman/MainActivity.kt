package org.caojun.decibelman

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.baidu.mapapi.SDKInitializer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_gdmap.*

class MainActivity : Activity() {

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

        toolbar.setTitle(R.string.app_name)
        setFragment(mapFragment, decibelFragment)

//        mapFragment.gdMapView.onCreate(savedInstanceState)
    }

    private fun setFragment(hide: Fragment, show: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.hide(hide)
        transaction.show(show)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if (navigation.selectedItemId != R.id.navigation_decibel) {
            doMapFragment()
        }
    }

    private fun doDecibelFragment() {
        setFragment(mapFragment, decibelFragment)
    }

    private fun doMapFragment() {
        setFragment(decibelFragment, mapFragment)
    }
}
