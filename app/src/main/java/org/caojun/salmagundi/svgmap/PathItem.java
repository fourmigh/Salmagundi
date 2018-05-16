package org.caojun.salmagundi.svgmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * @author: 蜗牛
 * @date: 2017/6/1 10:13
 * @desc:
 */

public class PathItem {
    private String title;
    private Path mPath;
    private boolean isSelect;

    public PathItem(String title, Path path) {
        this.title = title;
        mPath = path;
    }

    public String getMapName() {
        String name = title;
        String AND = " and ";
        String SPACE = " ";
        if (name.contains(AND)) {
            name = title.substring(0, title.indexOf(AND)) + "_" + title.substring(title.indexOf(AND) + AND.length());
        } else if (name.contains(SPACE)) {
            name = title.replace(SPACE, "_");
        }
        return name.toLowerCase();
    }

    public String getTitle() {
        return title;
    }

    /**
     * 是否touch在该path内部
     * @param x
     * @param y
     * @return
     */
    public boolean isTouch(int x, int y) {
        Region result = new Region();
        //构造一个区域对象。
        RectF r=new RectF();
        //计算path的边界
        mPath.computeBounds(r, true);
        //设置区域路径和剪辑描述的区域
        result.setPath(mPath, new Region((int)r.left,(int)r.top,(int)r.right,(int)r.bottom));
        return result.contains(x, y);
    }

    public void draw(Canvas canvas, Paint paint) {
        if (isSelect) {
            //首先绘制选中的背景阴影
            paint.clearShadowLayer();
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8, 0, 0, Color.BLACK);
            canvas.drawPath(mPath,paint);
            //绘制具体显示的
            paint.clearShadowLayer();
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(mPath,paint);
        } else {
            //绘制具体显示的
            paint.clearShadowLayer();
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(mPath,paint);
        }
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public RectF getRectF() {
        RectF r = new RectF();
        //计算path的边界
        mPath.computeBounds(r, true);
        return r;
    }
}
