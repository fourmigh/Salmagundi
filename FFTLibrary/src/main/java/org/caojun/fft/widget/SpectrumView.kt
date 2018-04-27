package org.caojun.fft.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.caojun.fft.R

/**
 * 频谱图
 */
class SpectrumView: View {

    private val FFT_SIZE = 512 // 进行两次1024个数据的FFT
    private val Samplerate = 44100

    //中间显示的段数，这里取31段展示
    private val SPECTROGRAM_COUNT = 31
    //这里代表最高电频（最多的格子数）
    private val ROW_LOCAL_COUNT = 32
    /**
     * 高频与低频的分界位置
     */
    private val LowFreqDividing = 14
    /**
     * 纵坐标分布数组
     */
    private val Row_Local_Table = DoubleArray(ROW_LOCAL_COUNT)

//    fun setBitspersample(bitspersample: Int) {
//        this.bitspersample = bitspersample
//    }

    private val Bits_Per_Sample = 16//这里默认为16位

//    private var bits: Double = 0.toDouble()//音频编码长度存储的最大10进制的值
    /**
     * 两个柱形之间的间隔
     */
    private val XINTERVAL = 10
    private val YINTERVAL = 8

//    private var canvas = Canvas()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var LineViewWidth = 0f
    private var LineViewHeight = 0f
    //不分频的实部和虚部
    private val first_fft_real = DoubleArray(FFT_SIZE)
    private val first_fft_imag = DoubleArray(FFT_SIZE)
    //分频后的实部和虚部
    private val second_fft_real = DoubleArray(FFT_SIZE)
    private val second_fft_imag = DoubleArray(FFT_SIZE)
    //绘制频谱的实部和虚部
    private val real = DoubleArray(SPECTROGRAM_COUNT)
    private val imag = DoubleArray(SPECTROGRAM_COUNT)

    // 落差效果，记录最高点的坐标
    private val top_local = IntArray(ROW_LOCAL_COUNT) // 绿色点的坐标
    /**
     * 达到最大时,等到该数达到定值时才开始下落top_local_count[i] = 0的时候，处于最高点，top_local_count[i]会一直
     * 叠加到10，最高点方格才会下落
     */
    private val top_local_count = IntArray(ROW_LOCAL_COUNT)
    //下落延时次数
    private val DELAY = 10

    private var data: ShortArray? = null

    //频谱颜色
    private var sepColor = Color.rgb(63, 81, 181)
    //字体颜色
    private var textColor = Color.rgb(63, 81, 181)
    //最高点方格颜色
    private var topColor = Color.rgb(51, 181, 229)
    private var sepAlpha: Int = 0
    private var textAlpha:Int = 0
    private val sampleratePoint = DoubleArray(SPECTROGRAM_COUNT)//显示频点
    private var loc: IntArray? = null//取31组频率

    private val paintText = Paint()
    private var title = ""

    fun getTextAlpha(): Int {
        return textAlpha
    }

    fun setTextAlpha(textAlpha: Int) {
        this.textAlpha = textAlpha
    }

    fun getSepAlpha(): Int {
        return sepAlpha
    }

    fun setSepAlpha(sepAlpha: Int) {
        this.sepAlpha = sepAlpha
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {

        for (i in 0 until FFT_SIZE) {
            first_fft_real[i] = 0.0
            first_fft_imag[i] = 0.0
            second_fft_real[i] = 0.0
            second_fft_imag[i] = 0.0
        }
        for (i in 0 until SPECTROGRAM_COUNT) {
            real[i] = 0.0
            imag[i] = 0.0
        }

        mPaint.color = textColor
        mPaint.alpha = getTextAlpha()
        mPaint.textSize = 15f

        paintText.color = textColor
        paintText.textAlign = Paint.Align.RIGHT
        paintText.textSize = 36f

        // 构建纵坐标的值;bits = 16位数转十进制的最大值,这里面的值是定值，只需要算一次
        Row_Local_Table[0] = 5.0
        //音频编码长度存储的最大10进制的值
        val bits = Math.pow(2.0, (Bits_Per_Sample - 1).toDouble()) - 1
        val step = Math.pow(bits / Row_Local_Table[0], 1.0 / ROW_LOCAL_COUNT)// x的y次幂
        for (i in 1 until ROW_LOCAL_COUNT) {
            Row_Local_Table[i] = Row_Local_Table[i - 1] * step
        }

        // 设置透明度(有真实信号加深)0-250
        setSepAlpha(250)
        setTextAlpha(250)

        title = context.getString(R.string.spectrum)
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)
        LineViewWidth = (this.width - XINTERVAL).toFloat()
        LineViewHeight = (this.height - YINTERVAL).toFloat()
        //显示频谱
        // 格子的频谱(绘制横坐标)
        drawSpectrogramAxis(canvas)
        //绘制纵坐标方格
        drawGridTypeSpectrogram(canvas, real, imag)

        canvas.drawText(title, this.width.toFloat() - 20, 40f, paintText)
    }

