package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.PlanItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.PlanEditDialog;
import com.kingyon.elevator.uis.dialogs.TipDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class MyPlanActivity extends BaseStateRefreshingLoadingActivity<PlanItemEntity> implements TipDialog.OnOperatClickListener<PlanItemEntity>, PlanEditDialog.OnResultListener {
    @BindView(R.id.pre_v_right)
    TextView preVRight;

    private PlanEditDialog editDialog;
    private TipDialog<PlanItemEntity> tipDialog;
    private boolean notFirstIn;

    @Override
    protected String getTitleText() {
        return "我的点位计划";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_plan;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setText("添加");

    }

    @Override
    protected MultiItemTypeAdapter<PlanItemEntity> getAdapter() {
        return new BaseAdapter<PlanItemEntity>(this, R.layout.activity_my_plan_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, PlanItemEntity item, int position) {
                holder.setText(R.id.tv_name, item.getPlanName());
                holder.setTextNotHide(R.id.tv_time, String.format("%s创建", TimeUtil.getYmdTimeItalic(item.getCreateTime())));
                OnClickWithObjects onClickWithObjects = new OnClickWithObjects(item) {
                    @Override
                    public void onClick(View view, Object[] objects) {
                        switch (view.getId()) {
                            case R.id.tv_delete:
                                onDeleteClick(((PlanItemEntity) objects[0]));
                                break;
                            case R.id.tv_edit:
                                onEditClick(((PlanItemEntity) objects[0]));
                                break;
                            case R.id.ll_content:
                                onContentClick(((PlanItemEntity) objects[0]));
                                break;
                        }
                    }
                };
                holder.setOnClickListener(R.id.tv_delete, onClickWithObjects);
                holder.setOnClickListener(R.id.tv_edit, onClickWithObjects);
                holder.setOnClickListener(R.id.ll_content, onClickWithObjects);
            }
        };
    }

    private void onContentClick(PlanItemEntity entity) {
        if (entity != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, entity.getObjectId());
            bundle.putParcelableArrayList(CommonUtil.KEY_VALUE_2, entity.getCells());
            startActivityForResult(PlanCellsActivity.class, CommonUtil.REQ_CODE_1, bundle);
        }
    }

    private void onDeleteClick(PlanItemEntity entity) {
        if (tipDialog == null) {
            tipDialog = new TipDialog<>(this, this);
        }
        tipDialog.show("是否要删除当前点位计划，删除后不能恢复!", "确定", "我再想想", entity);
    }

    private void onEditClick(PlanItemEntity entity) {
        showEditDialog(entity);
    }

    @Override
    protected void loadData(final int page) {
//        NetService.getInstance().plansList("", null, null)
//                .compose(this.<List<PlanItemEntity>>bindLifeCycle())
//                .subscribe(new CustomApiCallback<List<PlanItemEntity>>() {
//                    @Override
//                    protected void onResultError(ApiException ex) {
//                        showToast(ex.getDisplayMessage());
//                        loadingComplete(false, 1);
//                    }
//
//                    @Override
//                    public void onNext(List<PlanItemEntity> planItemEntities) {
//                        if (FIRST_PAGE == page) {
//                            mItems.clear();
//                        }
//                        if (planItemEntities != null) {
//                            mItems.addAll(planItemEntities);
//                        }
//                        loadingComplete(true, 1);
//                        if (tvTitle != null) {
//                            tvTitle.setText(String.format("我的点位计划(%s)", mItems.size()));
//                        }
//                    }
//                });
    }

    private void showEditDialog(PlanItemEntity entity) {
        if (editDialog == null) {
            editDialog = new PlanEditDialog(this, this);
        }
        editDialog.show(entity, this);
        editDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onEnsureClick(final PlanItemEntity param) {
        if (param == null) {
            return;
        }
        NetService.getInstance().plansDelete(String.valueOf(param.getObjectId()))
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("删除成功");
                        int indexOf = mItems.indexOf(param);
                        if (indexOf >= 0 || indexOf < mItems.size()) {
                            mItems.remove(param);
                            mAdapter.notifyItemRemoved(indexOf);
                        }
                        if (tvTitle != null) {
                            tvTitle.setText(String.format("我的点位计划(%s)", mItems.size()));
                        }
                    }
                });
    }

    @Override
    public void onCancelClick(PlanItemEntity param) {

    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
        showEditDialog(null);
    }

    @Override
    public boolean onResultListner(final PlanItemEntity entity, final String name, final String type) {
        NetService.getInstance().plansAdd(entity != null ? entity.getObjectId() : null, name, type)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        if (editDialog != null && editDialog.isShowing()) {
                            editDialog.dismiss();
                        }
                        if (entity != null) {
                            entity.setPlanName(name);
                            entity.setPlanType(type);
                            mAdapter.notifyDataSetChanged();
                            showToast("修改成功");
                        } else {
                            showToast("添加成功");
                            autoRefresh();
                        }
                    }
                });
        return false;
    }

    @Override
    protected void onResume() {
        if (notFirstIn) {
//            autoRefresh();
        } else {
            notFirstIn = true;
        }
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode && data != null) {
            long planId = data.getLongExtra(CommonUtil.KEY_VALUE_1, 0);
            ArrayList<CellItemEntity> cells = data.getParcelableArrayListExtra(CommonUtil.KEY_VALUE_2);
            if (cells == null) {
                cells = new ArrayList<>();
            }
            for (PlanItemEntity entity : mItems) {
                if (entity.getObjectId() == planId) {
                    entity.setCells(cells);
                }
            }
        }
    }
}
