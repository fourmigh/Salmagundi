package org.caojun.salmagundi.svgmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import org.caojun.salmagundi.R;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author: 蜗牛
 * @date: 2017/6/1 10:11
 * @desc:
 */

public class MapView extends View {
    private Paint mPaint;

    //手势监听器
    private GestureDetector mDetector;
    //缩放系数
//    private float scale=1.3f;
    private float scale = 1f;
    //保存path对象
    private List<PathItem> pathItems = new ArrayList<>();
    private String TAG = "MapView";

    private ScaleGestureDetector mScaleGestureDetector;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private float dX = 0, dY = 0;
    private float lastX = 0, lastY = 0;

    private void init() {
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                float x = e.getX() / scale;
                float y = e.getY() / scale;
                x -= dX;
                y -= dY;
                for (PathItem pathItem : pathItems) {
                    pathItem.setSelect(pathItem.isTouch((int) x, (int) y));
                }
                invalidate();
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                dX = lastX + distanceX;
                dY = lastY + distanceY;
                lastX = dX;
                lastY = dY;
                dX = -dX;
                dY = -dY;
                invalidate();
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                center();
                invalidate();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                return super.onContextClick(e);
            }
        });
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {

            private float preScale = scale;// 默认前一次缩放比例为1

            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                float previousSpan = scaleGestureDetector.getPreviousSpan();
                float currentSpan = scaleGestureDetector.getCurrentSpan();
//                if (currentSpan < previousSpan) {
//                    // 缩小
//                    scale = preScale - (previousSpan - currentSpan) / 1000;
//                } else {
//                    // 放大
//                    scale = preScale + (currentSpan - previousSpan) / 1000;
//                }
                scale = preScale + (currentSpan - previousSpan) / 1000;
                invalidate();
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
                preScale = scale;//记录本次缩放比例
            }
        });
        parserPaths();
    }

    /**
     * 解析path
     */
    private void parserPaths() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                // 创建DOM工厂对象
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    // DocumentBuilder对象
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    //打开输入流
//                    InputStream is = getResources().openRawResource(R.raw.taiwan_maps);
                    InputStream is = getResources().openRawResource(R.raw.china_maps);
                    // 获取文档对象
                    Document doc = db.parse(is);
                    //获取path元素节点集合
                    NodeList paths = doc.getElementsByTagName("path");
                    PathItem item;
                    for (int i = 0; i < paths.getLength(); i++) {
                        // 取出每一个元素
                        Element personNode = (Element) paths.item(i);
                        //得到android:pathData属性值
                        String nodeValue = personNode.getAttribute("android:pathData");
                        //解析，并创建pathItem
                        item = new PathItem(PathParser.createPathFromPathData(nodeValue));
                        pathItems.add(item);

                        //统计整体尺寸
                        RectF rf = item.getRectF();
                        if (rf.left < rectF.left) {
                            rectF.left = rf.left;
                        }
                        if (rf.top < rectF.top) {
                            rectF.top = rf.top;
                        }
                        if (rf.right > rectF.right) {
                            rectF.right = rf.right;
                        }
                        if (rf.bottom > rectF.bottom) {
                            rectF.bottom = rf.bottom;
                        }
                    }
                    Log.e(TAG, "蜗牛   itemsCount  " + pathItems.size());
                    Log.e("RectF", "rectF.left: " + rectF.left);
                    Log.e("RectF", "rectF.top: " + rectF.top);
                    Log.e("RectF", "rectF.right: " + rectF.right);
                    Log.e("RectF", "rectF.bottom: " + rectF.bottom);
                } catch (Exception e) {
                    Log.e(TAG, "蜗牛   解析出错 ");
                }
//            }
//        }).start();
//        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event) && mDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(scale, scale);
        canvas.translate(dX, dY);
        for (PathItem pathItem : pathItems) {
            pathItem.draw(canvas, mPaint);
        }
        canvas.restore();
    }

    RectF rectF = new RectF();
    private void center() {
        int Width = getWidth();
        int Height = getHeight();
        int width = (int)((rectF.right - rectF.left) * scale);
        int height = (int)((rectF.bottom - rectF.top) * scale);
        dX = (Width - width) / 2;
        dY = (Height - height) / 2;

        lastX = -dX;
        lastY = -dY;
    }
}
