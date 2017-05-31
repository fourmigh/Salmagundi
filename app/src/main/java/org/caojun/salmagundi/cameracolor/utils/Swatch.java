package org.caojun.salmagundi.cameracolor.utils;

import android.graphics.Color;

import java.util.Arrays;

/**
 * Created by CaoJun on 2017/5/27.
 */

public class Swatch {
    private static final float MIN_CONTRAST_TITLE_TEXT = 3.0f;
    private static final float MIN_CONTRAST_BODY_TEXT = 4.5f;
    private final int mRed, mGreen, mBlue;
    private final int mRgb;
    private final int mPopulation;

    private boolean mGeneratedTextColors;
    private int mTitleTextColor;
    private int mBodyTextColor;

    private float[] mHsl;

    Swatch(int rgbColor, int population) {
        mRed = Color.red(rgbColor);
        mGreen = Color.green(rgbColor);
        mBlue = Color.blue(rgbColor);
        mRgb = rgbColor;
        mPopulation = population;
    }

    Swatch(int red, int green, int blue, int population) {
        mRed = red;
        mGreen = green;
        mBlue = blue;
        mRgb = Color.rgb(red, green, blue);
        mPopulation = population;
    }

    /**
     * @return this swatch's RGB color value
     */
    public int getRgb() {
        return mRgb;
    }

    /**
     * Return this swatch's HSL values.
     *     hsv[0] is Hue [0 .. 360)
     *     hsv[1] is Saturation [0...1]
     *     hsv[2] is Lightness [0...1]
     */
    public float[] getHsl() {
        if (mHsl == null) {
            // Lazily generate HSL values from RGB
            mHsl = new float[3];
            ColorUtils.RGBtoHSL(mRed, mGreen, mBlue, mHsl);
        }
        return mHsl;
    }
    /**
     * @return the number of pixels represented by this swatch
     */
    public int getPopulation() {
        return mPopulation;
    }
    /**
     * Returns an appropriate color to use for any 'title' text which is displayed over this
     * {@link Swatch}'s color. This color is guaranteed to have sufficient contrast.
     */
    public int getTitleTextColor() {
        ensureTextColorsGenerated();
        return mTitleTextColor;
    }

    /**
     * Returns an appropriate color to use for any 'body' text which is displayed over this
     * {@link Swatch}'s color. This color is guaranteed to have sufficient contrast.
     */
    public int getBodyTextColor() {
        ensureTextColorsGenerated();
        return mBodyTextColor;
    }

    private void ensureTextColorsGenerated() {
        if (!mGeneratedTextColors) {
            mTitleTextColor = ColorUtils.getTextColorForBackground(mRgb,
                    MIN_CONTRAST_TITLE_TEXT);
            mBodyTextColor = ColorUtils.getTextColorForBackground(mRgb,
                    MIN_CONTRAST_BODY_TEXT);
            mGeneratedTextColors = true;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName())
                .append(" [RGB: #").append(Integer.toHexString(getRgb())).append(']')
                .append(" [HSL: ").append(Arrays.toString(getHsl())).append(']')
                .append(" [Population: ").append(mPopulation).append(']')
                .append(" [Title Text: #").append(Integer.toHexString(mTitleTextColor)).append(']')
                .append(" [Body Text: #").append(Integer.toHexString(mBodyTextColor)).append(']')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Swatch swatch = (Swatch) o;
        return mPopulation == swatch.mPopulation && mRgb == swatch.mRgb;
    }

    @Override
    public int hashCode() {
        return 31 * mRgb + mPopulation;
    }
}
