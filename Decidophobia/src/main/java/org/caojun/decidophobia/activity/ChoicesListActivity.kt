package org.caojun.decidophobia.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ExpandableListView
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_list.*
import org.caojun.decidophobia.R
import org.caojun.decidophobia.adapter.ExpandableListAdapter
import org.caojun.decidophobia.bmob.BOptions
import org.caojun.decidophobia.ormlite.Options
import org.caojun.decidophobia.utils.OptionsUtils
import org.jetbrains.anko.doAsync
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
        menu?.add(0, 1, 1, R.string.share)
        menu?.add(0, 2, 2, R.string.upload)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val info = item?.menuInfo as ExpandableListView.ExpandableListContextMenuInfo
        val groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition)
        when (item?.itemId) {
            0 -> {
                if (OptionsUtils.delete(this, list!![groupPos])) {
                    list = OptionsUtils.query(this)
                    adapter?.setData(list)
                    adapter?.notifyDataSetChanged()
                    return true
                }
            }
            1 -> {
                var text = StringBuffer(list!![groupPos].title)
                for (i in 0 until list!![groupPos].option.size) {
                    text.append("\n" + getString(R.string.no_n, (i + 1).toString(), list!![groupPos].option[i]))
                }
                text.append("\n\n" + getString(R.string.which_one_to_choose))
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, text.toString())
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
                return true
            }
            2 -> {
                doAsync {
                    val bOptions = BOptions(list!![groupPos])
                    bOptions.save(object : SaveListener<String>() {

                        override fun done(o: String, e: BmobException?) {
                            if (e == null) {
                                toast(R.string.upload_ok)
                            } else {
                                toast(getString(R.string.upload_error, e.toString()))
                            }
                        }
                    })
                }
            }
        }
        return super.onContextItemSelected(item)
    }
}