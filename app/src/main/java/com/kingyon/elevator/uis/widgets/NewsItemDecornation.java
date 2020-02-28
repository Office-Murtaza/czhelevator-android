package com.kingyon.elevator.uis.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.utils.DensityUtil;

public class NewsItemDecornation extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, DensityUtil.dip2px(15), 0);
    }
}
