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
import org.w3c.dom.Element
import java.io.InputStream
import java.util.ArrayList
import javax.xml.parsers.DocumentBuilderFactory

class SvgMapView: ScaleCanvasView {

    //默认动画帧数
    private val DEFAULT_STEP = 20
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
                postInvalidate()
                return super.onSingleTapUp(e)
            }

            override fun onLongPress(e: MotionEvent) {
                doClick(e, true)
                postInvalidate()
                super.onLongPress(e)
            }
        }))
    }

    interface MapListener {
        fun onClick(item: PathItem?, index: Int)
        fun onShow(item: PathItem, index: Int, size: Int)
        fun onLongClick(item: PathItem?, index: Int)
    }

    private var listener: MapListener? = null

    fun setMapListener(listener: MapListener) {
        this.listener = listener
    }

    private var inputStream: InputStream? = null

    fun hasMap(mapName: String): Boolean {
        return try {
            var name = mapName.replace('-', '_').toLowerCase()
            //打开输入流
            val resId = resources.getIdentifier("map_$name", "raw", context.packageName)
            inputStream = resources.openRawResource(resId)
            true
        } catch (e: Exception) {
            false
        }

    }

    private var leftLongitude = ""//左经度
    private var topLatitude = ""//上纬度
    private var rightLongitude = ""//右经度
    private var bottomLatitude = ""//下纬度

    /**
     * 解析path
     */
    fun setMap(mapName: String, colorSelected: Int, colorUnselected: Int) {

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
                val doc = db.parse(inputStream)

                ////////////////////////////////////////////////////////////////////////
                val de = doc.documentElement
                if (de != null) {
                    val defs = de.getElementsByTagName("amcharts:ammap")
                    if (defs != null) {
                        val ammap = defs.item(0) as Element
                        if (ammap != null) {
                            leftLongitude = ammap.getAttribute("leftLongitude")
                            topLatitude = ammap.getAttribute("topLatitude")
                            rightLongitude = ammap.getAttribute("rightLongitude")
                            bottomLatitude = ammap.getAttribute("bottomLatitude")
                        }
                    }
                }
                ////////////////////////////////////////////////////////////////////////

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
                    item = PathItem(context, i, id, title, path, colorSelected, colorUnselected)
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
//                    Thread.sleep(100)
                    item.isSelected = false
                    listener?.onShow(item, i, paths.length)
                }
//                curRectF = doCenter(rectF)
                curRectF.left = 0f
                curRectF.top = 0f
                curRectF.right = curRectF.left + width
                curRectF.bottom = curRectF.top + height
                doAnimateCenter(rectF)
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

    //全地图
    private val rectF = RectF()
    //当前选中区域地图
    private var curRectF = RectF()

    fun doCenter(index: Int): RectF {
        return if (index < 0 || index >= pathItems.size) {
            doCenter(rectF)
        } else {
            val pathItem = pathItems[index]
            doCenter(pathItem.getRectF())
        }
    }

    private fun doCenter(rectF: RectF): RectF {
        center(rectF)
        postInvalidate()
        return rectF
    }

    private fun center(rectF: RectF) {

        val Width = right - left
        val Height = bottom - top
        var width = rectF.width()
        var height = rectF.height()
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
        var index = -1
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
            e1.printStackTrace()
        }

        if (listener != null) {
            if (isLongClick) {
                listener?.onLongClick(item, index)
            } else {
                listener?.onClick(item, index)
            }
        }
        return rf
    }

    fun setSelected(index: Int, isSelected: Boolean, isSingle: Boolean) {
        if (index < 0 || index >= pathItems.size) {
            return
        }
        if (isSingle) {
            for (i in pathItems.indices) {
                pathItems[i].isSelected = false
            }
        }
        pathItems[index].isSelected = isSelected
    }

    fun getPathItems(): ArrayList<PathItem> {
        return pathItems
    }

    private interface AnimationListener {
        fun onFinish()
    }

    fun doAnimateCenter(index: Int): PathItem? {
        return doAnimateCenter(DEFAULT_STEP, index)
    }

    fun doAnimateCenter(step: Int, index: Int): PathItem? {
        return doAnimateCenter(step, index, false)
    }

    fun doAnimateCenter(rectF: RectF) {
        doAnimateCenter(DEFAULT_STEP, rectF, null)
    }

    fun doAnimateCenter(step: Int, index: Int, isDirect: Boolean): PathItem? {
        val rf = if (index < 0 || index >= pathItems.size) rectF else pathItems[index].getRectF()
        if (isDirect) {
            doAnimateCenter(step, rf, null)
        } else {
            doAnimateCenter(step, rectF, object : AnimationListener {
                override fun onFinish() {
                    doAnimateCenter(step, rf, null)
                }
            })
        }
        return if (rf == rectF) {
            null
        } else {
            pathItems[index]
        }
    }

    private fun doAnimateCenter(step: Int, rectF: RectF, listener: AnimationListener?) {
        if (rectF == curRectF) {
            listener?.onFinish()
            return
        }
        val STEP = if (step < 1) DEFAULT_STEP else step
        val stepLeft = (rectF.left - curRectF.left) / STEP
        val stepRight = (rectF.right - curRectF.right) / STEP
        val stepTop = (rectF.top - curRectF.top) / STEP
        val stepBottom = (rectF.bottom - curRectF.bottom) / STEP
        doAsync {
            for (i in 1 .. STEP) {
                val rf = RectF()
                rf.left = curRectF.left + stepLeft * i
                rf.right = curRectF.right + stepRight * i
                rf.top = curRectF.top + stepTop * i
                rf.bottom = curRectF.bottom + stepBottom * i

                doCenter(rf)
                Thread.sleep(100)
            }
            curRectF = rectF
            listener?.onFinish()
        }
    }

    fun setGestureSupport(isGestureSupport: Boolean) {
        init(isGestureSupport)
    }
}