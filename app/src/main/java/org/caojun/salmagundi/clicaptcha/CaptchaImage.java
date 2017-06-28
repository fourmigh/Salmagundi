package org.caojun.salmagundi.clicaptcha;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;

/**
 * Created by CaoJun on 2017/6/26.
 */

public class CaptchaImage extends View {

    public static final int SIZE = 100;

    private Random mRandom;
    private Paint linePaint, textPaint;
    private int mWidth, mHeight;
    private Bitmap mBitmap, mCodeBitmap;
    //将图片划分成4*3个小格
    private static final int WIDTH = 4;
    private static final int HEIGHT = 3;
    //小格相交的总的点数
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private String code;

    public CaptchaImage(Context context) {
        this(context, null);
    }

    public CaptchaImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptchaImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        refresh();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    public String getCode() {
        return this.code;
    }

    private void initView() {
        mWidth = SIZE;
        mHeight = SIZE;

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void refresh() {
        code = CaptchaUtils.getRandomChar();
        mRandom = new Random();
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        int size = (int)(SIZE * Math.min(Math.random() + 0.6, 0.9));
        textPaint.setTextSize(size);
        textPaint.setShadowLayer(5,3,3,0xFF999999);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextScaleX(0.8F);
        textPaint.setColor(Color.GREEN);
        textPaint.setTextAlign(Paint.Align.CENTER);

        createCodeBitmap();
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mCodeBitmap,0,0,null);
    }

    private void createCodeBitmap() {
        mBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mBitmap);

        int offsetDegree = mRandom.nextInt(15);
        // 这里只会产生0和1，如果是1那么正旋转正角度，否则旋转负角度
        offsetDegree = mRandom.nextInt(2) == 1?offsetDegree:-offsetDegree;
        mCanvas.save();
        mCanvas.rotate(offsetDegree,mWidth/2,mHeight/2);
        // 给画笔设置随机颜色
        textPaint.setARGB(255, mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
        mCanvas.drawText(code, mWidth / 2, mHeight * 3 / 4, textPaint);
        mCanvas.restore();

        mCodeBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mCodeBitmap);

        int index = 0;
        float bitmapwidth = mBitmap.getWidth();
        float bitmapheight = mBitmap.getHeight();
        for(int i = 0;i < HEIGHT + 1;i++){
            float fy = bitmapheight / HEIGHT * i;
            for(int j = 0;j < WIDTH + 1;j++){
                float fx = bitmapwidth / WIDTH * j;
                //偶数位记录x坐标  奇数位记录Y坐标
                verts[index * 2] = fx;
                verts[index * 2 + 1] = fy;
                index++;
            }
        }
        //设置变形点，这些点将会影响变形的效果
        float offset = bitmapheight / HEIGHT / 3;
        verts[12] = verts[12] - offset;
        verts[13] = verts[13] + offset;
        verts[16] = verts[16] + offset;
        verts[17] = verts[17] - offset;
        verts[24] = verts[24] + offset;
        verts[25] = verts[25] + offset;

        // 对验证码图片进行扭曲变形
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
    }
}
