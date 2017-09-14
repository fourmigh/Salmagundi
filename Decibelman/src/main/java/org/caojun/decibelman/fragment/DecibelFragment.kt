package org.caojun.decibelman.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socks.library.KLog
import kotlinx.android.synthetic.main.fragment_decibel.*
import org.caojun.decibelman.Decibelman
import org.caojun.decibelman.R
import org.caojun.decibelman.utils.AverageUtils


/**
 * Created by CaoJun on 2017/9/13.
 */
class DecibelFragment: Fragment() {

    private var decibelman: Decibelman? = null
    private var average: Float = 0f
    private var min: Int = Int.MAX_VALUE
    private var max: Int = Int.MIN_VALUE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_decibel, null)

        decibelman = Decibelman(object : Decibelman.OnDecibelListener {
            override fun onGetDecibel(decibel: Double) {
                velocimeterView.setValue(decibel.toFloat(), false)
                //数据统计
                average = AverageUtils.add(decibel.toInt())
                if (min > decibel) {
                    min = decibel.toInt()
                }
                if (max < decibel) {
                    max = decibel.toInt()
                }
                KLog.d("onGetDecibel", min.toString() + " : " + average.toString() + " : " + max.toString())
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
}