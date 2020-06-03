package com.kingyon.elevator.uis.activities.cooperation;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.PlanPointGroup;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.adapterone.CooperationCellDevicesAdapter;
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
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class CooperationCellDevicesActivity extends BaseStateRefreshingLoadingActivity<PlanPointGroup> {

    protected long cellId;
    protected String role;

    @Override
    protected String getTitleText() {
        cellId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        role = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        return "设备管理";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cooperation_cell_devices;
    }

    @Override
    protected MultiItemTypeAdapter<PlanPointGroup> getAdapter() {
        return new BaseAdapter<PlanPointGroup>(this, R.layout.activity_cooperation_cell_devices_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, PlanPointGroup item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getBuildName());
                holder.setSelected(R.id.img_expand, item.isExpand());
                holder.setTextNotHide(R.id.tv_expand, item.isExpand() ? "折叠" : "展开");
                RecyclerView rvPoints = holder.getView(R.id.rv_points);
                CooperationCellDevicesAdapter childrenAdapter = (CooperationCellDevicesAdapter) rvPoints.getAdapter();
                if (childrenAdapter == null) {
                    childrenAdapter = new CooperationCellDevicesAdapter(mContext, R.layout.activity_assign_child);
                    childrenAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<PointItemEntity>() {
                        @Override
                        public void onItemClick(View view, int position, PointItemEntity entity, BaseAdapterWithHF<PointItemEntity> baseAdaper) {
                            onDeviceClick(entity);
                        }
                    });
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

                holder.setOnClickListener(R.id.tv_name, onClickWithObjects);
                holder.setOnClickListener(R.id.ll_expand, onClickWithObjects);
            }
        };
    }

    protected void onGroupExpandClick(PlanPointGroup group) {
        group.setExpand(!group.isExpand());
        mAdapter.notifyItemChanged(mItems.indexOf(group));
    }

    protected void onDeviceClick(PointItemEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonUtil.KEY_VALUE_1, entity.getObjectId());
        bundle.putString(CommonUtil.KEY_VALUE_2, role);
        startActivity(CooperationDevicesDetailsActivity.class, bundle);
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().cellDeviceList(cellId)
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

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.background)
                .size(ScreenUtil.dp2px(10))
                .build());
    }
}
