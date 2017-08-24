package org.caojun.library.weekpager.view

import android.graphics.Paint
import android.graphics.Path

/**
 * Created by CaoJun on 2017/8/23.
 */
class Spring {
    var paint: Paint = Paint()
    var path: Path = Path()

    var headPoint: Point = Point()
    var footPoint: Point = Point()

    fun Spring() {
        init()
    }

    private fun init() {

//        headPoint = Point()
//        footPoint = Point()

//        path = Path()

//        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.style = Paint.Style.FILL_AND_STROKE
        paint!!.strokeWidth = 1f
    }

    fun makePath() {

        val headOffsetX = (headPoint.getRadius() * Math.sin(
                Math.atan(((footPoint.getY() - headPoint.getY()) / (footPoint.getX() - headPoint.getX())).toDouble()))).toFloat()
        val headOffsetY = (headPoint.getRadius() * Math.cos(
                Math.atan(((footPoint.getY() - headPoint.getY()) / (footPoint.getX() - headPoint.getX())).toDouble()))).toFloat()

        val footOffsetX = (footPoint.getRadius() * Math.sin(
                Math.atan(((footPoint.getY() - headPoint.getY()) / (footPoint.getX() - headPoint.getX())).toDouble()))).toFloat()
        val footOffsetY = (footPoint.getRadius() * Math.cos(
                Math.atan(((footPoint.getY() - headPoint.getY()) / (footPoint.getX() - headPoint.getX())).toDouble()))).toFloat()

        val x1 = headPoint.getX() - headOffsetX
        val y1 = headPoint.getY() + headOffsetY

        val x2 = headPoint.getX() + headOffsetX
        val y2 = headPoint.getY() - headOffsetY

        val x3 = footPoint.getX() - footOffsetX
        val y3 = footPoint.getY() + footOffsetY

        val x4 = footPoint.getX() + footOffsetX
        val y4 = footPoint.getY() - footOffsetY

        val anchorX = (footPoint.getX() + headPoint.getX()) / 2
        val anchorY = (footPoint.getY() + headPoint.getY()) / 2

        path.reset()
        path.moveTo(x1, y1)
        path.quadTo(anchorX, anchorY, x3, y3)
        path.lineTo(x4, y4)
        path.quadTo(anchorX, anchorY, x2, y2)
        path.lineTo(x1, y1)
    }

//    fun getHeadPoint(): Point {
//        return headPoint
//    }
//
//    fun getFootPoint(): Point {
//        return footPoint
//    }

    fun setIndicatorColor(color: Int) {
        paint.color = color
    }

    fun getIndicatorColor(): Int {
        return paint.color
    }
}