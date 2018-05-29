package org.caojun.mapman

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.caojun.contacts.Contact
import org.caojun.contacts.ContactsView
import org.caojun.svgmap.PathItem
import org.caojun.svgmap.SvgMapView
import org.caojun.utils.ActivityUtils

class MainActivity : AppCompatActivity()/*, NavigationView.OnNavigationItemSelectedListener*/ {

    val Key_Map_Name = "Key_Map_Name"
    private var lastId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val list = ArrayList<Contact>()

        svgMapView.setGestureSupport(false)
        svgMapView.setMapListener(object : SvgMapView.MapListener {

            override fun onLongClick(item: PathItem?, index: Int) {
                svgMapView.doAnimateCenter(index)
            }

            override fun onClick(item: PathItem?, index: Int) {

                if (item == null || TextUtils.isEmpty(item.id) || !svgMapView.hasMap(item.id)) {
                    lastId = ""
                    return
                }
                if (lastId != item.id) {
                    lastId = item.id
                    return
                }

                val intent = Intent(this@MainActivity, MainActivity::class.java)
                intent.putExtra(Key_Map_Name, item.id)
                startActivity(intent)
            }

            override fun onShow(item: PathItem, index: Int, size: Int) {
                var id = ActivityUtils.getStringResId(this@MainActivity, item.id)
                var contact: Contact
                contact = if (id == null) {
                    Contact(index, item.title, item.title,null)
                } else {
                    Contact(index, item.title, getString(id), null)
                }

                list.add(contact)

                if (index == size - 1) {
                    contactView.init(list, object : ContactsView.OnSelectChangedListener {
                        override fun onSelectChanged(index: Int, isChecked: Boolean) {
                            svgMapView.setSelected(index, isChecked, true)
                            if (isChecked) {
                                svgMapView.doAnimateCenter(index)
                            } else {
                                svgMapView.doAnimateCenter(-1)
                            }
                            drawer_layout.closeDrawer(GravityCompat.START)
                        }
                    })
                }
            }
        })

        val mapName = intent.getStringExtra(Key_Map_Name)
        if (TextUtils.isEmpty(mapName)) {
            svgMapView.setMap("cn")
        } else {
            svgMapView.setMap(mapName)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_camera -> {
//                // Handle the camera action
//            }
//            R.id.nav_gallery -> {
//
//            }
//            R.id.nav_slideshow -> {
//
//            }
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
//            R.id.nav_send -> {
//
//            }
//        }
//
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }
}
