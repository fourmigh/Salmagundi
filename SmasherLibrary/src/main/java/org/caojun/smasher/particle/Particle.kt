package org.caojun.smasher.particle

abstract class Particle {

    var color = 0  // 颜色
    var radius = 0f  // 半径
    var alpha = 0f   // 透明度（0~1）
    var cx = 0f // 圆心 x
    var cy = 0f // 圆心 y


    var horizontalElement = 0f   // 水平变化参数
    var verticalElement = 0f // 垂直变化参数

    var baseRadius = 0f  // 初始半径，同时负责半径大小变化
    var baseCx = 0f // 初始圆心 x
    var baseCy = 0f // 初始圆心 y

    var font = 0f    // 决定了粒子在动画开始多久之后，开始显示
    var later = 0f   // 决定了粒子动画结束前多少时间开始隐藏

    open fun advance(factor: Float, endValue: Float) {}
}