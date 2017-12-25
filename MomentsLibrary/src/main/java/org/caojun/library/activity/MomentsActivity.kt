package org.caojun.library.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
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
        val Key_Images = "Key_Images"
        private val REQUEST_IMAGE = 1002
    }

    private var originImages = ArrayList<String>()//原始图片
    private var mContext: Context? = null
    private var adapter: ImageAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moments)

        Utils.init(this)

        val title = intent.getStringExtra(Key_Title)
        if (!TextUtils.isEmpty(title)) {
            this.title = title
        }

        initData()
        initRcv()
    }

    private fun initData() {
        val list = intent.getStringArrayListExtra(Key_Images)
        if (list != null) {
            originImages.addAll(list)
        }
        mContext = applicationContext
    }

    private fun initRcv() {

        adapter = ImageAdapter(mContext, originImages)
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
                } else {
                    //TODO 预览图片
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

    //------------------图片相关-----------------------------

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

//    private fun doRefreshImages(add: Boolean, images: ArrayList<String>) {
//        doAsync {
////            val sdcardUtils = SdcardUtils()
////            var filePath: String
////            var newBitmap: Bitmap
////            var addIndex = originImages.size - 1
////            for (i in images.indices) {
////                if (images[i].contains(PlusIconPrefix)) {//说明是添加图片按钮
////                    continue
////                }
////                //压缩
////                newBitmap = ImageUtils.compressScaleByWH(images[i],
////                        DensityUtils.dp2px(100f),
////                        DensityUtils.dp2px(100f))
////                //文件地址
////                filePath = (sdcardUtils.getSDPATH() + FILE_DIR_NAME + "/"
////                        + FILE_IMG_NAME + "/" + String.format("img_%d.jpg", System.currentTimeMillis()))
////                //保存图片
////                ImageUtils.save(newBitmap, filePath, Bitmap.CompressFormat.JPEG, true)
////                //设置值
////                if (!add) {
////                    images[i] = filePath
////                } else {//添加图片，要更新
////                    dragImages.add(addIndex++, filePath)
//////                    originImages.add(addIndex++, filePath)
////                }
////            }
//            uiThread {
//                adapter?.notifyDataSetChanged()
//            }
//        }
//    }
}