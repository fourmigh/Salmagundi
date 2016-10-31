package org.caojun.salmagundi.color;

/**
 * 颜色
 * Created by  CaoJun 2016/10/28.
 */

public class Color {

    private int alpha, red, green, blue;

    public Color(int alpha, int red, int green, int blue)
    {
        this.setAlpha(alpha);
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
        {
            return false;
        }
        if(!(o instanceof Color))
        {
            return false;
        }
        Color color = (Color) o;
        if(color.getAlpha() != this.getAlpha())
        {
            return false;
        }
        if(color.getRed() != this.getRed())
        {
            return false;
        }
        if(color.getGreen() != this.getGreen())
        {
            return false;
        }
        if(color.getBlue() != this.getBlue())
        {
            return false;
        }
        return true;
    }

    public Color(int red, int green, int blue)
    {
        this(0xFF, red, green, blue);
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
