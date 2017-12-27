package org.caojun.library.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.hys.utils.*
import org.caojun.library.MultiImageSelector
import kotlinx.android.synthetic.main.activity_moments.*
import org.caojun.library.MultiImageSelectorActivity
import org.caojun.library.R
import org.caojun.library.adapter.ImageAdapter
import org.caojun.library.listener.DragCallback
import org.caojun.library.listener.OnRecyclerItemClickListener
import java.util.ArrayList

/**
 * Created by CaoJun on 2017-12-20.
 */
class MomentsActivity : AppCompatActivity() {

    companion object {
        val Key_Title = "Key_Title"
        val Key_ImagePaths = "Key_ImagePaths"
        val Key_Content = "Key_Content"
        val Key_Hint = "Key_Hint"
        private val REQUEST_IMAGE = 1002
    }

    private var originImages = ArrayList<String>()//原始图片
    private var adapter: ImageAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moments)

        Utils.init(this)

        initData()
        initRcv()
    }

    private fun initData() {
        val title = intent.getStringExtra(Key_Title)
        if (!TextUtils.isEmpty(title)) {
            this.title = title
        }
        val list = intent.getStringArrayListExtra(Key_ImagePaths)
        if (list != null) {
            originImages.addAll(list)
        }
        val content = intent.getStringExtra(Key_Content)
        etContent.setText(content)
        val hint = intent.getStringExtra(Key_Hint)
        etContent.hint = hint
    }

    private fun initRcv() {

        adapter = ImageAdapter(this, originImages)
        recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        val myCallBack = DragCallback(this, adapter/*, dragImages, originImages*/)
        itemTouchHelper = ItemTouchHelper(myCallBack)
        itemTouchHelper?.attachToRecyclerView(recyclerView)//绑定RecyclerView

        //事件监听
        recyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(recyclerView) {

            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                if (vh.adapterPosition == originImages.size) {
                    MultiImageSelector.create()
                            .showCamera(true)
                            .origin(originImages)
                            .multi()
                            .start(this@MomentsActivity, REQUEST_IMAGE)
                }
            }

            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {
                //如果item不是最后一个，则执行拖拽
                if (vh.layoutPosition != originImages.size) {
                    itemTouchHelper!!.startDrag(vh)
                }
            }
        })

        myCallBack.setDragListener(object : DragCallback.DragListener {
            override fun deleteState(delete: Boolean) {
                if (delete) {
                    tvDelete.setBackgroundResource(android.R.color.holo_red_dark)
                    tvDelete.text = resources.getString(R.string.post_delete_tv_s)
                } else {
                    tvDelete.text = resources.getString(R.string.post_delete_tv_d)
                    tvDelete.setBackgroundResource(android.R.color.holo_red_light)
                }
            }

            override fun dragState(start: Boolean) {
                if (start) {
                    tvDelete.visibility = View.VISIBLE
                } else {
                    tvDelete.visibility = View.GONE
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {//从相册选择完图片
            if (data != null) {
                originImages = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
                adapter?.setData(originImages)
                adapter?.notifyDataSetChanged()
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ok -> {
                doOK()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doOK() {
        val content = etContent.text.toString()
        val size = adapter?.itemCount?:0
        val intent = Intent()
        if (!TextUtils.isEmpty(content) || size > 0) {
            intent.putExtra(Key_Content, content)
            intent.putStringArrayListExtra(Key_ImagePaths, adapter?.getData())
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }
}