package org.caojun.salmagundi.decibelman

import android.media.AudioFormat
import android.media.AudioFormat.ENCODING_PCM_16BIT
import android.media.AudioFormat.CHANNEL_IN_DEFAULT
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.MediaRecorder.AudioSource.MIC
import com.socks.library.KLog
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * Created by CaoJun on 2017/9/11.
 */
class Decibelman {
    private val TAG = "Decibelman"
    private val SAMPLE_RATE_IN_HZ = 8000
    private val BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT)
    private var mAudioRecord: AudioRecord? = null
    private var isGetVoiceRun: Boolean = false
    private var listener: OnDecibelListener? = null

    interface OnDecibelListener {
        fun onGetDecibel(decibel: Double)
    }

    constructor(listener: OnDecibelListener) {
        this.listener = listener
    }

    fun start() {
        if (isGetVoiceRun) {
            KLog.e(TAG, "还在录着呢")
            return
        }
        mAudioRecord = AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE)
        if (mAudioRecord == null) {
           KLog.e("sound", "mAudioRecord初始化失败")
        }
        isGetVoiceRun = true

        doAsync {
            mAudioRecord!!.startRecording()
            val buffer = ShortArray(BUFFER_SIZE)
            while (isGetVoiceRun) {
                //r是实际读取的数据长度，一般而言r会小于buffersize
                val r = mAudioRecord!!.read(buffer, 0, BUFFER_SIZE)
                var v: Long = 0
                // 将 buffer 内容取出，进行平方和运算
                for (i in buffer.indices) {
                    v += (buffer[i] * buffer[i]).toLong()
                }
                // 平方和除以数据总长度，得到音量大小。
                val mean = v / r.toDouble()
                val volume = 10 * Math.log10(mean)
                uiThread {
                    listener?.onGetDecibel(volume)
                }
                // 大概一秒十次
                synchronized(this) {
                    Thread.sleep(100)
                }
            }
            mAudioRecord!!.stop()
            mAudioRecord!!.release()
            mAudioRecord = null
        }
    }

    fun stop() {
        isGetVoiceRun = false
    }
}