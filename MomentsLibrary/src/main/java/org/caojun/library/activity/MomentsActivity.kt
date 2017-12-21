package org.caojun.library.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.view.View
import com.hys.utils.*
import com.socks.library.KLog
import org.caojun.library.MultiImageSelector
import kotlinx.android.synthetic.main.activity_moments.*
import org.caojun.library.Constant
import org.caojun.library.MultiImageSelectorActivity
import org.caojun.library.R
import org.caojun.library.adapter.ImageAdapter
import org.caojun.library.listener.DragCallback
import org.caojun.library.listener.OnRecyclerItemClickListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.ArrayList

/**
 * Created by CaoJun on 2017-12-20.
 */
class MomentsActivity : AppCompatActivity() {

    companion object {
        val Key_Title = "Key_Title"
        val FILE_DIR_NAME = "org.caojun.library"//应用缓存地址
        val FILE_IMG_NAME = "images"//放置图片缓存
        val IMAGE_SIZE = 9//可添加图片最大数
        private val REQUEST_IMAGE = 1002
    }

    private var originImages = ArrayList<String>()//原始图片
    private var dragImages = ArrayList<String>()//压缩长宽后图片
    private var mContext: Context? = null
    private var postArticleImgAdapter: ImageAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null
//    private var rcvImg: RecyclerView? = null
//    private var tv: TextView? = null//删除区域提示

    fun startPostActivity(context: Context, images: ArrayList<String>) {
        val intent = Intent(context, MomentsActivity::class.java)
        intent.putStringArrayListExtra("img", images)
        context.startActivity(intent)
    }

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
        val list = intent.getStringArrayListExtra("img")
        if (list != null && list.isNotEmpty()) {
            originImages.addAll(list)
        }
        mContext = applicationContext
        InitCacheFileUtils.initImgDir(FILE_DIR_NAME, FILE_IMG_NAME)//清除图片缓存
        //添加按钮图片资源
        val plusPath = Constant.PlusIconPrefix + this.packageName + "/drawable/" + R.drawable.mine_btn_plus
//        dragImages = ArrayList()
        originImages.add(plusPath)//添加按键，超过9张时在adapter中隐藏
        dragImages.addAll(originImages)
//        Thread(MyRunnable(this, dragImages, originImages, dragImages, myHandler, false)).start()//开启线程，在新线程中去压缩图片
        doRefreshImages(false, dragImages)
    }

//    private fun initView() {
//        rcvImg = findViewById(R.id.rcv_img) as RecyclerView
//        tv = findViewById(R.id.tv) as TextView
//        initRcv()
//    }

    private fun initRcv() {

        postArticleImgAdapter = ImageAdapter(mContext, dragImages)
        recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = postArticleImgAdapter
        val myCallBack = DragCallback(this, postArticleImgAdapter, dragImages, originImages)
        itemTouchHelper = ItemTouchHelper(myCallBack)
        itemTouchHelper?.attachToRecyclerView(recyclerView)//绑定RecyclerView

        //事件监听
        recyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(recyclerView) {

            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                if (originImages[vh.adapterPosition].contains(Constant.PlusIconPrefix)) {//打开相册
                    MultiImageSelector.create()
                            .showCamera(true)
                            .count(IMAGE_SIZE - originImages.size + 1)
                            .multi()
                            .start(this@MomentsActivity, REQUEST_IMAGE)
                } else {
//                    ToastUtils.getInstance().show(MyApplication.getInstance().getContext(), "预览图片")
                    //TODO 预览图片
                }
            }

            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {
                //如果item不是最后一个，则执行拖拽
                if (vh.layoutPosition != dragImages.size - 1) {
                    itemTouchHelper!!.startDrag(vh)
                }
            }
        })

        myCallBack.setDragListener(object : DragCallback.DragListener {
            override fun deleteState(delete: Boolean) {
                KLog.d("myCallBack", "deleteState: " + delete)
                if (delete) {
                    tvDelete.setBackgroundResource(android.R.color.holo_red_dark)
                    tvDelete.text = resources.getString(R.string.post_delete_tv_s)
                } else {
                    tvDelete.text = resources.getString(R.string.post_delete_tv_d)
                    tvDelete.setBackgroundResource(android.R.color.holo_red_light)
                }
            }

            override fun dragState(start: Boolean) {
                KLog.d("myCallBack", "dragState: " + start)
                if (start) {
                    tvDelete.visibility = View.VISIBLE
                } else {
                    tvDelete.visibility = View.GONE
                }
            }
        })
    }

    //------------------图片相关-----------------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {//从相册选择完图片
            //压缩图片
