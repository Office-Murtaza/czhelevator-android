package com.kingyon.elevator.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kingyon.elevator.R;


@SuppressLint("AppCompatCustomView")
public class SelfAdaptionImageView extends ImageView {
    private int WidthRatio, HeightRatio;

    public SelfAdaptionImageView(Context context) {
        this(context, null);
    }

    public SelfAdaptionImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SelfAdaptionImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取到设置的宽高比，然后设置自己的值
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SelfAdaptionImageView);
        WidthRatio = array.getInt(R.styleable.SelfAdaptionImageView_WidthRatio, 0);
        HeightRatio = array.getInt(R.styleable.SelfAdaptionImageView_HeightRatio, 0);
        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //在这里重写自己的宽高.设置的宽高比大于0才执行,我这里的需求一般是填充父view的宽度，高度要做修改
        if (WidthRatio > 0 && HeightRatio > 0) {
            int width = getMeasuredWidth();
            int height = (width * HeightRatio) / WidthRatio;
            setMeasuredDimension(width, height);
            invalidate();
        }
    }
}
