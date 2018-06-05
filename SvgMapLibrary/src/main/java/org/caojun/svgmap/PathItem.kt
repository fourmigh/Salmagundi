package org.caojun.svgmap

import android.content.Context
import android.graphics.*
import org.caojun.utils.ActivityUtils

class PathItem {

    val context: Context
    val index: Int
    val id: String
    val title: String
    private var path: Path
    var isSelected = false
    private var colorSelected: Int = Color.BLUE
    private var colorUnselected: Int = Color.GRAY

    constructor(context: Context, index: Int, id: String, title: String, path: Path): this(context, index, id, title, path, Color.BLUE, Color.GRAY)

    constructor(context: Context, index: Int, id: String, title: String, path: Path, colorSelected: Int, colorUnselected: Int) {
        this.context = context
        this.index = index
        this.id = id.replace('-', '_').toLowerCase()
        this.title = title
        this.path = path
        this.colorSelected = colorSelected
        this.colorUnselected = colorUnselected
    }

    //地图名称（国名、地区名）
    fun getName(): String {
        val id = ActivityUtils.getStringResId(context, id)
        return if (id == null) {
            title
        } else {
            context.getString(id)
        }
    }

    //国旗（地区旗）图片Url
    fun getFlagUrl(): String {
        val flag = "flag_$id"
        val resId = ActivityUtils.getStringResId(context, flag)
        return if (resId == null) {
            ""
        } else {
            context.getString(resId)
        }
    }

    /**
     * 是否touch在该path内部
     * @param x
     * @param y
     * @return
     */
    fun isTouch(x: Int, y: Int): Boolean {
        val result = Region()
        //构造一个区域对象。
        val r = RectF()
        //计算path的边界
        path.computeBounds(r, true)
        //设置区域路径和剪辑描述的区域
        result.setPath(path, Region(r.left.toInt(), r.top.toInt(), r.right.toInt(), r.bottom.toInt()))
        return result.contains(x, y)
    }

    fun draw(canvas: Canvas, paint: Paint) {
        paint.clearShadowLayer()
        if (isSelected) {
            //首先绘制选中的背景阴影
            paint.style = Paint.Style.FILL
            paint.setShadowLayer(8f, 0f, 0f, Color.BLACK)
            canvas.drawPath(path, paint)
            //绘制具体显示的
            paint.clearShadowLayer()
            paint.color = colorSelected
            paint.style = Paint.Style.FILL
        } else {
            //绘制具体显示的
            paint.color = colorUnselected
            paint.style = Paint.Style.FILL
        }
        canvas.drawPath(path, paint)
    }

    fun getRectF(): RectF {
        val r = RectF()
        //计算path的边界
        path.computeBounds(r, true)
        return r
    }
}