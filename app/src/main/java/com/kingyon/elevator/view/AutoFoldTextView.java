package com.kingyon.elevator.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @Created By Admin  on 2020/8/12
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 带展开收起
 */
public class AutoFoldTextView extends android.support.v7.widget.AppCompatTextView {

    private final static String TAG = "AutoFoldTextView";

    public interface OnFoldListener {
        void onFold(boolean isFolded);
    }

    // 折叠状态显示行数
    private int mDefaultLines = 2;
    // 状态(是否是折叠状态)
    private boolean mIsFolded = true;
    // 外部状态监听接口
    private OnFoldListener mOnFoldListener;
    // 折叠时的高度
    private int mFoldHeight;
    // 展开时的高度
    private int mUnFoldHeight;
    // 高度变化的属性动画
    private ObjectAnimator mHeightObjectAnimator;
    // 动画时间
    private int mFoldDuration = 300;

    public AutoFoldTextView(Context context) {
        this(context,null);
    }

    public AutoFoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoFoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mUnFoldHeight == 0) {
            mUnFoldHeight = getLayout().getLineTop(getLineCount()) + getCompoundPaddingTop() + getCompoundPaddingBottom();
            setMaxLines(mDefaultLines);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mFoldHeight = getMeasuredHeight();
            Log.d(TAG, "onMeasure() called with: mUnFoldHeight = [" + mUnFoldHeight + "], " +
                    "mFoldHeight = [" + mFoldHeight + "]");
            if (mUnFoldHeight == mFoldHeight) {
                mIsFolded = false;
            }else{
                setMaxLines(Integer.MAX_VALUE);
                setMaxHeight(mFoldHeight);
            }
        }
    }

    public void setOnFlodListener(OnFoldListener mOnFoldListener) {
        this.mOnFoldListener = mOnFoldListener;
    }

    public void setFoldView(View mFoldView) {
        Log.d(TAG, "setFlodView() called with: mFlodView = [" + mFoldView + "]");
        // 折叠/展开 按钮
        if(!mIsFolded){
            mFoldView.setVisibility(GONE);
        }else {
            mFoldView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    fold(!mIsFolded);
                }
            });
        }
    }

    /**
     * 折叠/展开
     * @param isFolded true为折叠，false为展开
     */
    private void fold(boolean isFolded) {
        mIsFolded = isFolded;
        if(mHeightObjectAnimator != null && mHeightObjectAnimator.isRunning()){
            mHeightObjectAnimator.cancel();
        }
        if(mIsFolded) {
            mHeightObjectAnimator = ObjectAnimator.ofInt(this, "MaxHeight",
                    mFoldHeight);
        }else{
            mHeightObjectAnimator = ObjectAnimator.ofInt(this, "MaxHeight",
                    mUnFoldHeight);
        }
        mHeightObjectAnimator.setDuration(mFoldDuration);
        mHeightObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                notifyListener();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mHeightObjectAnimator.start();
    }

    private void notifyListener() {
        if(mOnFoldListener != null){
            mOnFoldListener.onFold(mIsFolded);
        }
    }

    public boolean getIsFolded() {
        return mIsFolded;
    }

    // for databinding
    public void setIsFolded(boolean mIsFolded) {
        if(mFoldHeight != 0 && mUnFoldHeight != 0) {
            fold(mIsFolded);
        }
    }

    public void setFoldDuration(int mFoldDuration) {
        this.mFoldDuration = mFoldDuration;
    }

    public void setDefaultLines(int mDefaultLines) {
        this.mDefaultLines = mDefaultLines;
        requestLayout();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mHeightObjectAnimator != null && mHeightObjectAnimator.isRunning()){
            mHeightObjectAnimator.cancel();
            mHeightObjectAnimator.removeAllListeners();
            mHeightObjectAnimator = null;
        }
    }
}
