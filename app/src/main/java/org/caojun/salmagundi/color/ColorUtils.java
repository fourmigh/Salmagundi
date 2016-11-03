package org.caojun.salmagundi.color;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.caojun.salmagundi.utils.DataStorageUtils;

/**
 * 颜色工具类
 * Created by CaoJun on 2016/10/28.
 */

public class ColorUtils {

    public static Color[] getSavedColors(Context context)
    {
        Integer[] intColorStart = DataStorageUtils.loadIntArray(context, "GradientColor", "colorStart", 0);
        if(intColorStart == null)
        {
            intColorStart = new Integer[]{0xFF, 0xFF, 0, 0};
        }
        Integer[] intColorEnd = DataStorageUtils.loadIntArray(context, "GradientColor", "colorEnd", 0);
        if(intColorEnd == null)
        {
            intColorEnd = new Integer[]{0xFF, 0, 0, 0xFF};
        }
        Color colorStart = new Color(intColorStart[0], intColorStart[1], intColorStart[2], intColorStart[3]);
        Color colorEnd = new Color(intColorEnd[0], intColorEnd[1], intColorEnd[2], intColorEnd[3]);
        return new Color[]{colorStart, colorEnd};
    }

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

    public static Bitmap createGradientImage(Color[] colors, int width, int height)
    {
        if(width <= 1 || height <= 1)
        {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        try {
            int w = width / colors.length;
            Canvas canvas = new Canvas(bitmap);
            for(int i = 0;i < colors.length;i ++)
            {
                Paint paint = new Paint();
                paint.setARGB(colors[i].getAlpha(), colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue());
                canvas.drawRect(i * w, 0, (i + 1) * w, height, paint);
            }
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }
}
