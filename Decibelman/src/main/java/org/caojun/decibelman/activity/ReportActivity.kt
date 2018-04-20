package org.caojun.decibelman.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_report.*
import org.caojun.decibelman.R
import org.caojun.utils.ActivityUtils
import org.caojun.utils.ImageUtils

class ReportActivity: AppCompatActivity() {

    companion object {
        val Key_Picture_Path = "Key_Picture_Path"
        val Key_Report_Content = "Key_Report_Content"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val path = intent.getStringExtra(Key_Picture_Path)
        val bitmap = ImageUtils.toBitmap(path)
        ivPicture.setImageBitmap(bitmap)

        val report = intent.getStringExtra(Key_Report_Content)
        stvReport.text = report
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.report, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_report -> {
                //拍照举报
                val bitmap = ImageUtils.toBitmap(root)
                val file = ImageUtils.toFile(this, bitmap)
                ActivityUtils.shareImage(this, getString(R.string.menu_report), file)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}