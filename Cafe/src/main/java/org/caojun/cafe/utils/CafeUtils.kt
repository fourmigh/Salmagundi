package org.caojun.cafe.utils

import org.caojun.cafe.R

object CafeUtils {
    enum class Material {
        Espresso,//意式咖啡
        RoastedCoffee,//烘焙咖啡
        InstantCoffee,//速溶咖啡
        FrenchCoffee,//法压咖啡
        WarmMilk,//温牛奶
        HotMilk,//热牛奶
        Foam,//奶泡
        FoamMany,//大量奶泡
        IceCake,//冰块
        HotWater,//热水
        IceWater,//冰水
        Sugar,//糖
        BrownSugar,//黄糖
        CondensedMilk,//炼乳
        IceCreamBall,//冰激凌球
        Syrup,//糖水
        CoffeeFoam,//咖啡泡沫
        Lemon,//柠檬片
        LemonJuice,//柠檬汁
        Cream,//奶油
        WhippedCream,//打发奶油
        IrishWhiskey,//爱尔兰威士忌
        Cocoa,//可可粉
        SpiceBlackTea,//香料红茶
        HotChocolate,//热巧克力
        Cinnamon,//肉桂粉
        Flavedo,//橙皮碎
    }

    enum class Cafe {
        Espresso,//意式咖啡
        Coffee,//牛奶咖啡
        Cortadito,//告尔多咖啡
        CaffeLatte,//拿铁咖啡
        CaPheSuaDa,//越南冰咖啡
        Macchiato,//玛奇朵
        Affogato,//阿芙佳朵
        Galao,//葡式嘎漏咖啡
        Frappe,//希腊冰咖啡
        CafeDelTiempo,//冰柠檬咖啡
        Mazagran,//葡式柠檬咖啡
        IrishCoffee,//爱尔兰咖啡
        Cappucino,//卡布奇诺
        FlatWhite,//白咖啡（馥芮白）
        Espressiono,//浓缩咖啡
        LongBlack,//黑咖啡
        Breve,//半拿铁
        CafeCubano,//古巴咖啡
        Bonbon,//意式炼乳咖啡
        CafeConHielo,//西班牙冰咖啡
        EspressoRomano,//罗马咖啡
        DirtyChaiLatte,//咖啡茶拿铁
        Vienna,//维也纳咖啡
        Americano,//美式咖啡
        Mocha,//摩卡咖啡
        Borgia,//波奇亚咖啡
    }

    private object Formula {

        val arrays = HashMap<Cafe, Array<Material>>()

