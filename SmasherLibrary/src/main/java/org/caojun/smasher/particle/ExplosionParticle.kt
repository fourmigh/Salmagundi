package org.caojun.smasher.particle

import android.graphics.Rect
import java.util.*

class ExplosionParticle: Particle {

    constructor(color: Int, radius: Int, rect: Rect, endValue: Float, random: Random, horizontalMultiple: Float, verticalMultiple: Float) {

        this.color = color
        alpha = 1f

        // 参与横向变化参数和竖直变化参数计算，规则：横向参数相对值越大，竖直参数越小
        val nextFloat = random.nextFloat()

        baseRadius = getBaseRadius(radius.toFloat(), random, nextFloat)
        this.radius = baseRadius

        horizontalElement = getHorizontalElement(rect, random, nextFloat, horizontalMultiple)
        verticalElement = getVerticalElement(rect, random, nextFloat, verticalMultiple)

        val offsetX = rect.width() / 4
        val offsetY = rect.height() / 4

        // baseCx,baseCy在中心点四周的offset/2的范围内。
        baseCx = rect.centerX() + offsetX * (random.nextFloat() - 0.5f)
        baseCy = rect.centerY() + offsetY * (random.nextFloat() - 0.5f)
        cx = baseCx
        cy = baseCy


        font = endValue / 10 * random.nextFloat()
        later = 0.4f * random.nextFloat()
    }

    private fun getBaseRadius(radius: Float, random: Random, nextFloat: Float): Float {
        var r = radius + radius * (random.nextFloat() - 0.5f) * 0.5f
        r = if (nextFloat < 0.6f)
            r
        else if (nextFloat < 0.8f) r * 1.4f else r * 0.8f
        return r
    }

    private fun getHorizontalElement(rect: Rect, random: Random, nextFloat: Float, horizontalMultiple: Float): Float {

        // 第一次随机运算：h=width*±(0.01~0.49)
        var horizontal = rect.width() * (random.nextFloat() - 0.5f)

        // 第二次随机运行： h= 1/5概率：h；3/5概率：h*0.6; 1/5概率：h*0.3; nextFloat越大，h越小。
        horizontal = if (nextFloat < 0.2f)
            horizontal
        else if (nextFloat < 0.8f) horizontal * 0.6f else horizontal * 0.3f

        // 上面的计算是为了让横向变化参数有随机性，下面的计算是修改横向变化的幅度。
        return horizontal * horizontalMultiple
    }

    private fun getVerticalElement(rect: Rect, random: Random, nextFloat: Float, verticalMultiple: Float): Float {

        // 第一次随机运算： v=height*(0.5~1)
        var vertical = rect.height() * (random.nextFloat() * 0.5f + 0.5f)

        // 第二次随机运行： v= 1/5概率：v；3/5概率：v*1.2; 1/5概率：v*1.4; nextFloat越大，h越大。
        vertical = if (nextFloat < 0.2f)
            vertical
        else if (nextFloat < 0.8f) vertical * 1.2f else vertical * 1.4f

        // 上面的计算是为了让变化参数有随机性，下面的计算是变化的幅度。
        return vertical * verticalMultiple
    }


    override fun advance(factor: Float, endValue: Float) {

        // 动画进行到了几分之几
        var normalization = factor / endValue

        if (normalization < font || normalization > 1f - later) {
            alpha = 0f
            return
        }
        alpha = 1f

        // 粒子可显示的状态中，动画实际进行到了几分之几
        normalization = (normalization - font) / (1f - font - later)
        // 动画超过7/10，则开始逐渐变透明
        if (normalization >= 0.7f) {
            alpha = 1f - (normalization - 0.7f) / 0.3f
        }

        val realValue = normalization * endValue

        // y=j+k*x，j、k都是常数，x为 0~1.4
        cx = baseCx + horizontalElement * realValue

        // y=j+k*(x*(x-1)，j、k都是常数，x为 0~1.4
        cy = baseCy + verticalElement * (realValue * (realValue - 1))

        radius = baseRadius + baseRadius / 4 * realValue

    }
}