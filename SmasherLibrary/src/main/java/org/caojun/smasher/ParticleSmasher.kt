package org.caojun.smasher

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.Window
import java.util.ArrayList

class ParticleSmasher: View {

    private val mAnimators = ArrayList<SmashAnimator>()
    private var mCanvas: Canvas? = null
    private val mActivity: Activity

    constructor(activity: Activity): super(activity as Context) {
        this.mActivity = activity
        addView2Window(activity)
        init()
    }

    /**
     * 添加View到当前界面
     */
    private fun addView2Window(activity: Activity) {
        val rootView = activity.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
        // 需要足够的空间展现动画，因此这里使用的是充满父布局
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        rootView.addView(this, layoutParams)
    }

    private fun init() {
        mCanvas = Canvas()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (animator in mAnimators) {
            animator.draw(canvas)
        }
    }

    fun with(view: View): SmashAnimator {
        // 每次都新建一个单独的SmashAnimator对象
        val animator = SmashAnimator(this, view)
        mAnimators.add(animator)
        return animator
    }


    /**
     * 获取View的Rect，并去掉状态栏、toolbar高度
     * @param view    来源View
     * @return        获取到的Rect
     */
    fun getViewRect(view: View): Rect {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)

        val location = IntArray(2)
        getLocationOnScreen(location)

        rect.offset(-location[0], -location[1])
        return rect
    }

    /**
     * 获取View的Bitmap
     * @param view     来源View
     * @return         获取到的图片
     */
    fun createBitmapFromView(view: View): Bitmap? {

        view.clearFocus()
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        if (bitmap != null) {
            synchronized(mCanvas!!) {
                val canvas = mCanvas
                canvas!!.setBitmap(bitmap)
                view.draw(canvas)
                canvas.setBitmap(null)
            }
        }
        return bitmap
    }

    /**
     * 移除动画
     * @param animator  需要移除的动画
     */
    fun removeAnimator(animator: SmashAnimator) {
        if (mAnimators.contains(animator)) {
            mAnimators.remove(animator)
        }
    }

    /**
     * 清除所有动画
     */
    fun clear() {
        mAnimators.clear()
        invalidate()
    }

    /**
     * 让View重新显示
     * @param view      已经隐藏的View
     */
    fun reShowView(view: View) {
        view.animate().setDuration(100).setStartDelay(0).scaleX(1f).scaleY(1f).translationX(0f).translationY(0f).alpha(1f).start()
    }
}