package org.caojun.svgmap

import android.graphics.*

class PathItem {

    val index: Int
    val id: String
    val title: String
    private var path: Path
    var isSelected = false
    private var color: Int = Color.BLUE

    constructor(index: Int, id: String, title: String, path: Path): this(index, id, title, path, Color.BLUE)

    constructor(index: Int, id: String, title: String, path: Path, color: Int) {
        this.index = index
        this.id = id.replace('-', '_').toLowerCase()
        this.title = title
        this.path = path
        this.color = color
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
            paint.color = color
            paint.style = Paint.Style.FILL
        } else {
            //绘制具体显示的
            paint.color = Color.GRAY
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