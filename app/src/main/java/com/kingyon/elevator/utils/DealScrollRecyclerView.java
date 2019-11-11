package com.kingyon.elevator.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class DealScrollRecyclerView {
    private static DealScrollRecyclerView dealScrollRecyclerView;

    private DealScrollRecyclerView() {

    }

    public static DealScrollRecyclerView getInstance() {
        if (dealScrollRecyclerView == null) {
            dealScrollRecyclerView = new DealScrollRecyclerView();
        }
        return dealScrollRecyclerView;
    }

    public void dealAdapter(RecyclerView.Adapter adapter, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        if (adapter != null && recyclerView != null && layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layoutManager).setSmoothScrollbarEnabled(true);
            }
            layoutManager.setAutoMeasureEnabled(true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
}
