package org.caojun.salmagundi.gyroscope

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_gyroscope.*
import org.caojun.salmagundi.R

/**
 * Created by CaoJun on 2017/9/28.
 */
class GyroscopeKotlinActivity: Activity(), SensorEventListener {

    private val Direction_Up = 0
    private val Direction_Down = 1
    private val Direction_Left = 2
    private val Direction_Right = 3
    private val Step = intArrayOf(1, 3, 6, 10, 15)
    private val MaxStep = 10
    private var OriginalValue = floatArrayOf(0f, 0f, 0f)
    private var OriginalValue4 = floatArrayOf(0f, 0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyroscope)

        initGyroscope()

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
            Direction_Down -> {
                if (iv4.y + Step[4] > OriginalValue4[1] + Step[4] * MaxStep) {
                    return
                }
                iv0.y += Step[0]
                iv1.y += Step[1]
                iv2.y += Step[2]
                iv3.y += Step[3]
                iv4.y += Step[4]
            }
            Direction_Up -> {
                if (iv4.y - Step[4] < OriginalValue4[1] - Step[4] * MaxStep) {
                    return
                }
                iv0.y -= Step[0]
                iv1.y -= Step[1]
                iv2.y -= Step[2]
                iv3.y -= Step[3]
                iv4.y -= Step[4]
            }
            Direction_Right -> {
                if (iv4.x + Step[4] > OriginalValue4[0] + Step[4] * MaxStep) {
                    return
                }
                iv0.x += Step[0]
                iv1.x += Step[1]
                iv2.x += Step[2]
                iv3.x += Step[3]
                iv4.x += Step[4]
            }
            Direction_Left -> {
                if (iv4.x - Step[4] < OriginalValue4[0] - Step[4] * MaxStep) {
                    return
                }
                iv0.x -= Step[0]
                iv1.x -= Step[1]
                iv2.x -= Step[2]
                iv3.x -= Step[3]
                iv4.x -= Step[4]
            }
        }
    }

    private fun initData(event: SensorEvent) {
        if (OriginalValue4[0] == 0f && OriginalValue4[1] == 0f) {
            OriginalValue4[0] = iv4.x
            OriginalValue4[1] = iv4.y

        }

        if (OriginalValue[0] == 0f && OriginalValue[1] == 0f && OriginalValue[2] == 0f) {
            OriginalValue[0] = event.values[0]
            OriginalValue[1] = event.values[1]
            OriginalValue[2] = event.values[2]
        }
    }

    private fun initGyroscope() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        //SensorManager.SENSOR_DELAY_FASTEST(0微秒)：最快。最低延迟，一般不是特别敏感的处理不推荐使用，该模式可能在成手机电力大量消耗，由于传递的为原始数据，诉法不处理好会影响游戏逻辑和UI的性能
        //SensorManager.SENSOR_DELAY_GAME(20000微秒)：游戏。游戏延迟，一般绝大多数的实时性较高的游戏都是用该级别
        //SensorManager.SENSOR_DELAY_NORMAL(200000微秒):普通。标准延时，对于一般的益智类或EASY级别的游戏可以使用，但过低的采样率可能对一些赛车类游戏有跳帧现象
        //SensorManager.SENSOR_DELAY_UI(60000微秒):用户界面。一般对于屏幕方向自动旋转使用，相对节省电能和逻辑处理，一般游戏开发中不使用
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {

        initData(event)
        if (event.values[1] < OriginalValue[1]) {
            moveImageView(Direction_Up)
        } else if (event.values[1] > OriginalValue[1]) {
            moveImageView(Direction_Down)
        }
        if (event.values[0] > OriginalValue[0]) {
            moveImageView(Direction_Left)
        } else if (event.values[0] < OriginalValue[0]) {
            moveImageView(Direction_Right)
        }
    }
}