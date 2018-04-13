package org.caojun.cafe.fragement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import kotlinx.android.synthetic.main.fragment_cafe.*
import org.caojun.cafe.R
import org.caojun.cafe.utils.CafeUtils
import org.caojun.cafe.utils.GameUtils

class CafeFragment: Fragment() {

    private var lastCheckedRadioButtonId = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_cafe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        refreshData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            lastCheckedRadioButtonId = rgCafe.checkedRadioButtonId
        } else {
            refreshData()
        }
    }

    private fun refreshData() {
        rgMaterial.removeAllViews()
        rgCafe.removeAllViews()
        for (cafe in CafeUtils.Cafe.values()) {
            val resId = CafeUtils.getResId(cafe)
            if (resId > 0) {
                val radioButton = RadioButton(context)
                radioButton.setText(resId)

                radioButton.isEnabled = GameUtils.isGained(context, cafe)

                rgCafe.addView(radioButton)
                radioButton.setOnCheckedChangeListener { _, isChecked ->
                    if (!isChecked) {
                        return@setOnCheckedChangeListener
                    }
                    rgMaterial.removeAllViews()

                    val materials = CafeUtils.getMaterialsResIds(cafe) ?: return@setOnCheckedChangeListener
                    for (i in materials.indices) {
                        val checkBox = CheckBox(context)
                        checkBox.isChecked = true
                        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (!isChecked) {
                                buttonView.isChecked = true
                            }
                        }
                        checkBox.setText(materials[i])
                        rgMaterial.addView(checkBox)
                    }
                }
            }
        }
    }
}