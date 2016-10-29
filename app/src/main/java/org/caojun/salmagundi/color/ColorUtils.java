package org.caojun.salmagundi.color;

/**
 * 颜色工具类
 * Created by CaoJun on 2016/10/28.
 */

public class ColorUtils {

    public static Color[] getGradientColor(Color start, Color end, int step)
    {
        if(start == null || end == null || step < 1)
        {
            return null;
        }
        if(step == 1)
        {
            return new Color[]{start, end};
        }
        Color colors[] = new Color[step];

        for(int i = 0;i < step;i ++)
        {
            int alpha = start.getAlpha() + (end.getAlpha() - start.getAlpha()) * i / step;
            int red = start.getRed() + (end.getRed() - start.getRed()) * i / step;
            int green = start.getGreen() + (end.getGreen() - start.getGreen()) * i / step;
            int blue = start.getBlue() + (end.getBlue() - start.getBlue()) * i / step;
            colors[i] = new Color(alpha, red, green, blue);
        }

        return colors;
    }
}
