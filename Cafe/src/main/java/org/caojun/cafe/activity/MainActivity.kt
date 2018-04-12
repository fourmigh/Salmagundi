package org.caojun.cafe.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.cafe.R
import org.caojun.cafe.utils.CafeUtils
import org.caojun.cafe.utils.CafeUtils.Material
import org.caojun.cafe.utils.CafeUtils.Cafe

class MainActivity : AppCompatActivity() {

    private val materials = ArrayList<Material>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun getResId(material: Material): Int {
        return when (material) {
            Material.Espresso -> R.string.espresso//意式咖啡
            Material.RoastedCoffee -> R.string.roasted_coffee//烘焙咖啡
            Material.InstantCoffee -> R.string.instant_coffee//速溶咖啡
            Material.FrenchCoffee -> R.string.french_coffee//法压咖啡
            Material.WarmMilk -> R.string.warm_milk//温牛奶
            Material.HotMilk -> R.string.hot_milk//热牛奶
            Material.Foam -> R.string.foam//奶泡
            Material.FoamMany -> R.string.foam_many//大量奶泡
            Material.IceCake -> R.string.ice_cake//冰块
            Material.HotWater -> R.string.hot_water//热水
            Material.IceWater -> R.string.ice_water//冰水
            Material.Sugar -> R.string.sugar//糖
            Material.BrownSugar -> R.string.brown_sugar//黄糖
            Material.CondensedMilk -> R.string.condensed_milk//炼乳
            Material.IceCreamBall -> R.string.ice_cream_ball//冰激凌球
            Material.Syrup -> R.string.syrup//糖水
            Material.CoffeeFoam -> R.string.coffee_foam//咖啡泡沫
            Material.Lemon -> R.string.lemon//柠檬片
            Material.LemonJuice -> R.string.lemon_juice//柠檬汁
            Material.Cream -> R.string.cream//奶油
            Material.WhippedCream -> R.string.whipped_cream//打发奶油
            Material.IrishWhiskey -> R.string.irish_coffee//爱尔兰威士忌
            Material.Cocoa -> R.string.cocoa//可可粉
            Material.SpiceBlackTea -> R.string.spice_black_tea//香料红茶
            Material.HotChocolate -> R.string.hot_chocolate//热巧克力
            Material.Cinnamon -> R.string.cinnamon//肉桂粉
            Material.Flavedo -> R.string.flavedo//橙皮碎
        }
        return -1
    }

    private fun getResId(cafe: Cafe): Int {
        return when (cafe) {
            Cafe.Espresso -> R.string.espresso//意式咖啡
            Cafe.Coffee -> R.string.coffee//牛奶咖啡
            Cafe.Cortadito -> R.string.cortadito//告尔多咖啡
            Cafe.CaffeLatte -> R.string.caffe_latte//拿铁咖啡
            Cafe.CaPheSuaDa -> R.string.ca_phe_sua_da//越南冰咖啡
            Cafe.Macchiato -> R.string.macchiato//玛奇朵
            Cafe.Affogato -> R.string.affogato//阿芙佳朵
            Cafe.Galao -> R.string.galao//葡式嘎漏咖啡
            Cafe.Frappe -> R.string.frappe//希腊冰咖啡
            Cafe.CafeDelTiempo -> R.string.cafe_del_tiempo//冰柠檬咖啡
            Cafe.Mazagran -> R.string.mazagran//葡式柠檬咖啡
            Cafe.IrishCoffee -> R.string.irish_coffee//爱尔兰咖啡
            Cafe.Cappucina -> R.string.cappucina//卡布奇诺
            Cafe.FlatWhite -> R.string.flat_white//白咖啡（馥芮白）
            Cafe.Espressiono -> R.string.espressiono//浓缩咖啡
            Cafe.LongBlack -> R.string.long_black//黑咖啡
            Cafe.Breve -> R.string.breve//半拿铁
            Cafe.CafeCubano -> R.string.cafe_cubano//古巴咖啡
            Cafe.Bonbon -> R.string.bonbon//意式炼乳咖啡
            Cafe.CafeConHielo -> R.string.cafe_con_hielo//西班牙冰咖啡
            Cafe.EspressoRomano -> R.string.espresso_romano//罗马咖啡
            Cafe.DirtyChaiLatte -> R.string.dirty_chai_latte//咖啡茶拿铁
            Cafe.Vienna -> R.string.vienna//维也纳咖啡
            Cafe.Americano -> R.string.americano//美式咖啡
            Cafe.Mocha -> R.string.mocha//摩卡咖啡
            Cafe.Borgia -> R.string.borgia//波奇亚咖啡
        }
        return -1
    }

    private fun initView() {
        for (material in Material.values()) {
            val checkBox = CheckBox(this)
            val resId = getResId(material)
            if (resId > 0) {
                checkBox.setText(resId)
                rgMaterial.addView(checkBox)

                checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (isChecked) {
                            materials.add(material)
                        } else {
                            materials.remove(material)
                        }
                        val cafe = CafeUtils.getCafe(materials)
                        tvCafe.text = null
                        if (cafe != null) {
                            val cafeId = getResId(cafe)
                            if (cafeId > 0) {
                                tvCafe.setText(cafeId)
                            }
                        }
                    }
                })
            }
        }
    }
}
