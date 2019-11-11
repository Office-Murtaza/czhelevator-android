package com.kingyon.elevator.uis.activities.plan;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.PlanPointGroup;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.PlanPointChildrenAdapter;
import com.kingyon.elevator.uis.widgets.FullyLinearLayoutManager;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.kingyon.elevator.utils.FormatUtils;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class OrderCellDetailsActivity extends BaseStateRefreshingLoadingActivity<PlanPointGroup> {
    private CellItemEntity cell;
    private String type;
    private long startTime, endTime;

    @Override
    protected String getTitleText() {
        cell = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        type = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        startTime = getIntent().getLongExtra(CommonUtil.KEY_VALUE_3, 0);
        endTime = getIntent().getLongExtra(CommonUtil.KEY_VALUE_4, 0);
        return "投放梯数";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_cell_details;
    }

    @Override
    protected MultiItemTypeAdapter<PlanPointGroup> getAdapter() {
        return new BaseAdapter<PlanPointGroup>(this, R.layout.activity_order_cell_details_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, PlanPointGroup item, int position) {
                holder.setTextNotHide(R.id.tv_name, String.format("%s%s", item.getCellName(), item.getBuildName()));
                holder.setSelected(R.id.img_expand, item.isExpand());
                holder.setTextNotHide(R.id.tv_expand, item.isExpand() ? "折叠" : "展开");
                RecyclerView rvPoints = holder.getView(R.id.rv_points);
                PlanPointChildrenAdapter childrenAdapter = (PlanPointChildrenAdapter) rvPoints.getAdapter();
                if (childrenAdapter == null) {
                    childrenAdapter = new PlanPointChildrenAdapter(mContext, R.layout.activity_order_cell_details_point);
                    DealScrollRecyclerView.getInstance().dealAdapter(childrenAdapter, rvPoints, new FullyLinearLayoutManager(mContext));
                }
                childrenAdapter.refreshDatas(item.getPoints());
                holder.setVisible(R.id.rv_points, item.isExpand());
                holder.setVisible(R.id.v_line, item.isExpand());
                OnClickWithObjects onClickWithObjects = new OnClickWithObjects(item) {
                    @Override
                    public void onClick(View view, Object[] objects) {
                        PlanPointGroup group = (PlanPointGroup) objects[0];
                        switch (view.getId()) {
                            case R.id.ll_expand:
                                onGroupExpandClick(group);
                                break;
                        }
                    }
                };
                holder.setOnClickListener(R.id.ll_expand, onClickWithObjects);
            }
        };
    }

    private void onGroupExpandClick(PlanPointGroup group) {
        group.setExpand(!group.isExpand());
        mAdapter.notifyItemChanged(mItems.indexOf(group));
    }

    @Override
    protected void loadData(final int page) {
        mItems.clear();
        if (cell.getPoints() != null) {
            mItems.addAll(FormatUtils.getInstance().getPlanPointGroup(cell.getPoints(), null));
            loadingComplete(true, 1);
        } else {
            NetService.getInstance().planCellsPoinList(cell.getObjctId(), type, startTime, endTime)
                    .compose(this.<List<PointItemEntity>>bindLifeCycle())
                    .subscribe(new CustomApiCallback<List<PointItemEntity>>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            loadingComplete(false, 1);
                        }

                        @Override
                        public void onNext(List<PointItemEntity> pointItemEntities) {
                            List<PlanPointGroup> planPointGroups = FormatUtils.getInstance().getPlanPointGroup(pointItemEntities, null);
                            if (FIRST_PAGE == page) {
                                mItems.clear();
                            }
                            mItems.addAll(planPointGroups);
                            loadingComplete(true, 1);
                        }
                    });
        }
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.background)
                .size(ScreenUtil.dp2px(10))
                .build());
    }
}