    /**
     * 绘制频率坐标
     */
    private fun drawSpectrogramAxis(canvas: Canvas) {
        //这里对应的值是30HZ-20KHZ，中间的值成对数关系，即sampleratePoint
        val X_axis = arrayOf("20Hz", "46Hz", "69Hz", "105Hz", "160Hz", "244Hz", "371Hz", "565Hz", "859Hz", "1.30KHz", "1.98KHz", "3.02KHz", "4.60KHz", "7.00KHz", "10.6KHz", "20KHz")
        val x_step = LineViewWidth / SPECTROGRAM_COUNT
        //这里计算的是格子的宽度
        val width = x_step - XINTERVAL
        // 横坐标(Hz)
        for (i in X_axis.indices) {
            val textWidth = mPaint.measureText(X_axis[i])
            //这里是为了计算格子跟字的宽度差，用来确定字的位置,确保字跟方格中心在一条直线
            val xBad = (width - textWidth) / 2
            val x = XINTERVAL.toFloat() + 2f * i.toFloat() * x_step + xBad
            //获取文字上坡度(为负数)和下坡度的高度
            val font = mPaint.fontMetrics
            val y = -(font.ascent + font.descent) / 2
            canvas.drawText(X_axis[i], x, LineViewHeight - YINTERVAL / 2 + y, mPaint)
        }
    }

    /**
     * 柱形频谱：方格方式显示
     *
     * @param real
     * @param imag
     */
    private fun drawGridTypeSpectrogram(canvas: Canvas, real: DoubleArray, imag: DoubleArray) {
        var model: Double
        val local = IntArray(ROW_LOCAL_COUNT)
        //计算绘制频谱格子的宽度
        val x_step = LineViewWidth / SPECTROGRAM_COUNT
        //格子的高度
        val y_step = LineViewHeight / ROW_LOCAL_COUNT
        canvas.save()
        canvas.translate(0f, -10f)
        for (i in 0 until SPECTROGRAM_COUNT) {
            model = 2 * Math.hypot(real[i], imag[i]) / FFT_SIZE// 计算电频最大值，Math.hypot(x,y)返回sqrt(x2+y2)，最高电频
            for (k in 1 until ROW_LOCAL_COUNT) {
                if (model >= Row_Local_Table[k - 1] && model < Row_Local_Table[k]) {
                    local[i] = k - 1//这里取最高电频所对应的方格数
                    break
                }
            }
            // 最上面的为0位置，最下面的为31位置,为了方便绘制top方格
            local[i] = ROW_LOCAL_COUNT - local[i]
            // 柱形
            mPaint.color = sepColor
            mPaint.alpha = getSepAlpha()

            val x = XINTERVAL + i * x_step
            for (j in ROW_LOCAL_COUNT downTo local[i] + 1) {
                val y = (j - 1) * y_step
                canvas.drawRect(x, y, x + x_step - XINTERVAL, y + y_step - YINTERVAL,
                        mPaint)// 绘制矩形,左上右下
            }
            // 绿点
            mPaint.color = topColor
            mPaint.alpha = getSepAlpha()
            //下面部分是用来显示落差效果的，没有强大的理解能力可能会绕晕，所以我也不做过多注释，看个人天赋吧
            //local[i] < top_local[i]说明最高点改变（local[i]越小，点越高，这里需要注意）
            if (local[i] < top_local[i]) {
                //当进入到这里，说明达到最大电频，这个矩形会停留十次循环的时间才有改变
                top_local[i] = local[i]
                top_local_count[i] = 0
            } else {
                top_local_count[i]++
                //这里top_local_count这个是用来记录达到top值的柱体，然后会循环10次开始
                // top_local_count中小于DELAY的top_local都保持不变
                if (top_local_count[i] >= DELAY) {
                    top_local_count[i] = DELAY
                    //这里控制下降的速度
                    top_local[i] = if (local[i] > top_local[i] + 1) top_local[i] + 1 else local[i]
                }
            }
            //y增加则最高位方格下降
            val y = top_local[i] * y_step
            canvas.drawRect(x, y, x + x_step - XINTERVAL, y + y_step - YINTERVAL, mPaint)// 最高位置的方格
        }
        canvas.restore()
    }