//            Thread(MyRunnable(this, data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT),
//                    originImages, dragImages, myHandler, true)).start()
            doRefreshImages(true, data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT))
        }
    }

//    /**
//     * 另起线程压缩图片
//     */
//    internal class MyRunnable(var context: Context, var images: ArrayList<String>, var originImages: ArrayList<String>, var dragImages: ArrayList<String>, var handler: Handler, var add: Boolean//是否为添加图片
//    ) : Runnable {
//
//        override fun run() {
//            val sdcardUtils = SdcardUtils()
//            var filePath: String
//            var newBitmap: Bitmap
//            var addIndex = originImages.size - 1
//            for (i in images.indices) {
//                if (images[i].contains(Constant.PlusIconPrefix)) {//说明是添加图片按钮
//                    continue
//                }
//                //压缩
//                newBitmap = ImageUtils.compressScaleByWH(images[i],
//                        DensityUtils.dp2px(100f),
//                        DensityUtils.dp2px(100f))
//                //文件地址
//                filePath = (sdcardUtils.getSDPATH() + FILE_DIR_NAME + "/"
//                        + FILE_IMG_NAME + "/" + String.format("img_%d.jpg", System.currentTimeMillis()))
//                //保存图片
//                ImageUtils.save(newBitmap, filePath, Bitmap.CompressFormat.JPEG, true)
//                //设置值
//                if (!add) {
//                    images[i] = filePath
//                } else {//添加图片，要更新
//                    dragImages.add(addIndex, filePath)
//                    originImages.add(addIndex++, filePath)
//                }
//            }
//            val message = Message()
//            message.what = 1
//            handler.sendMessage(message)
//            KLog.e("MyRunnable", "run")
//        }
//    }
//
//    private val myHandler = MyHandler(this)
//
//    private class MyHandler(activity: Activity) : Handler() {
//        private val reference: WeakReference<Activity>
//
//        init {
//            reference = WeakReference(activity)
//        }
//
//        override fun handleMessage(msg: Message) {
//            val activity = reference.get() as MomentsActivity
//            if (activity != null) {
//                when (msg.what) {
//                    1 -> {
//                        activity.postArticleImgAdapter?.setData(dragImages)
//                        activity.postArticleImgAdapter?.notifyDataSetChanged()
//                    }
//                }
//            }
//        }
//    }

    private fun doRefreshImages(add: Boolean, images: ArrayList<String>) {
        doAsync {
            val sdcardUtils = SdcardUtils()
            var filePath: String
            var newBitmap: Bitmap
            var addIndex = originImages.size - 1
            for (i in images.indices) {
                if (images[i].contains(Constant.PlusIconPrefix)) {//说明是添加图片按钮
                    continue
                }
                //压缩
                newBitmap = ImageUtils.compressScaleByWH(images[i],
                        DensityUtils.dp2px(100f),
                        DensityUtils.dp2px(100f))
                //文件地址
                filePath = (sdcardUtils.getSDPATH() + FILE_DIR_NAME + "/"
                        + FILE_IMG_NAME + "/" + String.format("img_%d.jpg", System.currentTimeMillis()))
                //保存图片
                ImageUtils.save(newBitmap, filePath, Bitmap.CompressFormat.JPEG, true)
                //设置值
                if (!add) {
                    images[i] = filePath
                } else {//添加图片，要更新
                    dragImages.add(addIndex, filePath)
                    originImages.add(addIndex++, filePath)
                }
            }
            uiThread {
//                postArticleImgAdapter?.setData(dragImages)
                postArticleImgAdapter?.notifyDataSetChanged()
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        myHandler.removeCallbacksAndMessages(null)
//    }
}