package org.caojun.fft.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import org.caojun.fft.R
import org.caojun.fft.SimpleSurface
import org.caojun.fft.px
import org.caojun.fft.textPaint
import java.util.*

/**
 * @author Pär Amsen 06/2017
 */
class AudioView(context: Context, attrs: AttributeSet?) : SimpleSurface(context, attrs) {
    private val sec = 10
    private val hz = 44100
    private val merge = 512
    private val history = hz * sec / merge
    private val audio: ArrayDeque<Float> = ArrayDeque()

    private val paintAudio: Paint = Paint()
//    val paintLines: Paint = Paint()
    private val paintText: Paint = textPaint()
    private val path: Path = Path()

    private val bg = Color.parseColor("#39424F")

    private val title = context.getString(R.string.audio)

    init {
        paintAudio.color = Color.parseColor("#23E830")
        paintAudio.strokeWidth = 0f
        paintAudio.style = Paint.Style.STROKE
    }

    private fun drawAudio(canvas: Canvas): Canvas {
        path.reset()

        synchronized(audio) {
            for ((i, sample) in audio.withIndex()) {
                if (i == 0) path.moveTo(width.toFloat(), sample)
                path.lineTo(width - width * i / history.toFloat(), Math.min(sample * 0.175f + height / 2f, height.toFloat()))
            }
            if (audio.size in 1..(history - 1)) path.lineTo(0f, height / 2f)
        }

        canvas.drawColor(bg)
        canvas.drawPath(path, paintAudio)
        canvas.drawText(title, 16f.px, 24f.px, paintText)

        return canvas
    }

    fun show(data: FloatArray) {
        synchronized(audio) {
            var accum = 0f

            for ((i, sample) in data.withIndex()) {
                if (i > 0 && i % merge != 0) {
                    accum += sample
                } else {
                    audio.addFirst(accum / merge)
                    accum = 0f
                }
            }

            while (audio.size > history) {
                audio.removeLast()
            }
        }

        drawSurface(this::drawAudio)
    }
}