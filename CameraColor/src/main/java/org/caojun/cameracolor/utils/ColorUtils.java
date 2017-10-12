package org.caojun.cameracolor.utils;

import android.graphics.Color;

/**
 * Created by CaoJun on 2017/5/27.
 */

public class ColorUtils {

    public static void RGBtoHSL(int r, int g, int b, float[] hsl) {
        final float rf = r / 255f;
        final float gf = g / 255f;
        final float bf = b / 255f;

        final float max = Math.max(rf, Math.max(gf, bf));
        final float min = Math.min(rf, Math.min(gf, bf));
        final float deltaMaxMin = max - min;

        float h, s;
        float l = (max + min) / 2f;

        if (max == min) {
            // Monochromatic
            h = s = 0f;
        } else {
            if (max == rf) {
                h = ((gf - bf) / deltaMaxMin) % 6f;
            } else if (max == gf) {
                h = ((bf - rf) / deltaMaxMin) + 2f;
            } else {
                h = ((rf - gf) / deltaMaxMin) + 4f;
            }

            s = deltaMaxMin / (1f - Math.abs(2f * l - 1f));
        }

        hsl[0] = (h * 60f) % 360f;
        hsl[1] = s;
        hsl[2] = l;
    }

    public static int HSLtoRGB(float[] hsl) {
        final float h = hsl[0];
        final float s = hsl[1];
        final float l = hsl[2];

        final float c = (1f - Math.abs(2 * l - 1f)) * s;
        final float m = l - 0.5f * c;
        final float x = c * (1f - Math.abs((h / 60f % 2f) - 1f));

        final int hueSegment = (int) h / 60;

        int r = 0, g = 0, b = 0;

        switch (hueSegment) {
            case 0:
                r = Math.round(255 * (c + m));
                g = Math.round(255 * (x + m));
                b = Math.round(255 * m);
                break;
            case 1:
                r = Math.round(255 * (x + m));
                g = Math.round(255 * (c + m));
                b = Math.round(255 * m);
                break;
            case 2:
                r = Math.round(255 * m);
                g = Math.round(255 * (c + m));
                b = Math.round(255 * (x + m));
                break;
            case 3:
                r = Math.round(255 * m);
                g = Math.round(255 * (x + m));
                b = Math.round(255 * (c + m));
                break;
            case 4:
                r = Math.round(255 * (x + m));
                g = Math.round(255 * m);
                b = Math.round(255 * (c + m));
                break;
            case 5:
            case 6:
                r = Math.round(255 * (c + m));
                g = Math.round(255 * m);
                b = Math.round(255 * (x + m));
                break;
        }

        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        return Color.rgb(r, g, b);
    }

    public static String toHexEncoding(int color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
        R = Integer.toHexString(android.graphics.Color.red(color));
        G = Integer.toHexString(android.graphics.Color.green(color));
        B = Integer.toHexString(android.graphics.Color.blue(color));
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
//        sb.append("#");
        sb.append(R);
        sb.append(G);
        sb.append(B);
        return sb.toString();
    }

    public static void RGBtoHSV(int r, int g, int b, float[] hsv) {
        final float rf = r/* / 255f*/;
        final float gf = g/* / 255f*/;
        final float bf = b/* / 255f*/;

        final float max = Math.max(rf, Math.max(gf, bf));
        final float min = Math.min(rf, Math.min(gf, bf));
        final float deltaMaxMin = max - min;

        float h, s;
        float v = max;

        if (max == min) {
            // Monochromatic
            h = s = 0f;
        } else {
            if (max == rf) {
                h = ((gf - bf) / deltaMaxMin) % 6f;
            } else if (max == gf) {
                h = ((bf - rf) / deltaMaxMin) + 2f;
            } else {
                h = ((rf - gf) / deltaMaxMin) + 4f;
            }

            s = deltaMaxMin / max;
        }

        h += 360;

        hsv[0] = (h * 60f) % 360f;
        hsv[1] = s;
        hsv[2] = v / 255;
    }

    public static int HSVtoRGB(float[] hsv) {
        final float h = hsv[0];
        final float s = hsv[1];
        final float v = hsv[2];

        final float c = v * s / 10000;
        final float m = v / 100 - c;
        final float x = c * (1f - Math.abs((h / 60f % 2f) - 1f));

        final int hueSegment = (int) h / 60;

        float r = 0, g = 0, b = 0;

        switch (hueSegment) {
            case 0:
                r = c;
                g = x;
                b = 0;
                break;
            case 1:
                r = x;
                g = c;
                b = 0;
                break;
            case 2:
                r = 0;
                g = c;
                b = x;
                break;
            case 3:
                r = 0;
                g = x;
                b = c;
                break;
            case 4:
                r = x;
                g = 0;
                b = c;
                break;
            case 5:
            case 6:
                r = c;
                g = 0;
                b = x;
                break;
        }

        r = (r + m) * 255;
        g = (g + m) * 255;
        b = (b + m) * 255;

        return Color.rgb((int) Math.rint((double) r), (int) Math.rint((double) g), (int) Math.rint((double) b));
    }

    /**
     * 比较颜色
     *
     * @param colorOld
     * @param colorLast
     * @return
     */
    public static int compareSpan(int colorOld, int colorLast) {
        int or = Color.red(colorOld);
        int og = Color.green(colorOld);
        int ob = Color.blue(colorOld);

        int lr = Color.red(colorLast);
        int lg = Color.green(colorLast);
        int lb = Color.blue(colorLast);
        return (Math.abs(or - lr) + Math.abs(og - lg) + Math.abs(ob - lb)) / 3;
    }
}
