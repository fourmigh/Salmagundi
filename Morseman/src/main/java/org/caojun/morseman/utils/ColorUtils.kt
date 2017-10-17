package org.caojun.morseman.utils

import android.graphics.Color

/**
 * Created by CaoJun on 2017/10/16.
 */
object ColorUtils {

    fun RGBtoHSL(r: Int, g: Int, b: Int, hsl: FloatArray) {
        val rf = r / 255f
        val gf = g / 255f
        val bf = b / 255f

        val max = Math.max(rf, Math.max(gf, bf))
        val min = Math.min(rf, Math.min(gf, bf))
        val deltaMaxMin = max - min

        val h: Float
        val s: Float
        val l = (max + min) / 2f

        if (max == min) {
            // Monochromatic
            s = 0f
            h = s
        } else {
            if (max == rf) {
                h = (gf - bf) / deltaMaxMin % 6f
            } else if (max == gf) {
                h = (bf - rf) / deltaMaxMin + 2f
            } else {
                h = (rf - gf) / deltaMaxMin + 4f
            }

            s = deltaMaxMin / (1f - Math.abs(2f * l - 1f))
        }

        hsl[0] = h * 60f % 360f
        hsl[1] = s
        hsl[2] = l
    }

    fun HSLtoRGB(hsl: FloatArray): Int {
        val h = hsl[0]
        val s = hsl[1]
        val l = hsl[2]

        val c = (1f - Math.abs(2 * l - 1f)) * s
        val m = l - 0.5f * c
        val x = c * (1f - Math.abs(h / 60f % 2f - 1f))

        val hueSegment = h.toInt() / 60

        var r = 0
        var g = 0
        var b = 0

        when (hueSegment) {
            0 -> {
                r = Math.round(255 * (c + m))
                g = Math.round(255 * (x + m))
                b = Math.round(255 * m)
            }
            1 -> {
                r = Math.round(255 * (x + m))
                g = Math.round(255 * (c + m))
                b = Math.round(255 * m)
            }
            2 -> {
                r = Math.round(255 * m)
                g = Math.round(255 * (c + m))
                b = Math.round(255 * (x + m))
            }
            3 -> {
                r = Math.round(255 * m)
                g = Math.round(255 * (x + m))
                b = Math.round(255 * (c + m))
            }
            4 -> {
                r = Math.round(255 * (x + m))
                g = Math.round(255 * m)
                b = Math.round(255 * (c + m))
            }
            5, 6 -> {
                r = Math.round(255 * (c + m))
                g = Math.round(255 * m)
                b = Math.round(255 * (x + m))
            }
        }

        r = Math.max(0, Math.min(255, r))
        g = Math.max(0, Math.min(255, g))
        b = Math.max(0, Math.min(255, b))

        return Color.rgb(r, g, b)
    }

    fun toHexEncoding(color: Int): String {
        var R: String
        var G: String
        var B: String
        val sb = StringBuffer()
        R = Integer.toHexString(android.graphics.Color.red(color))
        G = Integer.toHexString(android.graphics.Color.green(color))
        B = Integer.toHexString(android.graphics.Color.blue(color))
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = if (R.length == 1) "0" + R else R
        G = if (G.length == 1) "0" + G else G
        B = if (B.length == 1) "0" + B else B
        //        sb.append("#");
        sb.append(R)
        sb.append(G)
        sb.append(B)
        return sb.toString()
    }

    fun RGBtoHSV(r: Int, g: Int, b: Int, hsv: FloatArray) {
        val rf = r.toFloat()/* / 255f*/
        val gf = g.toFloat()/* / 255f*/
        val bf = b.toFloat()/* / 255f*/

        val max = Math.max(rf, Math.max(gf, bf))
        val min = Math.min(rf, Math.min(gf, bf))
        val deltaMaxMin = max - min

        var h: Float
        val s: Float

        if (max == min) {
            // Monochromatic
            s = 0f
            h = s
        } else {
            if (max == rf) {
                h = (gf - bf) / deltaMaxMin % 6f
            } else if (max == gf) {
                h = (bf - rf) / deltaMaxMin + 2f
            } else {
                h = (rf - gf) / deltaMaxMin + 4f
            }

            s = deltaMaxMin / max
        }

        h += 360f

        hsv[0] = h * 60f % 360f
        hsv[1] = s
        hsv[2] = max / 255
    }

    fun HSVtoRGB(hsv: FloatArray): Int {
        val h = hsv[0]
        val s = hsv[1]
        val v = hsv[2]

        val c = v * s / 10000
        val m = v / 100 - c
        val x = c * (1f - Math.abs(h / 60f % 2f - 1f))

        val hueSegment = h.toInt() / 60

        var r = 0f
        var g = 0f
        var b = 0f

        when (hueSegment) {
            0 -> {
                r = c
                g = x
                b = 0f
            }
            1 -> {
                r = x
                g = c
                b = 0f
            }
            2 -> {
                r = 0f
                g = c
                b = x
            }
            3 -> {
                r = 0f
                g = x
                b = c
            }
            4 -> {
                r = x
                g = 0f
                b = c
            }
            5, 6 -> {
                r = c
                g = 0f
                b = x
            }
        }

        r = (r + m) * 255
        g = (g + m) * 255
        b = (b + m) * 255

        return Color.rgb(Math.rint(r.toDouble()).toInt(), Math.rint(g.toDouble()).toInt(), Math.rint(b.toDouble()).toInt())
    }

    /**
     * 比较颜色
     *
     * @param colorOld
     * @param colorLast
     * @return
     */
    fun compareSpan(colorOld: Int, colorLast: Int): Int {
        val or = Color.red(colorOld)
        val og = Color.green(colorOld)
        val ob = Color.blue(colorOld)

        val lr = Color.red(colorLast)
        val lg = Color.green(colorLast)
        val lb = Color.blue(colorLast)
        return (Math.abs(or - lr) + Math.abs(og - lg) + Math.abs(ob - lb)) / 3
    }
}