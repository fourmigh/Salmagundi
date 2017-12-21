package org.caojun.library.listener

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import org.caojun.library.R
import org.caojun.library.adapter.ImageAdapter
import java.util.*

/**
 * Created by CaoJun on 2017-12-20.
 */
class DragCallback : ItemTouchHelper.Callback {

    private var context: Context? = null
    private var dragFlags: Int = 0
    private var swipeFlags: Int = 0
    private var adapter: ImageAdapter? = null
    private val images = ArrayList<String>()//图片经过压缩处理
    private val originImages = ArrayList<String>()//图片没有经过处理，这里传这个进来是为了使原图片的顺序与拖拽顺序保持一致
    private var up: Boolean = false//手指抬起标记位

    private var dragListener: DragListener? = null

    interface DragListener {
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

    fun setDragListener(dragListener: DragListener?) {
        this.dragListener = dragListener
    }

    constructor(context: Context, adapter: ImageAdapter?, images: List<String>) : super() {
        this.context = context
        this.adapter = adapter
        this.images.addAll(images)
        this.originImages.addAll(images)
    }

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        if (recyclerView?.layoutManager is StaggeredGridLayoutManager) {//设置能拖拽的方向
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            swipeFlags = 0//0则不响应事件
        }
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        val fromPosition = viewHolder?.adapterPosition ?: 0//得到item原来的position
        val toPosition = target?.adapterPosition ?: 0//得到目标position
        if (toPosition == images.size - 1 || images.size - 1 == fromPosition) {
            return true
        }
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(images, i, i + 1)
                Collections.swap(originImages, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(images, i, i - 1)
                Collections.swap(originImages, i, i - 1)
            }
        }
        adapter?.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)
        adapter?.notifyDataSetChanged()
        initData()
    }

    override fun getAnimationDuration(recyclerView: RecyclerView, animationType: Int, animateDx: Float, animateDy: Float): Long {
        //手指放开
        up = true
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && dragListener != null) {
            dragListener?.dragState(true)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (null == dragListener) {
            return
        }

        if (dY >= (recyclerView.height
                - viewHolder.itemView.bottom//item底部距离recyclerView顶部高度
                - context!!.resources.getDimensionPixelSize(R.dimen.article_post_delete))) {//拖到删除处
            dragListener?.deleteState(true)
            if (up) {//在删除处放手，则删除item
                viewHolder.itemView.visibility = View.INVISIBLE//先设置不可见，如果不设置的话，会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHolder删除
                originImages.removeAt(viewHolder.adapterPosition)
                images.removeAt(viewHolder.adapterPosition)
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

    private fun initData() {
        dragListener?.deleteState(false)
        dragListener?.dragState(false)
        up = false
    }
}