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
        Cappucina,//卡布奇诺
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

    fun getCafe(materials: List<Material>): Cafe? {
        when (materials.size) {
            1 -> {
                if (Material.Espresso in materials) {
                    return Cafe.Espresso
                }
            }
            2 -> {
                if (Material.Espresso in materials && Material.HotMilk in materials) {
                    return Cafe.CaffeLatte
                }
                if (Material.Espresso in materials && Material.Foam in materials) {
                    return Cafe.Macchiato
                }
                if (Material.Espresso in materials && Material.FoamMany in materials) {
                    //配方和Macchiato一样，仅Foam的量比较多
                    return Cafe.Galao
                }
                if (Material.IceCreamBall in materials && Material.Espresso in materials) {
                    return Cafe.Affogato
                }
                if (Material.Espresso in materials && Material.HotMilk in materials) {
                    return Cafe.FlatWhite
                }
                if (Material.HotWater in materials && Material.Espresso in materials) {
                    return Cafe.LongBlack
                }
                if (Material.BrownSugar in materials && Material.Espresso in materials) {
                    return Cafe.CafeCubano
                }
                if (Material.CondensedMilk in materials && Material.Espresso in materials) {
                    return Cafe.Bonbon
                }
                if (Material.IceCake in materials && Material.Espresso in materials) {
                    return Cafe.CafeConHielo
                }
                if (Material.Espresso in materials && Material.Lemon in materials) {
                    return Cafe.EspressoRomano
                }
                if (Material.Espresso in materials && Material.WhippedCream in materials) {
                    return Cafe.Vienna
                }
                if (Material.Espresso in materials && Material.HotWater in materials) {
                    return Cafe.Americano
                }
            }
            3 -> {
                if (Material.Espresso in materials && Material.WarmMilk in materials && Material.Foam in materials) {
                    return Cafe.Coffee
                }
                if (Material.Espresso in materials && Material.IceCake in materials && Material.Lemon in materials) {
                    return Cafe.CafeDelTiempo
                }
                if (Material.Espresso in materials && Material.HotMilk in materials && Material.Foam in materials) {
                    return Cafe.Cappucina
                }
                if (Material.Espresso in materials && Material.HotMilk in materials && Material.Cocoa in materials) {
                    return Cafe.Espressiono
                }
                if (Material.Espresso in materials && Material.HotChocolate in materials && Material.WhippedCream in materials) {
                    return Cafe.Mocha
                }
            }
            4 -> {
                if (Material.BrownSugar in materials && Material.Espresso in materials && Material.WarmMilk in materials && Material.Foam in materials) {
                    return Cafe.Cortadito
                }
                if (Material.CondensedMilk in materials && Material.Sugar in materials && Material.RoastedCoffee in materials && Material.IceCake in materials) {
                    return Cafe.CaPheSuaDa
                }
                if (Material.BrownSugar in materials && Material.FrenchCoffee in materials && Material.LemonJuice in materials && Material.IceCake in materials) {
                    return Cafe.Mazagran
                }
                if (Material.BrownSugar in materials && Material.FrenchCoffee in materials && Material.IrishWhiskey in materials && Material.Cream in materials) {
                    return Cafe.IrishCoffee
                }
                if (Material.Espresso in materials && Material.HotMilk in materials && Material.Cream in materials && Material.Foam in materials) {
                    return Cafe.Breve
                }
                if (Material.Espresso in materials && Material.SpiceBlackTea in materials && Material.HotMilk in materials && Material.Foam in materials) {
                    return Cafe.DirtyChaiLatte
                }
            }
            5 -> {
                if (Material.InstantCoffee in materials && Material.Syrup in materials && Material.IceWater in materials && Material.IceCake in materials && Material.CoffeeFoam in materials) {
                    return Cafe.Frappe
                }
                if (Material.Espresso in materials && Material.HotChocolate in materials && Material.WhippedCream in materials && Material.Cinnamon in materials && Material.Flavedo in materials) {
                    return Cafe.Borgia
                }
            }
        }
        return null
    }
}