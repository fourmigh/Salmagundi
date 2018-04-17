package org.caojun.cafe.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.cafe.R
import org.caojun.cafe.fragement.CafeFragment
import org.caojun.cafe.fragement.MaterialFragment
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private var currentFragment: Fragment? = null
    private val cafeFragment = CafeFragment()
    private val materialFragment = MaterialFragment()
    private var lastFragmentId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (lastFragmentId == item.itemId) {
                return@OnNavigationItemSelectedListener true
            }
            lastFragmentId = item.itemId
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.menu_baike) {
            startActivity<BaikeActivity>()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
