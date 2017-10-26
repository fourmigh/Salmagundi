package org.caojun.library.carousel.view

import android.content.Context
import android.view.animation.AnimationUtils

/**
 * Created by CaoJun on 2017/10/25.
 */
class Rotator {

    private var mMode = 0
    private var mStartAngle = 0f
    private var mCurrAngle = 0f

    private var mStartTime = 0.toLong()
    private var mDuration = 0.toLong()

    private var mDeltaAngle = 0f

    private var mFinished = false

    private val CoeffVelocity = 0.05f
    private var mVelocity = 0f

    private val DEFAULT_DURATION = 2000
    private val SCROLL_MODE = 0
    private val FLING_MODE = 1

    private val mDeceleration = 120f

    /**
     * Create a Scroller with the specified interpolator. If the interpolator is
     * null, the default (viscous) interpolator will be used.
     */
    constructor(context: Context) {
        mFinished = true
    }

    /**
     *
     * Returns whether the scroller has finished scrolling.
     *
     * @return True if the scroller has finished scrolling, false otherwise.
     */
    fun isFinished(): Boolean {
        return mFinished
    }

    /**
     * Force the finished field to a particular value.
     *
     * @param finished
     * The new finished value.
     */
    fun forceFinished(finished: Boolean) {
        mFinished = finished
    }

    /**
     * Returns how long the scroll event will take, in milliseconds.
     *
     * @return The duration of the scroll in milliseconds.
     */
    fun getDuration(): Long {
        return mDuration
    }

    /**
     * Returns the current X offset in the scroll.
     *
     * @return The new X offset as an absolute distance from the origin.
     */
    fun getCurrAngle(): Float {
        return mCurrAngle
    }

    /**
     * @hide Returns the current velocity.
     *
     * @return The original velocity less the deceleration. Result may be
     * negative.
     */
    fun getCurrVelocity(): Float {
        return CoeffVelocity * mVelocity - mDeceleration * timePassed()
    }

    /**
     * Returns the start X offset in the scroll.
     *
     * @return The start X offset as an absolute distance from the origin.
     */
    fun getStartAngle(): Float = mStartAngle

    /**
     * Returns the time elapsed since the beginning of the scrolling.
     *
     * @return The elapsed time in milliseconds.
     */
    fun timePassed(): Int = (AnimationUtils.currentAnimationTimeMillis() - mStartTime).toInt()

    /**
     * Extend the scroll animation. This allows a running animation to scroll
     * further and longer, when used with [.] or
     * [.].
     *
     * @param extend
     * Additional time to scroll in milliseconds.
     */
    fun extendDuration(extend: Int) {
        val passed = timePassed()
        mDuration = (passed + extend).toLong()
        mFinished = false
    }

    /**
     * Stops the animation. Contrary to [.forceFinished],
     * aborting the animating cause the scroller to move to the final x and y
     * position
     *
     * @see .forceFinished
     */
    fun abortAnimation() {
        mFinished = true
    }

    /**
     * Call this when you want to know the new location. If it returns true, the
     * animation is not yet finished. loc will be altered to provide the new
     * location.
     */
    fun computeAngleOffset(): Boolean {
        if (mFinished) {
            return false
        }

        val systemClock = AnimationUtils.currentAnimationTimeMillis()
        val timePassed = systemClock - mStartTime

        if (timePassed < mDuration) {
            when (mMode) {
                SCROLL_MODE -> {

                    val sc = timePassed.toFloat() / mDuration
                    mCurrAngle = mStartAngle + Math.round(mDeltaAngle * sc)
                }

                FLING_MODE -> {

                    val timePassedSeconds = timePassed / 1000.0f
                    val distance: Float

                    if (mVelocity < 0) {
                        distance = CoeffVelocity * mVelocity * timePassedSeconds - mDeceleration * timePassedSeconds * timePassedSeconds / 2.0f
                    } else {
                        distance = -CoeffVelocity * mVelocity * timePassedSeconds - mDeceleration * timePassedSeconds * timePassedSeconds / 2.0f
                    }

                    mCurrAngle = mStartAngle - Math.signum(mVelocity) * Math.round(distance)
                }
            }//mCurrAngle = mCurrAngle == 0.0f ? (mStartAngle +mDeltaAngle * sc) : mCurrAngle;
            // System.out.println("The mCurrAngle is:"+mCurrAngle);
            return true
        } else {
            mFinished = true
            return false
        }
    }

    /**
     * Start scrolling by providing a starting point and the distance to travel.
     *
     * Starting horizontal scroll offset in pixels. Positive numbers
     * will scroll the content to the left.
     * Starting vertical scroll offset in pixels. Positive numbers
     * will scroll the content up.
     * Horizontal distance to travel. Positive numbers will scroll
     * the content to the left.
     * Vertical distance to travel. Positive numbers will scroll the
     * content up.
     * @param duration
     * Duration of the scroll in milliseconds.
     */
    fun startRotate(startAngle: Float, dAngle: Float, duration: Int) {
        mMode = SCROLL_MODE
        mFinished = false
        mDuration = duration.toLong()
        mStartTime = AnimationUtils.currentAnimationTimeMillis()
        mStartAngle = startAngle
        mDeltaAngle = dAngle
    }

    /**
     * Start scrolling by providing a starting point and the distance to travel.
     * The scroll will use the default value of 250 milliseconds for the
     * duration.
     *
     * Starting horizontal scroll offset in pixels. Positive numbers
     * will scroll the content to the left.
     * Starting vertical scroll offset in pixels. Positive numbers
     * will scroll the content up.
     * Horizontal distance to travel. Positive numbers will scroll
     * the content to the left.
     * Vertical distance to travel. Positive numbers will scroll the
     * content up.
     */
    fun startRotate(startAngle: Float, dAngle: Float) {
        startRotate(startAngle, dAngle, DEFAULT_DURATION)
    }

    /**
     * Start scrolling based on a fling gesture. The distance travelled will
     * depend on the initial velocity of the fling.
     *
     * @param velocityAngle
     * Initial velocity of the fling (X) measured in pixels per
     * second.
     */
    fun fling(velocityAngle: Float) {

        mMode = FLING_MODE
        mFinished = false

        mVelocity = velocityAngle
        mDuration = (1000.0f * Math.sqrt((2.0f * CoeffVelocity * Math.abs(velocityAngle) / mDeceleration).toDouble())).toInt().toLong()

        mStartTime = AnimationUtils.currentAnimationTimeMillis()

    }
}