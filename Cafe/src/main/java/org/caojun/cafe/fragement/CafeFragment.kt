package org.caojun.cafe.fragement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import kotlinx.android.synthetic.main.fragment_cafe.*
import org.caojun.cafe.R
import org.caojun.cafe.utils.CafeUtils

class CafeFragment: Fragment() {

    private val cafes = ArrayList<CafeUtils.Cafe>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_cafe, container, false)
        initView()
        return view
    }

    private fun initView() {
    }
}