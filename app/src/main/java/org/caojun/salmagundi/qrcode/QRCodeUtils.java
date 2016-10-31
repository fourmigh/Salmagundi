package org.caojun.salmagundi.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.caojun.salmagundi.color.Color;
import org.caojun.salmagundi.color.ColorUtils;

import java.util.Hashtable;

/**
 * 二维码工具类
 * Created by CaoJun on 2016/10/27.
 */

public class QRCodeUtils {

    /**
     * 增加渐变色的二维码
     * @param text
     * @param width
     * @param height
     * @param directColor 0：左到右，1：上到下，2：左上到右下，3：外到内，4：内到外
     * @return
     */
    public static Bitmap createQRImage(String text, int width, int height, int directColor)
    {
        try
        {
            if (TextUtils.isEmpty(text) || width < 1 || height < 1)
            {
                return null;
            }
            int step = 0;
            switch(directColor)
            {
                case 0:
                    step = width;
                    break;
                case 1:
                    step = height;
                    break;
                case 2:
                    step = width + height - 1;
                    break;
                case 3:
                case 4:
                    int max = Math.max(width, height);
                    step = max / 2;
                    if(max % 2 > 0)
                    {
                        step ++;
                    }
                    break;
                default:
                    return createQRImage(text, width, height);
            }
            Color[] colors = ColorUtils.getGradientColor(new Color(0xFF,0,0), new Color(0,0,0xFF), step);
            if(colors == null)
            {
                return null;
            }

            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
//                        pixels[y * width + x] = 0xff000000;
                        switch(directColor)
                        {
                            case 0:
                                pixels[y * width + x] = colors[x].toInt();
                                break;
                            case 1:
                                pixels[y * width + x] = colors[y].toInt();
                                break;
                            case 2:
                                pixels[y * width + x] = colors[x + y].toInt();
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                        }
                    }
                    else
                    {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createQRImage(String text, int width, int height)
    {
        try
        {
            if (TextUtils.isEmpty(text) || width < 1 || height < 1)
            {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * width + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加Logo后无法识别
     * @param src
     * @param logo
     * @return
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);

            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }
}
