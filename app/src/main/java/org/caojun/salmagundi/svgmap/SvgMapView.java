package org.caojun.salmagundi.svgmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SvgMapView extends View {
    private Paint mPaint;

    //手势监听器
    private GestureDetector mDetector;
    //缩放系数
    private float scale=1.3f;
    //保存path对象
    private List<PathItem> pathItems = new ArrayList<>();

    private ScaleGestureDetector mScaleGestureDetector;

    public SvgMapView(Context context) {
        this(context, null);
    }

    public SvgMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private float dX = 0, dY = 0;
    private float lastX = 0, lastY = 0;

    public interface OnClickListener {
        void onClick(String mapName);
    }

    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    private void init() {
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
//                float x = e.getX() / scale;
//                float y = e.getY() / scale;
//                x -= dX;
//                y -= dY;
//                try {
//                    for (PathItem pathItem : pathItems) {
//                        boolean isSelected = pathItem.isTouch((int) x, (int) y);
//                        pathItem.setSelect(isSelected);
//
////                        if (isSelected) {
////                            parserPaths(pathItem.getMapName());
////                        }
//                    }
//                    invalidate();
//                } catch (Exception e1) {
//                }
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                float x = e.getX() / scale;
                float y = e.getY() / scale;
                x -= dX;
                y -= dY;
                String mapName = "";
                try {
                    for (PathItem pathItem : pathItems) {
                        boolean isSelected = pathItem.isTouch((int) x, (int) y);
                        pathItem.setSelect(isSelected);

                        if (isSelected) {
                            mapName = pathItem.getMapName();
                        }
                    }
                    invalidate();
                } catch (Exception e1) {
                }
                if (listener != null && !TextUtils.isEmpty(mapName)) {
                    listener.onClick(mapName);
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float s = Math.abs(scale);
                dX = lastX + distanceX / s;
                dY = lastY + distanceY / s;
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
//        setMap("world");
    }

    /**
     * 解析path
     */
    public void setMap(final String mapName) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建DOM工厂对象
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    // DocumentBuilder对象
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    //打开输入流
                    int resId = getResources().getIdentifier(mapName, "raw", getContext().getPackageName());
                    InputStream is = getResources().openRawResource(resId);
                    // 获取文档对象
                    Document doc = db.parse(is);
                    //获取path元素节点集合
                    NodeList paths = doc.getElementsByTagName("path");
                    PathItem item;
                    pathItems.clear();
                    for (int i = 0; i < paths.getLength(); i++) {
                        // 取出每一个元素
                        Element personNode = (Element) paths.item(i);
                        //得到d属性值
                        String nodeValue = personNode.getAttribute("d");
                        String title = personNode.getAttribute("title");
                        //解析，并创建pathItem
                        item = new PathItem(title, PathParser.createPathFromPathData(nodeValue));
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
                    postInvalidate();
                } catch (Exception e) {
                }
            }
        }).start();
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
        try {
            for (PathItem pathItem : pathItems) {
                pathItem.draw(canvas, mPaint);
            }
        } catch (Exception e) {
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
