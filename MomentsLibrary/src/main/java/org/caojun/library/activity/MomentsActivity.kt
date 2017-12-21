package org.caojun.library.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import org.caojun.library.MultiImageSelector
import kotlinx.android.synthetic.main.activity_moments.*
import org.caojun.library.Constant
import org.caojun.library.MultiImageSelectorActivity
import org.caojun.library.R
import org.caojun.library.adapter.ImageAdapter
import org.caojun.library.listener.DragCallback
import org.caojun.library.listener.OnRecyclerItemClickListener
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.util.ArrayList

/**
 * Created by CaoJun on 2017-12-20.
 */
class MomentsActivity : AppCompatActivity() {

    companion object {
        private val REQUEST_IMAGE = 2
        private val REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101
    }
    private val mSelectPath = ArrayList<String>()//原始图片
    private var adapter: ImageAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moments)
//        initData()
//        initView()
        pickImage()
    }

//    private fun initData() {
//        val list = intent.getStringArrayListExtra("img")
//        originImages.clear()
//        if (list.isNotEmpty()) {
//            originImages.addAll(list)
//        }
//    }

    private fun initView() {
        adapter = ImageAdapter(this, mSelectPath)
        recyclerView.adapter = adapter
        val callback = DragCallback(this, adapter, mSelectPath)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper?.attachToRecyclerView(recyclerView)//绑定RecyclerView

        //事件监听
        recyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(recyclerView) {

            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                if (mSelectPath[vh.adapterPosition].contains(Constant.PlusIconPrefix)) {//打开相册
                    pickImage()
                } else {
                    //TODO 预览图片
                }
            }

            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {
                //如果item不是最后一个，则执行拖拽
                if (vh.layoutPosition != mSelectPath.size - 1) {
                    itemTouchHelper?.startDrag(vh)
                }
            }
        })

        callback.setDragListener(object : DragCallback.DragListener {
            override fun deleteState(delete: Boolean) {
                if (delete) {
                    tvDelete.setBackgroundResource(android.R.color.holo_red_dark)
                    tvDelete.setText(R.string.post_delete_tv_s)
                } else {
                    tvDelete.setText(R.string.post_delete_tv_d)
                    tvDelete.setBackgroundResource(android.R.color.holo_red_light)
                }
            }

            override fun dragState(start: Boolean) {
                tvDelete.visibility = if (start) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            val list = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT)
            mSelectPath.clear()
            if (list.isNotEmpty()) {
                mSelectPath.addAll(list)
            }
            initView()
        }
    }

    private fun pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION)
        } else {
            val showCamera = true
            var maxNum = MultiImageSelectorActivity.DEFAULT_IMAGE_SIZE - mSelectPath.size + 1
            val selector = MultiImageSelector.create()
            selector.showCamera(showCamera)
            selector.count(maxNum)
            selector.multi()
            selector.origin(mSelectPath)
            selector.start(this, REQUEST_IMAGE)
        }
    }

    private fun requestPermission(permission: String, rationale: String, requestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            alert(rationale) {
                title = getString(R.string.mis_permission_dialog_title)
                yesButton {
                    ActivityCompat.requestPermissions(this@MomentsActivity, arrayOf(permission), requestCode)
                }
                noButton {}
            }.show()
//            AlertDialog.Builder(this)
//                    .setTitle(R.string.mis_permission_dialog_title)
//                    .setMessage(rationale)
//                    .setPositiveButton(R.string.mis_permission_dialog_ok) { dialog, which -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode) }
//                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
//                    .create().show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }
}