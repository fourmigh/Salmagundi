package com.paramsen.noise.sample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import org.caojun.fft.SimpleSurface
import org.caojun.fft.px
import org.caojun.fft.textPaint
import java.lang.System.arraycopy

/**
 * @author Pär Amsen 06/2017
 */
class FFTBandView(context: Context, attrs: AttributeSet?) : SimpleSurface(context, attrs) {
    private val size = 4096
    private val bands = 64
    private val bandSize = size / bands
    private val maxConst = 1750000000 //reference max value for accum magnitude
    private var average = .0f

    private val fft: FloatArray = FloatArray(4096)
    private val paintBandsFill: Paint = Paint()
    private val paintBands: Paint = Paint()
    private val paintAvg: Paint = Paint()
    private val paintText: Paint = textPaint()

    init {
        paintBandsFill.color = Color.parseColor("#33FF2C00")
        paintBandsFill.style = Paint.Style.FILL

        paintBands.color = Color.parseColor("#AAFF2C00")
        paintBands.strokeWidth = 1f
        paintBands.style = Paint.Style.STROKE

        paintAvg.color = Color.parseColor("#33FFFFFF")
        paintAvg.strokeWidth = 1f
        paintAvg.style = Paint.Style.STROKE
    }

    private fun drawAudio(canvas: Canvas): Canvas {
        canvas.drawColor(Color.DKGRAY)
        for (i in 0..bands - 1) {
            var accum = .0f

            synchronized(fft) {
                for (j in 0..bandSize - 1 step 2) {
                    //convert real and imag part to get energy
                    accum += (Math.pow(fft[j + (i * bandSize)].toDouble(), 2.0) + Math.pow(fft[j + 1 + (i * bandSize)].toDouble(), 2.0)).toFloat()
                }

                accum /= bandSize / 2
            }

            average += accum

            canvas.drawRect(width * (i / bands.toFloat()), height - (height * Math.min(accum / maxConst.toDouble(), 1.0).toFloat()) - height * .02f, width * (i / bands.toFloat()) + width / bands.toFloat(), height.toFloat(), paintBandsFill)
            canvas.drawRect(width * (i / bands.toFloat()), height - (height * Math.min(accum / maxConst.toDouble(), 1.0).toFloat()) - height * .02f, width * (i / bands.toFloat()) + width / bands.toFloat(), height.toFloat(), paintBands)
        }

        average /= bands

        canvas.drawLine(0f, height - (height * (average / maxConst)) - height * .02f, width.toFloat(), height - (height * (average / maxConst)) - height * .02f, paintAvg)
        canvas.drawText("FFT BANDS", 16f.px, 24f.px, paintText)

        return canvas
    }

    fun show(fft: FloatArray) {
        synchronized(this.fft) {
            arraycopy(fft, 2, this.fft, 0, fft.size - 2)
            drawSurface(this::drawAudio)
        }
    }
}