package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.leo.afbaselibrary.utils.ScreenUtil;

/**
 * Created by GongLi on 2018/12/26.
 * Email：lc824767150@163.com
 */

public class AdScreenProportionFrame extends FrameLayout {

    //    private float adScreenProporty = 0.85492f;
    private float adScreenProporty = Constants.adScreenProperty;

    public AdScreenProportionFrame(@NonNull Context context) {
        super(context);
    }

    public AdScreenProportionFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initProporty(context, attrs);
    }

    public AdScreenProportionFrame(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProporty(context, attrs);
    }

    private void initProporty(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.AdScreenProportionFrame);
        adScreenProporty = mTypedArray.getFloat(R.styleable.AdScreenProportionFrame_adScreenProporty, adScreenProporty);
        mTypedArray.recycle();
    }

    public void setProporty(float ratio) {
        if (ratio > 0) {
            this.adScreenProporty = ratio;
        }
        postInvalidate();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null && layoutParams.width > 0) {
            layoutParams.height = (int) (layoutParams.width / adScreenProporty);
            setLayoutParams(layoutParams);
        }
    }

    public float getProporty() {
        return adScreenProporty;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
            int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
            float criticalProperty = maxWidth / (float) maxHeight;
            if (adScreenProporty > criticalProperty) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) (maxWidth / adScreenProporty + 0.5), MeasureSpec.EXACTLY));
            } else {
                super.onMeasure(MeasureSpec.makeMeasureSpec((int) (maxHeight * adScreenProporty + 0.5f), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY));
            }
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) (width / adScreenProporty + 0.5f), MeasureSpec.EXACTLY));
        }
    }
}
