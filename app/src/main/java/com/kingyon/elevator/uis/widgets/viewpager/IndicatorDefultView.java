package com.kingyon.elevator.uis.widgets.viewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.Gravity;

/**
 * Created by gongli on 2017/5/18 14:49
 * email: lc824767150@163.com
 */
public class IndicatorDefultView extends AppCompatCheckedTextView {
    private boolean isHasSelector = false;
    private Paint paint;
    private float width;
    private float height;
    private float radius;

    public void setHasSelector(boolean hasSelector) {
        isHasSelector = hasSelector;
    }

    public IndicatorDefultView(Context context) {
        super(context);
        this.setGravity(Gravity.CENTER);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff888888);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isHasSelector)
            return;
        if (width == 0) {
            width = getWidth();
            height = getHeight();
            radius = (width > height ? height : width - 4) / 2;
        }
        if (isChecked()) {
            paint.setColor(0xff26BAFF);
            canvas.drawCircle(width / 2, height / 2, radius, paint);
        } else {
            paint.setColor(0xffbdeaff);
            canvas.drawCircle(width / 2, height / 2, radius, paint);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (isChecked() != checked) {
            invalidate();
        }
        super.setChecked(checked);
    }

}
