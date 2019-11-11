package com.kingyon.elevator.uis.activities.order;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellGroupEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.PointMonitChildrenAdapter;
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

import java.util.List;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class OrderMonitActivity extends BaseStateRefreshingLoadingActivity<CellGroupEntity> {

    private long orderId;
    private boolean monitImage;

    @Override
    protected String getTitleText() {
        orderId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        monitImage = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_2, true);
        return monitImage ? "查看监播表" : "查看视频监控";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_monit;
    }

    @Override
    protected MultiItemTypeAdapter<CellGroupEntity> getAdapter() {
        return new BaseAdapter<CellGroupEntity>(this, R.layout.activity_order_monit_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, CellGroupEntity item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getCellName());
                holder.setSelected(R.id.img_expand, item.isExpand());
                holder.setTextNotHide(R.id.tv_expand, item.isExpand() ? "折叠" : "展开");
                RecyclerView rvPoints = holder.getView(R.id.rv_points);
                PointMonitChildrenAdapter childrenAdapter = (PointMonitChildrenAdapter) rvPoints.getAdapter();
                if (childrenAdapter == null) {
                    childrenAdapter = new PointMonitChildrenAdapter(mContext, monitImage);
                    childrenAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<PointItemEntity>() {
                        @Override
                        public void onItemClick(View view, int position, PointItemEntity entity, BaseAdapterWithHF<PointItemEntity> baseAdaper) {
                            onMonitClick(entity);
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
                        CellGroupEntity group = (CellGroupEntity) objects[0];
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

    private void onMonitClick(PointItemEntity entity) {
        Bundle bundle = new Bundle();
        if (monitImage) {
            bundle.putLong(CommonUtil.KEY_VALUE_1, orderId);
            bundle.putLong(CommonUtil.KEY_VALUE_2, entity.getObjectId());
            startActivity(MonitImagesActivity.class, bundle);
        } else {
//            entity.setLiveAddress("rtmp://219.151.1.83/live/12345678901234567890");
            bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
            startActivity(DeviceLiveActivity.class, bundle);
        }
    }

    private void onGroupExpandClick(CellGroupEntity group) {
        group.setExpand(!group.isExpand());
        mAdapter.notifyItemChanged(mItems.indexOf(group));
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().orderPoints(orderId)
                .compose(this.<List<PointItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<PointItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 1);
                    }

                    @Override
                    public void onNext(List<PointItemEntity> pointItemEntities) {
                        List<CellGroupEntity> cellGroups = FormatUtils.getInstance().getCellGroups(pointItemEntities, null);
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(cellGroups);
                        loadingComplete(true, 1);
                    }
                });
    }
}
