package org.caojun.cafe.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.cafe.R
import org.caojun.cafe.fragement.CafeFragment
import org.caojun.cafe.fragement.MaterialFragment

class MainActivity : AppCompatActivity() {

    private var currentFragment: Fragment? = null
    private val cafeFragment = CafeFragment()
    private val materialFragment = MaterialFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_material -> {
                    switchFragment(materialFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_cafe -> {
                    switchFragment(cafeFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        switchFragment(materialFragment)
    }

    private fun switchFragment(targetFragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frameLayout, targetFragment, targetFragment.tag)

        } else {
            transaction.hide(currentFragment).show(targetFragment)
        }
        currentFragment = targetFragment
        transaction.commit()
    }
}
