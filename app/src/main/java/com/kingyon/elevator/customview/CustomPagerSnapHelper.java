package com.kingyon.elevator.customview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created By SongPeng  on 2019/12/25
 * Email : 1531603384@qq.com
 */
public class CustomPagerSnapHelper extends PagerSnapHelper {

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        return super.calculateDistanceToFinalSnap(layoutManager, targetView);
    }
}
