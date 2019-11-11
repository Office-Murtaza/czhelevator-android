package com.kingyon.elevator.uis.activities.plan;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class OrderCellsActivity extends BaseStateRefreshingLoadingActivity<CellItemEntity> {

    private List<CellItemEntity> cells;
    private String type;
    private long startTime, endTime;

    @Override
    protected String getTitleText() {
        cells = getIntent().getParcelableArrayListExtra(CommonUtil.KEY_VALUE_1);
        type = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        startTime = getIntent().getLongExtra(CommonUtil.KEY_VALUE_3, 0);
        endTime = getIntent().getLongExtra(CommonUtil.KEY_VALUE_4, 0);
        return "投放梯数";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_cells;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mLayoutRefresh.setEnabled(false);
    }

    @Override
    protected MultiItemTypeAdapter<CellItemEntity> getAdapter() {
        return new BaseAdapter<CellItemEntity>(this, R.layout.activity_order_cells_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, CellItemEntity item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getCellName());
                holder.setTextNotHide(R.id.tv_address, item.getAddress());
                holder.setTextNotHide(R.id.tv_number, String.format("投放数量:%s", item.getChoosedScreenNum()));
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, CellItemEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonUtil.KEY_VALUE_1, item);
            bundle.putString(CommonUtil.KEY_VALUE_2, type);
            bundle.putLong(CommonUtil.KEY_VALUE_3, startTime);
            bundle.putLong(CommonUtil.KEY_VALUE_4, endTime);
            startActivity(OrderCellDetailsActivity.class, bundle);
        }
    }

    @Override
    protected void loadData(int page) {
        mItems.clear();
        if (cells != null) {
            mItems.addAll(cells);
        }
        loadingComplete(true, 1);
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .drawable(R.drawable.white_margin_sixteen_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build());
    }
}
