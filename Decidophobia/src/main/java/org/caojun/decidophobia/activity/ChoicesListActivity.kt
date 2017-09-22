package org.caojun.decidophobia.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import kotlinx.android.synthetic.main.activity_list.*
import org.caojun.decidophobia.R
import org.caojun.decidophobia.adapter.ExpandableListAdapter
import org.caojun.decidophobia.ormlite.Options
import org.caojun.decidophobia.utils.OptionsUtils
import org.jetbrains.anko.toast

/**
 * Created by CaoJun on 2017/9/20.
 */
class ChoicesListActivity: AppCompatActivity() {

    private var list: List<Options>? = null
    private var adapter: ExpandableListAdapter? = null
    private var countLongClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_list)

        list = OptionsUtils.query(this)
        adapter = ExpandableListAdapter(this, list!!)
        expandableListView?.setAdapter(adapter)

        registerForContextMenu(expandableListView)

        btnDelete?.setOnClickListener {
            toast(R.string.tips_delete)
            countLongClick = 1
        }

        btnDelete?.setOnLongClickListener {
            countLongClick ++
            when (countLongClick) {
                1 -> toast(R.string.tips_delete)
                2 -> toast(R.string.tips_delete2)
                3 -> {
                    toast(R.string.tips_delete3)
                    btnDelete?.isEnabled = false
                }
            }
            true
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(0, 0, 0, R.string.delete)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val info = item?.menuInfo as ExpandableListView.ExpandableListContextMenuInfo
        val groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition)
        if (OptionsUtils.delete(this, list!![groupPos])) {
            list = OptionsUtils.query(this)
            adapter?.setData(list)
            adapter?.notifyDataSetChanged()
            return true
        }
        return super.onContextItemSelected(item)
    }
}