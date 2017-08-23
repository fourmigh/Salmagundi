package org.caojun.library.weekpager.view

/**
 * Created by CaoJun on 2017/8/23.
 */
class Point {
    private var x: Float = 0.toFloat()
    private var y: Float = 0.toFloat()
    private var radius: Float = 0.toFloat()
    private var color: Int = 0

    fun getX(): Float {
        return x
    }

    fun setX(x: Float) {
        this.x = x
    }

    fun getY(): Float {
        return y
    }

    fun setY(y: Float) {
        this.y = y
    }

    fun getRadius(): Float {
        return radius
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    fun getColor(): Int {
        return color
    }

    fun setColor(color: Int) {
        this.color = color
    }
}