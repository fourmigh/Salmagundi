package org.caojun.decidophobia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_cloud.*
import org.caojun.decidophobia.R
import org.caojun.decidophobia.adapter.CloudAdapter
import org.caojun.decidophobia.bmob.BOptions
import java.util.ArrayList

/**
 * Created by CaoJun on 2017/10/20.
 */
class CloudChoicesListActivity: AppCompatActivity() {

    companion object {
        val list: ArrayList<BOptions> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_cloud)

        val adapter = CloudAdapter(this, list)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val options = adapter.getItem(i) as BOptions
            val intent = Intent()
            intent.putExtra("options", options.getOptions())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}