        init {
            arrays[Cafe.Espresso] = arrayOf(Material.Espresso)
            arrays[Cafe.Coffee] = arrayOf(Material.Espresso, Material.WarmMilk, Material.Foam)
            arrays[Cafe.Cortadito] = arrayOf(Material.BrownSugar, Material.Espresso, Material.WarmMilk, Material.Foam)
            arrays[Cafe.CaffeLatte] = arrayOf(Material.Espresso, Material.HotMilk)
            arrays[Cafe.CaPheSuaDa] = arrayOf(Material.CondensedMilk, Material.Sugar, Material.RoastedCoffee, Material.IceCake)
            arrays[Cafe.Macchiato] = arrayOf(Material.Espresso, Material.Foam)
            arrays[Cafe.Affogato] = arrayOf(Material.IceCreamBall, Material.Espresso)
            arrays[Cafe.Galao] = arrayOf(Material.Espresso, Material.FoamMany)
            arrays[Cafe.Frappe] = arrayOf(Material.InstantCoffee, Material.Syrup, Material.IceWater, Material.IceCake, Material.CoffeeFoam)
            arrays[Cafe.CafeDelTiempo] = arrayOf(Material.Espresso, Material.IceCake, Material.Lemon)
            arrays[Cafe.Mazagran] = arrayOf(Material.BrownSugar, Material.FrenchCoffee, Material.LemonJuice, Material.IceCake)
            arrays[Cafe.IrishCoffee] = arrayOf(Material.BrownSugar, Material.FrenchCoffee, Material.IrishWhiskey, Material.Cream)
            arrays[Cafe.Cappucino] = arrayOf(Material.Espresso, Material.HotMilk, Material.Foam)
            arrays[Cafe.FlatWhite] = arrayOf(Material.Espresso, Material.HotMilk)
            arrays[Cafe.Espressiono] = arrayOf(Material.Espresso, Material.HotMilk, Material.Cocoa)
            arrays[Cafe.LongBlack] = arrayOf(Material.HotWater, Material.Espresso)
            arrays[Cafe.Breve] = arrayOf(Material.Espresso, Material.HotMilk, Material.Cream, Material.Foam)
            arrays[Cafe.CafeCubano] = arrayOf(Material.BrownSugar, Material.Espresso)
            arrays[Cafe.Bonbon] = arrayOf(Material.CondensedMilk, Material.Espresso)
            arrays[Cafe.CafeConHielo] = arrayOf(Material.IceCake, Material.Espresso)
            arrays[Cafe.EspressoRomano] = arrayOf(Material.Espresso, Material.Lemon)
            arrays[Cafe.DirtyChaiLatte] = arrayOf(Material.Espresso, Material.SpiceBlackTea, Material.HotMilk, Material.Foam)
            arrays[Cafe.Vienna] = arrayOf(Material.Espresso, Material.WhippedCream)
            arrays[Cafe.Americano] = arrayOf(Material.Espresso, Material.HotWater)
            arrays[Cafe.Mocha] = arrayOf(Material.Espresso, Material.HotChocolate, Material.WhippedCream)
            arrays[Cafe.Borgia] = arrayOf(Material.Espresso, Material.HotChocolate, Material.WhippedCream, Material.Cinnamon, Material.Flavedo)
        }
    }

    fun getMaterial(cafe: Cafe): Array<Material>? {
        if (cafe in Formula.arrays) {
            return Formula.arrays[cafe]
        }
        return null
    }


    fun getCafe(materials: List<Material>): Cafe? {
        for (cafe in Formula.arrays.keys) {
            if (isCafe(Formula.arrays[cafe], materials)) {
                return cafe
            }
        }
        return null
    }

    private fun isCafe(cafe: Array<Material>?, materials: List<Material>): Boolean {
        if (cafe == null) {
            return false
        }
        if (cafe.size != materials.size) {
            return false
        }
        for (i in cafe.indices) {
            if (cafe[i] in materials) {
                continue
            } else {
                return false
            }
        }
        return true
    }

    fun getResId(material: CafeUtils.Material): Int {
        return when (material) {
            CafeUtils.Material.Espresso -> R.string.espresso//意式咖啡
            CafeUtils.Material.RoastedCoffee -> R.string.roasted_coffee//烘焙咖啡
            CafeUtils.Material.InstantCoffee -> R.string.instant_coffee//速溶咖啡
            CafeUtils.Material.FrenchCoffee -> R.string.french_coffee//法压咖啡
            CafeUtils.Material.WarmMilk -> R.string.warm_milk//温牛奶
            CafeUtils.Material.HotMilk -> R.string.hot_milk//热牛奶
            CafeUtils.Material.Foam -> R.string.foam//奶泡
            CafeUtils.Material.FoamMany -> R.string.foam_many//大量奶泡
            CafeUtils.Material.IceCake -> R.string.ice_cake//冰块
            CafeUtils.Material.HotWater -> R.string.hot_water//热水
            CafeUtils.Material.IceWater -> R.string.ice_water//冰水
            CafeUtils.Material.Sugar -> R.string.sugar//糖
            CafeUtils.Material.BrownSugar -> R.string.brown_sugar//黄糖
            CafeUtils.Material.CondensedMilk -> R.string.condensed_milk//炼乳
            CafeUtils.Material.IceCreamBall -> R.string.ice_cream_ball//冰激凌球
            CafeUtils.Material.Syrup -> R.string.syrup//糖水
            CafeUtils.Material.CoffeeFoam -> R.string.coffee_foam//咖啡泡沫
            CafeUtils.Material.Lemon -> R.string.lemon//柠檬片
            CafeUtils.Material.LemonJuice -> R.string.lemon_juice//柠檬汁
            CafeUtils.Material.Cream -> R.string.cream//奶油
            CafeUtils.Material.WhippedCream -> R.string.whipped_cream//打发奶油
            CafeUtils.Material.IrishWhiskey -> R.string.irish_coffee//爱尔兰威士忌
            CafeUtils.Material.Cocoa -> R.string.cocoa//可可粉
            CafeUtils.Material.SpiceBlackTea -> R.string.spice_black_tea//香料红茶
            CafeUtils.Material.HotChocolate -> R.string.hot_chocolate//热巧克力
            CafeUtils.Material.Cinnamon -> R.string.cinnamon//肉桂粉
            CafeUtils.Material.Flavedo -> R.string.flavedo//橙皮碎
        }
    }

