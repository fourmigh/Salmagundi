package org.caojun.cafe.fragement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import kotlinx.android.synthetic.main.fragment_material.*
import org.caojun.cafe.R
import org.caojun.cafe.utils.CafeUtils

class MaterialFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_material, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val materials = ArrayList<CafeUtils.Material>()
        for (material in CafeUtils.Material.values()) {
            val checkBox = CheckBox(context)
            val resId = CafeUtils.getResId(material)
            if (resId > 0) {
                checkBox.setText(resId)
                rgMaterial.addView(checkBox)

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        materials.add(material)
                    } else {
                        materials.remove(material)
                    }
                    val cafeId = CafeUtils.getCafeResId(materials)
                    if (cafeId > 0) {
                        tvCafe.setText(cafeId)
                    } else {
                        tvCafe.text = null
                    }
                }
            }
        }
    }
}