    /**
     * 显示频谱时进行FFT计算
     *
     * @param buf
     * @param samplerate 采样率
     */
    private fun spectrogram(buf: FloatArray) {
        first_fft_real[0] = buf[0].toDouble()
        first_fft_imag[0] = 0.0

        second_fft_real[0] = buf[0].toDouble()
        second_fft_imag[0] = 0.0

//        for (i in 0 until FFT_SIZE) {
        for (i in 0 until buf.size / 8) {
            first_fft_real[i] = buf[i].toDouble()
            first_fft_imag[i] = 0.0
            // 八分频(相当于降低了8倍采样率)，这样1024缓存区中的fft频率密度就越大，有利于取低频
            second_fft_real[i] = (buf[i * 8] + buf[i * 8 + 1] + buf[i * 8 + 2]
                    + buf[i * 8 + 3] + buf[i * 8 + 4] + buf[i * 8 + 5]
                    + buf[i * 8 + 6] + buf[i * 8 + 7]) / 8.0
            second_fft_imag[i] = 0.0
        }
        // 高频部分从原始数据取
        fft(first_fft_real, first_fft_imag, FFT_SIZE)

        // 八分频后的1024个数据的FFT,频率间隔为5.512Hz(samplerate / 8)，取低频部分
        fft(second_fft_real, second_fft_imag, FFT_SIZE)
        //这里算出的是每一个频点的坐标，对应横坐标的值，因为是定值，所以只需要算一次
        if (loc == null) {
            loc = IntArray(SPECTROGRAM_COUNT)
//            sampleratePoint = DoubleArray(SPECTROGRAM_COUNT)
            for (i in loc!!.indices) {
                //20000表示的最大频点20KHZ,这里的20-20K之间坐标的数据成对数关系,这是音频标准
                val F = Math.pow((20000 / 20).toDouble(), 1.0 / SPECTROGRAM_COUNT)//方法中20为低频起点20HZ，31为段数
                sampleratePoint[i] = 20 * Math.pow(F, i.toDouble())//乘方，30为低频起点
                //这里的samplerate为采样率(samplerate / (1024 * 8))是8分频后点FFT的点密度
                loc!![i] = (sampleratePoint[i] / (Samplerate / (1024 * 8))).toInt()//估算出每一个频点的位置
            }
        }
        //低频部分
        for (j in 0 until LowFreqDividing) {
            val k = loc!![j]
            // 低频部分：八分频的数据,取31段，以第14段为分界点，小于为低频部分，大于为高频部分
            // 这里的14是需要取数后分析确定的，确保低频有足够的数可取
            real[j] = second_fft_real[k]
            imag[j] = second_fft_imag[k]
        }
        // 高频部分，高频部分不需要分频
        for (m in LowFreqDividing until loc!!.size) {
            val k = loc!![m]
            real[m] = first_fft_real[k / 8]
            imag[m] = first_fft_imag[k / 8]
        }
    }

    /**
     * 方格方式显示背景
     */
    private fun drawGridTypeSpectrogrambg(canvas: Canvas) {
        val x_step = LineViewWidth / SPECTROGRAM_COUNT
        val y_step = LineViewHeight / ROW_LOCAL_COUNT

        mPaint.color = Color.rgb(0x1f, 0x1f, 0x1f)
        for (i in 0 until SPECTROGRAM_COUNT) {
            val x = 25 + i * x_step
            for (j in 0 until ROW_LOCAL_COUNT) {
                val y = j * y_step
                canvas.drawRect(x, y, x + x_step - XINTERVAL, y + y_step - YINTERVAL,
                        mPaint)
            }
        }
    }

    /**
     * 绘制频谱,提供绘制接口
     *
     * @param buf
     */
    @Synchronized
    fun show(buf: FloatArray) {
        // 绘制网格频谱
        spectrogram(buf)
        // 刷屏
        invalidate()
    }

    /**
     * 快速傅立叶变换，将复数 x 变换后仍保存在 x 中(这个算法可以不用理解，直接用)，转成频率轴的数（呈线性分步）
     * 计算出每一个点的信号强度，即电频强度
     *
     * @param real 实部
     * @param imag 虚部
     * @param n    多少个数进行FFT,n必须为2的指数倍数
     * @return
     */
    private fun fft(real: DoubleArray, imag: DoubleArray, n: Int): Int {
        var i: Int
        var j: Int
        var l: Int
        var k: Int
        var ip: Int
        var M = 0
        var le: Int
        var le2: Int
        var sR: Double
        var sI: Double
        var tR: Double
        var tI: Double
        var uR: Double
        var uI: Double

        M = (Math.log(n.toDouble()) / Math.log(2.0)).toInt()

        // bit reversal sorting
        l = n / 2
        j = l
        // 控制反转，排序，从低频到高频
        i = 1
        while (i <= n - 2) {
            if (i < j) {
                tR = real[j]
                tI = imag[j]
                real[j] = real[i]
                imag[j] = imag[i]
                real[i] = tR
                imag[i] = tI
            }
            k = l
            while (k <= j) {
                j = j - k
                k = k / 2
            }
            j = j + k
            i++
        }
        // For Loops
        l = 1
        while (l <= M) { /* loop for ceil{log2(N)} */
            le = Math.pow(2.0, l.toDouble()).toInt()
            le2 = le / 2
            uR = 1.0
            uI = 0.0
            sR = Math.cos(Math.PI / le2)// cos和sin消耗大量的时间，可以用定值
            sI = -Math.sin(Math.PI / le2)
            j = 1
            while (j <= le2) { // loop for each sub DFT
                // jm1 = j - 1;
                i = j - 1
                while (i <= n - 1) {// loop for each butterfly
                    ip = i + le2
                    tR = real[ip] * uR - imag[ip] * uI
                    tI = real[ip] * uI + imag[ip] * uR
                    real[ip] = real[i] - tR
                    imag[ip] = imag[i] - tI
                    real[i] += tR
                    imag[i] += tI
                    i += le
                } // Next i
                tR = uR
                uR = tR * sR - uI * sI
                uI = tR * sI + uI * sR
                j++
            } // Next j
            l++
        } // Next l

        return 0
    }
}