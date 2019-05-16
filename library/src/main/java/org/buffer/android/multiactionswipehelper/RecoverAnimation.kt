package org.buffer.android.multiactionswipehelper

import android.animation.Animator
import android.animation.ValueAnimator
import androidx.recyclerview.widget.RecyclerView

open class RecoverAnimation(
    val mViewHolder: RecyclerView.ViewHolder,
    val mAnimationType: Int,
    val mActionState: Int,
    val mStartDx: Float,
    val mStartDy: Float,
    val mTargetX: Float,
    val mTargetY: Float
) : Animator.AnimatorListener {
    
    private val mValueAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
    var mIsPendingCleanup: Boolean = false
    var hitX: Float = 0.toFloat()
    val hitY: Float get() = mViewHolder.itemView.y + mY
    var mY: Float = 0.toFloat()
    var mEnded = false
    
    // if user starts touching a recovering view, we put it into interaction mode again,
    // instantly.
    var mOverridden = false
    
    private var mFraction: Float = 0.toFloat()
    
    init {
        mValueAnimator.addUpdateListener { animation -> setFraction(animation.animatedFraction) }
        mValueAnimator.setTarget(mViewHolder.itemView)
        mValueAnimator.addListener(this)
        setFraction(0f)
    }
    
    fun setDuration(duration: Long) {
        mValueAnimator.duration = duration
    }
    
    fun start() {
        mViewHolder.setIsRecyclable(false)
        mValueAnimator.start()
    }
    
    fun cancel() {
        mValueAnimator.cancel()
    }
    
    fun setFraction(fraction: Float) {
        mFraction = fraction
    }
    
    /**
     * We run updates on onDraw method but use the fraction from animator callback.
     * This way, we can sync translate x/y values w/ the animators to avoid one-off frames.
     */
    fun update() {
        /*if (mStartDx == mTargetX) {
            //mX = ViewCompat.getTranslationX(mViewHolder.itemView);
        } else */run { hitX = mStartDx + mFraction * (mTargetX - mStartDx) }
        mY = if (mStartDy == mTargetY) {
            mViewHolder.itemView.translationY
        } else {
            mStartDy + mFraction * (mTargetY - mStartDy)
        }
    }
    
    override fun onAnimationStart(animation: Animator) {}
    
    override fun onAnimationEnd(animation: Animator) {
        if (!mEnded)
            mViewHolder.setIsRecyclable(true)
        mEnded = true
    }
    
    override fun onAnimationCancel(animation: Animator) {
        setFraction(1f) //make sure we recover the view's state.
    }
    
    override fun onAnimationRepeat(animation: Animator) {}
    
}
