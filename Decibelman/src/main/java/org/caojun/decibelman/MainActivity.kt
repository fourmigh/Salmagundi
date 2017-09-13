package org.caojun.decibelman

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private val manager: FragmentManager = getFragmentManager()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_decibel -> {
                setFragment(mapFragment, decibelFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                setFragment(decibelFragment, mapFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        toolbar.setTitle(R.string.app_name)
        setFragment(mapFragment, decibelFragment)
    }

    private fun setFragment(hide: Fragment, show: Fragment) {
        val transaction = manager.beginTransaction()
        transaction.hide(hide)
        transaction.show(show)
        transaction.commit()
    }

}
