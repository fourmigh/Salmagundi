package org.caojun.library.listener

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.socks.library.KLog
import org.caojun.library.R
import org.caojun.library.adapter.ImageAdapter
import java.util.*

/**
 * Created by CaoJun on 2017-12-20.
 */
class DragCallback : ItemTouchHelper.Callback {

    private var dragFlags: Int = 0
    private var swipeFlags: Int = 0
    private var adapter: ImageAdapter? = null
//    private var images: ArrayList<String>? = null//图片经过压缩处理
//    private var originImages: ArrayList<String>? = null//图片没有经过处理，这里传这个进来是为了使原图片的顺序与拖拽顺序保持一致
    private var up: Boolean = false//手指抬起标记位
    private var heightDelete = 0

    constructor(context: Context, adapter: ImageAdapter?/*, images: ArrayList<String>, originImages: ArrayList<String>*/) {
        this.adapter = adapter
//        this.images = images
//        this.originImages = originImages
        heightDelete = context.resources.getDimensionPixelSize(R.dimen.article_post_delete)
    }

    /**
     * 设置item是否处理拖拽事件和滑动事件，以及拖拽和滑动操作的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        //判断 recyclerView的布局管理器数据
        if (recyclerView.layoutManager is StaggeredGridLayoutManager) {//设置能拖拽的方向
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            swipeFlags = 0//0则不响应事件
        }
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * 当用户从item原来的位置拖动可以拖动的item到新位置的过程中调用
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val fromPosition = viewHolder.adapterPosition//得到item原来的position
        val toPosition = target.adapterPosition//得到目标position
        KLog.d("onMove", "fromPosition: " + fromPosition)
        KLog.d("onMove", "toPosition: " + toPosition)
        val size = (adapter?.itemCount?:0) - 1
        KLog.d("onMove", "size: " + size)
        if (toPosition == size || size == fromPosition) {
            return true
        }
        val images = adapter?.getData()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                KLog.d("onMove", "i: " + i)
                Collections.swap(images, i, i + 1)
//                Collections.swap(originImages, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(images, i, i - 1)
//                Collections.swap(originImages, i, i - 1)
            }
        }
        adapter?.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    /**
     * 设置是否支持长按拖拽
     *
     * @return
     */
    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    /**
     *
     * @param viewHolder
     * @param direction
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     *
     * @param recyclerView
     * @param viewHolder
     */
    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        adapter?.notifyDataSetChanged()
        initData()
    }

    /**
     * 重置
     */
    private fun initData() {
        dragListener?.deleteState(false)
        dragListener?.dragState(false)
        up = false
    }

    /**
     * 自定义拖动与滑动交互
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (null == dragListener) {
            return
        }
        if (dY >= (recyclerView.height - viewHolder.itemView.bottom - heightDelete)) {//拖到删除处
            dragListener?.deleteState(true)
            if (up) {//在删除处放手，则删除item
                viewHolder.itemView.visibility = View.INVISIBLE//先设置不可见，如果不设置的话，会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHolder删除
//                originImages?.removeAt(viewHolder.adapterPosition)
//                images?.removeAt(viewHolder.adapterPosition)
                val images = adapter?.getData()
                images?.removeAt(viewHolder.adapterPosition)
                adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                initData()
                return
            }
        } else {//没有到删除处
            if (View.INVISIBLE == viewHolder.itemView.visibility) {//如果viewHolder不可见，则表示用户放手，重置删除区域状态
                dragListener?.dragState(false)
            }
            dragListener?.deleteState(false)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     *
     * @param viewHolder
     * @param actionState
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        KLog.d("onSelectedChanged", "actionState: " + actionState)
        if (ItemTouchHelper.ACTION_STATE_DRAG == actionState) {
            dragListener?.dragState(true)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 设置手指离开后ViewHolder的动画时间，在用户手指离开后调用
     *
     * @param recyclerView
     * @param animationType
     * @param animateDx
     * @param animateDy
     * @return
     */
    override fun getAnimationDuration(recyclerView: RecyclerView, animationType: Int, animateDx: Float, animateDy: Float): Long {
        //手指放开
        up = true
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
    }

    internal interface DragListener {
        /**
         * 用户是否将 item拖动到删除处，根据状态改变颜色
         *
         * @param delete
         */
        fun deleteState(delete: Boolean)

        /**
         * 是否于拖拽状态
         *
         * @param start
         */
        fun dragState(start: Boolean)
    }

    private var dragListener: DragListener? = null

    internal fun setDragListener(dragListener: DragListener) {
        this.dragListener = dragListener
    }
}