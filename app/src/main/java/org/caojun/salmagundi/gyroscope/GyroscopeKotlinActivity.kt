package org.caojun.salmagundi.gyroscope

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_gyroscope.*
import org.caojun.salmagundi.R

/**
 * Created by CaoJun on 2017/9/28.
 */
class GyroscopeKotlinActivity: Activity() {

    private val Direction_Up = 0
    private val Direction_Down = 1
    private val Direction_Left = 2
    private val Direction_Right = 3
    private val Step = intArrayOf(1, 3, 6, 10, 15)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyroscope)

        btnUp.setOnClickListener {
            moveImageView(Direction_Up)
        }
        btnDown.setOnClickListener {
            moveImageView(Direction_Down)
        }
        btnLeft.setOnClickListener {
            moveImageView(Direction_Left)
        }
        btnRight.setOnClickListener {
            moveImageView(Direction_Right)
        }
    }

    private fun moveImageView(direction: Int) {
        when (direction) {
            Direction_Up -> {
                iv0.y += Step[0]
                iv1.y += Step[1]
                iv2.y += Step[2]
                iv3.y += Step[3]
                iv4.y += Step[4]
            }
            Direction_Down -> {
                iv0.y -= Step[0]
                iv1.y -= Step[1]
                iv2.y -= Step[2]
                iv3.y -= Step[3]
                iv4.y -= Step[4]
            }
            Direction_Left -> {
                iv0.x += Step[0]
                iv1.x += Step[1]
                iv2.x += Step[2]
                iv3.x += Step[3]
                iv4.x += Step[4]
            }
            Direction_Right -> {
                iv0.x -= Step[0]
                iv1.x -= Step[1]
                iv2.x -= Step[2]
                iv3.x -= Step[3]
                iv4.x -= Step[4]
            }
        }
    }
}