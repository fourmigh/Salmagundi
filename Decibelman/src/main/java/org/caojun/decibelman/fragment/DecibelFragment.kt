package org.caojun.decibelman.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.caojun.decibelman.Decibelman
import org.caojun.decibelman.R
import org.caojun.library.VelocimeterView


/**
 * Created by CaoJun on 2017/9/13.
 */
class DecibelFragment: Fragment() {

    private var decibelman: Decibelman? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_decibel, null)

        val velocimeterView: VelocimeterView = view.findViewById(R.id.velocimeterView)

        decibelman = Decibelman(object : Decibelman.OnDecibelListener {
            override fun onGetDecibel(decibel: Double) {
                velocimeterView.setValue(decibel.toFloat(), false)
            }
        })

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