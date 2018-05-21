package org.caojun.svgmap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Element
import java.io.InputStream
import java.util.ArrayList
import javax.xml.parsers.DocumentBuilderFactory

class SvgMapView: View {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //手势监听器
    private var mDetector: GestureDetector? = null
    //缩放系数
    private var scale = 1.3f
    private var preScale = scale// 默认前一次缩放比例为1
    //保存path对象
    private val pathItems = ArrayList<PathItem>()

    private var mScaleGestureDetector: ScaleGestureDetector? = null

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        mDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                doClick(e)
                return super.onSingleTapUp(e)
            }

            override fun onLongPress(e: MotionEvent) {
                doCenter(e)
                super.onLongPress(e)
            }

            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                doScroll(distanceX, distanceY)
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        })
        mScaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.OnScaleGestureListener {

            override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
                val previousSpan = scaleGestureDetector.previousSpan
                val currentSpan = scaleGestureDetector.currentSpan
                scale = preScale + (currentSpan - previousSpan) / 1000
                invalidate()
                return false
            }

            override fun onScaleBegin(scaleGestureDetector: ScaleGestureDetector): Boolean {
                return true
            }

            override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
                preScale = scale//记录本次缩放比例
            }
        })
    }

    private var dX = 0f
    private var dY = 0f
    private var lastX = 0f
    private var lastY = 0f

    interface MapListener {
        fun onClick(item: PathItem)
        fun onShow(item: PathItem, index: Int, size: Int)
    }

    private var listener: MapListener? = null

    fun setMapListener(listener: MapListener) {
        this.listener = listener
    }

    private var inputStream: InputStream? = null

    fun hasMap(mapName: String): Boolean {
        return try {
            var name = mapName
            if (name.toUpperCase() == "DO") {
                name = "DODO"
            }
            //打开输入流
            val resId = resources.getIdentifier(name.toLowerCase(), "raw", context.packageName)
            inputStream = resources.openRawResource(resId)
            true
        } catch (e: Exception) {
            false
        }

    }

    /**
     * 解析path
     */
    fun setMap(mapName: String) {

        doAsync {
            if (inputStream == null) {
                hasMap(mapName)
            }
            if (inputStream == null) {
                return@doAsync
            }
            try {
                // 创建DOM工厂对象
                val dbf = DocumentBuilderFactory.newInstance()
                val db = dbf.newDocumentBuilder()
                // 获取文档对象
                val doc = db.parse(inputStream!!)
                //获取path元素节点集合
                val paths = doc.getElementsByTagName("path")
                var item: PathItem

                pathItems.clear()
                rectF.left = java.lang.Float.MAX_VALUE
                rectF.top = java.lang.Float.MAX_VALUE
                rectF.right = 0f
                rectF.bottom = 0f

                for (i in 0 until paths.length) {
                    // 取出每一个元素
                    val personNode = paths.item(i) as Element
                    //得到d属性值
                    val nodeValue = personNode.getAttribute("d")
                    val path = PathParser.createPathFromPathData(nodeValue) ?: continue
                    val title = personNode.getAttribute("title")
                    val id = personNode.getAttribute("id")
                    //解析，并创建pathItem
                    item = PathItem(id, title, path)
                    item.isSelected = true
                    pathItems.add(item)

                    //统计整体尺寸
                    val rf = item.getRectF()
                    if (rf.left < rectF.left) {
                        rectF.left = rf.left
                    }
                    if (rf.top < rectF.top) {
                        rectF.top = rf.top
                    }
                    if (rf.right > rectF.right) {
                        rectF.right = rf.right
                    }
                    if (rf.bottom > rectF.bottom) {
                        rectF.bottom = rf.bottom
                    }

                    postInvalidate()
                    Thread.sleep(100)
                    item.isSelected = false

                    uiThread {
                        listener?.onShow(item, i, paths.length)
                    }
                }
                postInvalidate()
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mScaleGestureDetector!!.onTouchEvent(event) && mDetector!!.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.scale(scale, scale)
        canvas.translate(dX, dY)
        try {
            for (pathItem in pathItems) {
                pathItem.draw(canvas, mPaint)
            }
        } catch (e: Exception) {
        }

        canvas.restore()
    }

    private var rectF = RectF()

    private fun center(rectF: RectF) {

        val Width = right - left
        val Height = bottom - top
        var width = rectF.right - rectF.left
        var height = rectF.bottom - rectF.top
        val sX = Width / width
        val sY = Height / height
        scale = Math.min(sX, sY)
        preScale = scale

        width *= scale
        height *= scale

        dX = (Width - width) / (2 * scale)
        dY = (Height - height) / (2 * scale)

        dX -= rectF.left
        dY -= rectF.top

        lastX = -dX
        lastY = -dY
    }

    private fun doCenter(e: MotionEvent) {
        var x = e.x / scale
        var y = e.y / scale
        x -= dX
        y -= dY
        try {
            var rf: RectF? = null
            for (pathItem in pathItems) {
                pathItem.isSelected = pathItem.isTouch(x.toInt(), y.toInt())

                if (pathItem.isSelected) {
                    rf = pathItem.getRectF()
                }
            }
            if (rf == null) {
                center(rectF)
            } else {
                center(rf)
            }
        } catch (e1: Exception) {
        }

        invalidate()
    }

    private fun doClick(e: MotionEvent) {
        var x = e.x / scale
        var y = e.y / scale
        x -= dX
        y -= dY
        var item: PathItem? = null
        try {
            for (pathItem in pathItems) {
                pathItem.isSelected = pathItem.isTouch(x.toInt(), y.toInt())

                if (pathItem.isSelected) {
                    item = pathItem
                }
            }
            invalidate()
        } catch (e1: Exception) {
        }

        if (listener != null && item != null) {
            listener?.onClick(item)
        }
    }

    private fun doScroll(distanceX: Float, distanceY: Float) {
        val s = Math.abs(scale)
        dX = lastX + distanceX / s
        dY = lastY + distanceY / s
        lastX = dX
        lastY = dY
        dX = -dX
        dY = -dY
        invalidate()
    }
}