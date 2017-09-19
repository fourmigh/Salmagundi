package org.caojun.decibelman.fragment

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Description
import kotlinx.android.synthetic.main.fragment_decibel.*
import org.caojun.decibelman.Decibelman
import org.caojun.decibelman.R
import org.caojun.decibelman.utils.AverageUtils
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.Legend.LegendForm
import org.caojun.decibelman.Constant


/**
 * Created by CaoJun on 2017/9/13.
 */
class DecibelFragment: Fragment() {

    private var decibelman: Decibelman? = null
//    private var average: Float = 0f
//    private var min: Int = Int.MAX_VALUE
//    private var max: Int = Int.MIN_VALUE
    private var chartInited = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_decibel, null)

        decibelman = Decibelman(object : Decibelman.OnDecibelListener {
            override fun onGetDecibel(decibel: Double) {
                velocimeterView?.setValue(decibel.toFloat(), false)
                //数据统计
                Constant.average = AverageUtils.add(decibel.toInt())
                //四舍五入
                val value = decibel + 0.5
                if (Constant.min > value) {
                    Constant.min = value.toInt()
                }
                if (Constant.max < value) {
                    Constant.max = value.toInt()
                }
                addEntry(value.toFloat())
            }
        })

        AverageUtils.init()
        return view
    }

    override fun onResume() {
        super.onResume()
        decibelman!!.start()
    }

    override fun onPause() {
        super.onPause()
        decibelman!!.stop()
    }

    private fun initChart() {

        val description = Description()
        description.text = getString(R.string.decibel_description)

        chart.description = description

        chart.setTouchEnabled(true)

        // 可拖曳
        chart.isDragEnabled = true

        // 可缩放
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)

        chart.setPinchZoom(true)

        // 设置图表的背景颜色
        chart.setBackgroundColor(Color.LTGRAY)

        val data = LineData()

        // 数据显示的颜色
        data.setValueTextColor(Color.WHITE)

        // 先增加一个空的数据，随后往里面动态添加
        chart.data = data

        // 图表的注解(只有当数据集存在时候才生效)
        val l = chart.legend

        // 可以修改图表注解部分的位置
        // l.setPosition(LegendPosition.LEFT_OF_CHART);

        // 线性，也可是圆
        l.form = LegendForm.LINE

        // 颜色
        l.textColor = Color.WHITE

        // x坐标轴
        val xl = chart.xAxis
        xl.textColor = Color.WHITE
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)

        // 如果false，那么x坐标轴将不可见
        xl.isEnabled = true

        // 将X坐标轴放置在底部，默认是在顶部。
        xl.position = XAxisPosition.BOTTOM

        // 图表左边的y坐标轴线
        val leftAxis = chart.axisLeft
        leftAxis.textColor = Color.WHITE

        // 最大值
        leftAxis.axisMaximum = velocimeterView.max

        // 最小值
        leftAxis.axisMinimum = velocimeterView.min

        leftAxis.setDrawGridLines(true)

        val rightAxis = chart.axisRight
        // 不显示图表的右边y坐标轴线
        rightAxis.isEnabled = false

        chart.invalidate()
    }

    private fun createSet(): LineDataSet {

        val set = LineDataSet(null, getString(R.string.decibel))
        set.lineWidth = 2.5f
        set.circleRadius = 4.5f
        set.color = Color.rgb(240, 99, 99)
        set.setCircleColor(Color.rgb(240, 99, 99))
        set.highLightColor = Color.rgb(190, 190, 190)
        set.axisDependency = AxisDependency.LEFT
        set.valueTextSize = 10f

        return set
    }

    private fun addEntry(value: Float) {

        if (!chartInited) {
            initChart()
            chartInited = true
        }

        val data = chart.data

        var set: ILineDataSet? = data.getDataSetByIndex(0)
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet()
            data.addDataSet(set)
        }

        // choose a random dataSet
        val randomDataSetIndex = (Math.random() * data.dataSetCount).toInt()

        data.addEntry(Entry(data.getDataSetByIndex(randomDataSetIndex).entryCount.toFloat(), value), randomDataSetIndex)
        data.notifyDataChanged()

        // let the chart know it's data has changed
        chart.notifyDataSetChanged()

        chart.setVisibleXRangeMaximum(6f)

        // this automatically refreshes the chart (calls invalidate())
        chart.moveViewTo((data.entryCount - 7).toFloat(), 50f, AxisDependency.LEFT)
    }
}