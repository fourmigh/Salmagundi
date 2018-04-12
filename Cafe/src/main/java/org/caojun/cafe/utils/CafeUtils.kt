package org.caojun.cafe.utils

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
}