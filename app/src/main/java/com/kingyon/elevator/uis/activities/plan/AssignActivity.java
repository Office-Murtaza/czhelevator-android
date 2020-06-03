package com.kingyon.elevator.uis.activities.plan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.PlanPointGroup;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.adapterone.PlanPointChildrenAdapter;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/2.
 * Email：lc824767150@163.com
 */

@Deprecated
public class AssignActivity extends BaseStateRefreshingLoadingActivity<PlanPointGroup> {
    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;

    private long cellId;
    private String type;
    private long startTime;
    private long endTime;
    private long planId;
    private List<PointItemEntity> choosedPoints;
    private boolean canCacheChoose;

    @Override
    protected String getTitleText() {
        cellId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        type = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        startTime = getIntent().getLongExtra(CommonUtil.KEY_VALUE_3, 0);
        endTime = getIntent().getLongExtra(CommonUtil.KEY_VALUE_4, 0);
        planId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_5, 0);
        choosedPoints = getIntent().getParcelableArrayListExtra(CommonUtil.KEY_VALUE_6);
        return "单独指定电梯";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_assign;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        preVRight.setVisibility(View.GONE);
    }

    @Override
    protected MultiItemTypeAdapter<PlanPointGroup> getAdapter() {
        return new BaseAdapter<PlanPointGroup>(this, R.layout.activity_assign_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, PlanPointGroup item, int position) {
                holder.setTextNotHide(R.id.tv_name, String.format("%s%s", item.getCellName(), item.getBuildName()));
                holder.setSelected(R.id.tv_name, item.isChoosed());
                holder.setSelected(R.id.img_expand, item.isExpand());
//                holder.setTextNotHide(R.id.tv_expand, item.isExpand() ? "折叠" : "展开");
                RecyclerView rvPoints = holder.getView(R.id.rv_points);
                PlanPointChildrenAdapter childrenAdapter = (PlanPointChildrenAdapter) rvPoints.getAdapter();
                if (childrenAdapter == null) {
                    childrenAdapter = new PlanPointChildrenAdapter(mContext, R.layout.activity_assign_child);
                    childrenAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<PointItemEntity>() {
                        @Override
                        public void onItemClick(View view, int position, PointItemEntity entity, BaseAdapterWithHF<PointItemEntity> baseAdaper) {
                            onPointClick(entity);
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
                            case R.id.tv_name:
                                onGroupChooseClick(group);
                                break;
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

    private void onGroupChooseClick(PlanPointGroup group) {
        group.setChoosed(!group.isChoosed());
        List<PointItemEntity> points = group.getPoints();
        for (PointItemEntity point : points) {
            point.setChoosed(group.isChoosed());
        }
        updateGroupChoose();
    }

    private void onGroupExpandClick(PlanPointGroup group) {
        group.setExpand(!group.isExpand());
        mAdapter.notifyItemChanged(mItems.indexOf(group));
    }

    private void onPointClick(PointItemEntity entity) {
        entity.setChoosed(!entity.isChoosed());
        updateGroupChoose();
    }

    private void updateGroupChoose() {
        boolean allChoosed = true;
        for (PlanPointGroup group : mItems) {
            boolean choosed = true;
            List<PointItemEntity> points = group.getPoints();
            for (PointItemEntity point : points) {
                if (!point.isChoosed()) {
                    choosed = false;
                    break;
                }
            }
            group.setChoosed(choosed);
            if (!group.isChoosed()) {
                allChoosed = false;
            }
        }
        mAdapter.notifyDataSetChanged();
        preVRight.setVisibility(View.VISIBLE);
        preVRight.setSelected(allChoosed);
        preVRight.setText(allChoosed ? "取消" : "全选");
    }

    private ArrayList<PointItemEntity> getChoosedParams() {
        ArrayList<PointItemEntity> results = new ArrayList<>();
        for (PlanPointGroup group : mItems) {
            List<PointItemEntity> points = group.getPoints();
            for (PointItemEntity point : points) {
                if (point.isChoosed()) {
                    results.add(point);
                }
            }
        }
        return results;
    }

    @Override
    protected void loadData(final int page) {
        if (FIRST_PAGE == page && canCacheChoose) {
            choosedPoints = getChoosedParams();
        }
        NetService.getInstance().planCellsPoinList(cellId, type, startTime, endTime)
                .compose(this.<List<PointItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<PointItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 1);
                    }

                    @Override
                    public void onNext(List<PointItemEntity> pointItemEntities) {
                        LogUtils.e(pointItemEntities.toString());
                        List<PlanPointGroup> planPointGroups = FormatUtils.getInstance().getPlanPointGroup(pointItemEntities, choosedPoints);
                        canCacheChoose = true;
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(planPointGroups);
                        loadingComplete(true, 1);
                        updateGroupChoose();
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

    @OnClick({R.id.pre_v_right, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_right:
                preVRight.setSelected(!preVRight.isSelected());
                preVRight.setText(preVRight.isSelected() ? "取消" : "全选");
                for (PlanPointGroup group : mItems) {
                    group.setChoosed(preVRight.isSelected());
                    List<PointItemEntity> points = group.getPoints();
                    for (PointItemEntity point : points) {
                        point.setChoosed(group.isChoosed());
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_ensure:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putLong(CommonUtil.KEY_VALUE_1, cellId);
                bundle.putString(CommonUtil.KEY_VALUE_2, type);
                bundle.putLong(CommonUtil.KEY_VALUE_3, startTime);
                bundle.putLong(CommonUtil.KEY_VALUE_4, endTime);
                bundle.putLong(CommonUtil.KEY_VALUE_5, planId);
                bundle.putParcelableArrayList(CommonUtil.KEY_VALUE_6, getChoosedParams());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
