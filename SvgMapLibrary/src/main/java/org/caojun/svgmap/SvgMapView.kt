package org.caojun.svgmap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Element
import java.io.InputStream
import java.util.ArrayList
import javax.xml.parsers.DocumentBuilderFactory

class SvgMapView: ScaleCanvasView {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //保存path对象
    private val pathItems = ArrayList<PathItem>()

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        this.setGestureDetector(GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                doClick(e, false)
                invalidate()
                return super.onSingleTapUp(e)
            }

            override fun onLongPress(e: MotionEvent) {
//                doClick(e, true)
                doCenter(doClick(e, true))
//                doAnimateCenter(doClick(e, true), null)
                super.onLongPress(e)
            }
        }))
    }

    interface MapListener {
        fun onClick(item: PathItem, index: Int)
        fun onShow(item: PathItem, index: Int, size: Int)
        fun onLongClick(item: PathItem, index: Int)
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
                doCenter(rectF)
//                doAnimateCenter(rectF, null)
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun drawCustom(canvas: Canvas) {
        try {
            for (pathItem in pathItems) {
                pathItem.draw(canvas, mPaint)
            }
        } catch (e: Exception) {
        }
    }

    private val rectF = RectF()

    fun doCenter(index: Int) {
        if (index < 0 || index >= pathItems.size) {
            doCenter(rectF)
        } else {
            val pathItem = pathItems[index]
            doCenter(pathItem.getRectF())
        }
    }

    private fun doCenter(rectF: RectF) {
        doAsync {
            center(rectF)
            uiThread {
                invalidate()
            }
        }
    }

    private fun center(rectF: RectF) {

        val Width = right - left
        val Height = bottom - top
        var width = rectF.width()//rectF.right - rectF.left
        var height = rectF.height()//rectF.bottom - rectF.top
        val sX = Width / width
        val sY = Height / height
        val scale = Math.min(sX, sY)

        width *= scale
        height *= scale

        var dX = (Width - width) / (2 * scale)
        var dY = (Height - height) / (2 * scale)

        dX -= rectF.left
        dY -= rectF.top

        resetMatrix()
        postTranslate(dX, dY)
        postScale(scale, scale)
        onMatrixEnd(scale)
    }

    private fun doClick(e: MotionEvent, isLongClick: Boolean): RectF {

        val matrixValue = getMatrixValues()
        val scale = matrixValue[Matrix.MSCALE_X]

        val x = (e.x - matrixValue[Matrix.MTRANS_X]) / scale
        val y = (e.y - matrixValue[Matrix.MTRANS_Y]) / scale

        var item: PathItem? = null
        var index = 0
        var rf = rectF
        try {
            for (i in pathItems.indices) {
                val pathItem = pathItems[i]
                pathItem.isSelected = pathItem.isTouch(x.toInt(), y.toInt())

                if (pathItem.isSelected) {
                    item = pathItem
                    index = i
                    rf = pathItem.getRectF()
                }
            }
        } catch (e1: Exception) {
        }

        if (listener != null && item != null) {
            if (isLongClick) {
                listener?.onLongClick(item, index)
            } else {
                listener?.onClick(item, index)
            }
        }
        return rf
    }

    fun getPathItems(): ArrayList<PathItem> {
        return pathItems
    }

    private interface AnimationListener {
        fun onFinish()
    }

    fun doAnimateCenter(index: Int) {
//        doAnimateCenter(rectF, object : AnimationListener {
//            override fun onFinish() {
//                doAnimateCenter(pathItems[index].getRectF(), null)
//            }
//        })
        doAnimateCenter(pathItems[index].getRectF(), null)
    }

    private fun doAnimateCenter(rectF: RectF, listener: AnimationListener?) {
//        val lastDX = dX
//        val lastDY = dY
//        val lastScale = scale
//        center(rectF)
//        val goalDX = dX
//        val goalDY = dY
//        val goalScale = scale
//
//        var step = 20
//
//        var stepX = (goalDX - lastDX) / step
//        var stepY = (goalDY - lastDY) / step
//        var stepScale = (goalScale - lastScale) / step
//
//        doAsync {
//            for (i in 1 .. step) {
//                dX = lastDX + stepX * i
//                dY = lastDY + stepY * i
//                scale = lastScale + stepScale * i
//                uiThread {
//                    invalidate()
//                }
//                Thread.sleep(100)
//            }
//            listener?.onFinish()
//        }

    }
}