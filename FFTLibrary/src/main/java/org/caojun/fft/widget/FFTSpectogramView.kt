package com.paramsen.noise.sample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import org.caojun.fft.*
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * @author Pär Amsen 06/2017
 */
class FFTSpectogramView(context: Context, attrs: AttributeSet?) : SimpleSurface(context, attrs) {

    private val sec = 10
    private val hz = 44100 / 4096
    private val fps = 80
    private val history = hz * sec
    private var resolution = 512
    private var minResolution = 64
    private val ffts = ArrayDeque<FloatArray>()

    private val paintSpectogram: Paint = Paint()
    private val paintText: Paint = textPaint()
    private val paintMsg: Paint = errTextPaint()
    private val bg = Color.rgb(20, 20, 25)

    private val hotThresholds = hashMapOf(512 to 15000, 256 to 15000, 128 to 30000, 64 to 45000)

    private val drawTimes = ArrayDeque<Long>()
    private var msg: Pair<Long, String>? = null

    private var lastAvg = Pair(System.currentTimeMillis(), 0)

    init {
        paintSpectogram.color = Color.parseColor("#FF2C00")
        paintSpectogram.style = Paint.Style.FILL
    }

    private fun drawTitle(canvas: Canvas) = canvas.drawText("FFT SPECTOGRAM", 16f.px, 24f.px, paintText)

    private fun drawIndicator(canvas: Canvas) {
        for (i in 0..height) {
            val f = i / height.toDouble()
            paintSpectogram.color = Spectogram.color(1.0 - f)

            canvas.drawRect(0f, i.toFloat(), 10f, i + 1f, paintSpectogram)
        }
    }

    private fun drawMsg(canvas: Canvas) {
        if (msg?.first ?: 0 > System.currentTimeMillis()) {
            canvas.drawText(msg?.second, (width - paintMsg.measureText(msg?.second)) / 2, height - 16f.px, paintMsg)
        }
    }

    private fun drawSpectogram(canvas: Canvas) {
        val fftW = width / history.toFloat()
        val bandWH = height / resolution.toFloat()

        var x: Float
        var y: Float
        var band: FloatArray? = null

        val hot = hotThresholds[resolution] ?: 0

        for (i in 0 until ffts.size) {
            synchronized(ffts) {
                band = ffts.elementAtOrNull(i)
            }

            x = width - (fftW * i)

            for (j in 0 until resolution) {
                y = height - (bandWH * j)
                val mag = band?.get(j) ?: .0f

                paintSpectogram.color = Spectogram.color(Math.min(mag / hot.toDouble(), 1.0))
                canvas.drawRect(x - fftW, y - bandWH, x, y, paintSpectogram)
            }
        }
    }

    private fun canDrawSpectogram() = resolution >= minResolution

    private fun drawGraphic(canvas: Canvas): Canvas {
        canvas.drawColor(bg)

        if (!canDrawSpectogram()) {
            val msg = "GPU MEM TOO LOW"
            drawTitle(canvas)
            canvas.drawText(msg, (width - paintMsg.measureText(msg)) / 2, height - 16f.px, paintMsg)

            return canvas
        }

        // If rendering is causing backpressure [and thus fps drop], lower resolution + show message
        if (resolution >= minResolution && drawTimes.size >= history / 5 && avgDrawTime() > fps) {
            synchronized(ffts) {
                ffts.clear()
            }

            drawTimes.clear()
            resolution /= 2
            msg = Pair(System.currentTimeMillis() + 10000, "DOWNSAMPLE DUE TO LOW GPU MEMORY")

            return canvas
        }

        drawTimes.addLast(measureTimeMillis {
            drawSpectogram(canvas)
            drawIndicator(canvas)
            drawTitle(canvas)
            drawMsg(canvas)
        })

        while (drawTimes.size > history) drawTimes.removeFirst()

        return canvas
    }

    fun show(fft: FloatArray) {
        if (canDrawSpectogram()) {
            val bands = FloatArray(resolution)
            var accum: Float
            var avg = 0f

            for (i in 0..resolution - 1) {
                accum = .0f

                for (j in 0..fft.size / resolution - 1 step 2) {
                    accum += (Math.sqrt(Math.pow(fft[i * j].toDouble(), 2.0) + Math.pow(fft[i * j + 1].toDouble(), 2.0))).toFloat() //magnitudes
                }

                accum /= resolution
                bands[i] = accum
                avg += accum
            }

            avg /= resolution

            for (i in 0..resolution - 1) {
                if (bands[i] < avg / 2) bands[i] * 1000f
            }

            synchronized(ffts) {
                ffts.addFirst(bands)

                while (ffts.size > history)
                    ffts.removeLast()
            }
        }

        drawSurface(this::drawGraphic)
    }

    private fun avgDrawTime(): Int {
        if (System.currentTimeMillis() - lastAvg.first > 1000) {
            lastAvg = Pair(System.currentTimeMillis(), if (drawTimes.size > 0) drawTimes.sum().div(drawTimes.size).toInt() else 0)
        }

        return lastAvg.second
    }
}