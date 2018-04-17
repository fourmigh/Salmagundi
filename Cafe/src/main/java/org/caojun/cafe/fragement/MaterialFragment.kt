package org.caojun.cafe.fragement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import kotlinx.android.synthetic.main.fragment_material.*
import org.caojun.cafe.R
import org.caojun.cafe.utils.CafeUtils
import org.caojun.cafe.utils.GameUtils
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class MaterialFragment: BaseFragment() {

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
                    tvCafe.text = null
                    if (isChecked) {
                        materials.add(material)
                    } else {
                        materials.remove(material)
                    }
                    val cafe = CafeUtils.getCafe(materials) ?: return@setOnCheckedChangeListener
                    val cafeId = CafeUtils.getResId(cafe)
                    if (cafeId <= 0) {
                        return@setOnCheckedChangeListener
                    }
                    tvCafe.setText(cafeId)

                    val imgResId = CafeUtils.getImageResId(cafe)
                    ivCafe.setImageResource(imgResId)
                    ivCafe.visibility = View.VISIBLE
                    ivFull.setImageResource(imgResId)

                    if (!GameUtils.isGained(context, cafe)) {
                        activity.alert(getString(R.string.you_found, getString(cafeId))) {
                            yesButton {
                                GameUtils.gain(context, cafe)

                            }
                            isCancelable = false
                        }.show()
                    }

                    ivCafe.setOnLongClickListener {
                        doBaike(getString(cafeId))
                        true
                    }
                }
            }
        }

        ivCafe.setOnClickListener({
            ivFull.visibility = View.VISIBLE
            ivFull.setOnClickListener({
                ivFull.visibility = View.GONE
            })
        })
    }
}