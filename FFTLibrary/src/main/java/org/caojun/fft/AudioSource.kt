package org.caojun.fft

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.paramsen.noise.Noise
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.nio.FloatBuffer

class AudioSource {

    interface OnDecibelListener {
        fun onGetDecibel(decibel: Double)
        fun onGetData(data: FloatArray)
        fun onGetFFT(data: FloatArray)
        fun onRequestPermission()
    }

    private var isGetVoiceRun = false
    private val listener: OnDecibelListener

    constructor(listener: OnDecibelListener) {
        this.listener = listener
    }

    fun start() {
        if (isGetVoiceRun) {
            return
        }
        isGetVoiceRun = true
        val RATE_HZ = 44100
        val SAMPLE_SIZE = 4096
        val BUF_SIZE = 512
        val src = MediaRecorder.AudioSource.MIC
        val cfg = AudioFormat.CHANNEL_IN_MONO
        val format = AudioFormat.ENCODING_PCM_16BIT
        val size = AudioRecord.getMinBufferSize(RATE_HZ, cfg, format)

        if (size <= 0) {
            listener.onRequestPermission()
            return
        }

        doAsync {
            val recorder = AudioRecord(src, RATE_HZ, cfg, format, size)
            recorder.startRecording()

            val buf = ShortArray(BUF_SIZE)
            val out = FloatBuffer.allocate(SAMPLE_SIZE)
            var read = 0

            val noise = Noise.real().optimized().init(SAMPLE_SIZE, false)

            while (isGetVoiceRun) {
                read += recorder.read(buf, read, buf.size - read)

                if (read == buf.size) {
                    for (i in 0 until buf.size) {
                        out.put(buf[i].toFloat())
                    }

                    if (!out.hasRemaining()) {
                        val cpy = FloatArray(out.array().size)
                        System.arraycopy(out.array(), 0, cpy, 0, out.array().size)

                        var v: Long = 0
                        // 将 buffer 内容取出，进行平方和运算
                        for (i in cpy.indices) {
                            v += (cpy[i] * cpy[i]).toLong()
                        }
                        // 平方和除以数据总长度，得到音量大小。
                        val mean = v / read.toDouble()
                        val volume = 10 * Math.log10(mean)


                        uiThread {
                            if (volume.toString().indexOf(".") > 0) {
                                listener?.onGetDecibel(volume)
                            }
                            listener?.onGetFFT(noise.fft(cpy, FloatArray(SAMPLE_SIZE + 2)))
                            listener?.onGetData(cpy)
                        }
                        out.clear()
                    }

                    read = 0
                }
            }

            recorder.stop()
            recorder.release()
        }
    }

    fun stop() {
        isGetVoiceRun = false
    }
}