    fun getResId(cafe: CafeUtils.Cafe): Int {
        return when (cafe) {
            CafeUtils.Cafe.Espresso -> R.string.espresso//意式咖啡
            CafeUtils.Cafe.Coffee -> R.string.coffee//牛奶咖啡
            CafeUtils.Cafe.Cortadito -> R.string.cortadito//告尔多咖啡
            CafeUtils.Cafe.CaffeLatte -> R.string.caffe_latte//拿铁咖啡
            CafeUtils.Cafe.CaPheSuaDa -> R.string.ca_phe_sua_da//越南冰咖啡
            CafeUtils.Cafe.Macchiato -> R.string.macchiato//玛奇朵
            CafeUtils.Cafe.Affogato -> R.string.affogato//阿芙佳朵
            CafeUtils.Cafe.Galao -> R.string.galao//葡式嘎漏咖啡
            CafeUtils.Cafe.Frappe -> R.string.frappe//希腊冰咖啡
            CafeUtils.Cafe.CafeDelTiempo -> R.string.cafe_del_tiempo//冰柠檬咖啡
            CafeUtils.Cafe.Mazagran -> R.string.mazagran//葡式柠檬咖啡
            CafeUtils.Cafe.IrishCoffee -> R.string.irish_coffee//爱尔兰咖啡
            CafeUtils.Cafe.Cappucino -> R.string.cappucino//卡布奇诺
            CafeUtils.Cafe.FlatWhite -> R.string.flat_white//白咖啡（馥芮白）
            CafeUtils.Cafe.Espressiono -> R.string.espressiono//浓缩咖啡
            CafeUtils.Cafe.LongBlack -> R.string.long_black//黑咖啡
            CafeUtils.Cafe.Breve -> R.string.breve//半拿铁
            CafeUtils.Cafe.CafeCubano -> R.string.cafe_cubano//古巴咖啡
            CafeUtils.Cafe.Bonbon -> R.string.bonbon//意式炼乳咖啡
            CafeUtils.Cafe.CafeConHielo -> R.string.cafe_con_hielo//西班牙冰咖啡
            CafeUtils.Cafe.EspressoRomano -> R.string.espresso_romano//罗马咖啡
            CafeUtils.Cafe.DirtyChaiLatte -> R.string.dirty_chai_latte//咖啡茶拿铁
            CafeUtils.Cafe.Vienna -> R.string.vienna//维也纳咖啡
            CafeUtils.Cafe.Americano -> R.string.americano//美式咖啡
            CafeUtils.Cafe.Mocha -> R.string.mocha//摩卡咖啡
            CafeUtils.Cafe.Borgia -> R.string.borgia//波奇亚咖啡
        }
    }
}