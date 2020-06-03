package com.kingyon.elevator.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @Created By Admin  on 2020/5/25
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
@SuppressLint("AppCompatCustomView")
public class AlwaysMarqueeTextView extends TextView {
    public AlwaysMarqueeTextView(Context context) {
        super(context);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}