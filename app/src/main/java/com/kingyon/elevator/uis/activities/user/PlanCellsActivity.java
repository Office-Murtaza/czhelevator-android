package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.PlanCellDeleteEntity;
import com.kingyon.elevator.uis.activities.homepage.CellDetailsActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
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

public class PlanCellsActivity extends BaseStateRefreshingLoadingActivity<CellItemEntity> {

    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.tv_delete_all)
    TextView tvDeleteAll;
    @BindView(R.id.tv_delete_number)
    TextView tvDeleteNumber;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;

    private boolean deleteMode;
    private long planId;
    private ArrayList<CellItemEntity> datas;

    @Override
    protected String getTitleText() {
        planId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        datas = getIntent().getParcelableArrayListExtra(CommonUtil.KEY_VALUE_2);
        return "计划详情";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_plan_cells;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setText("删除");
        llDelete.setVisibility(View.GONE);
    }

    @Override
    protected MultiItemTypeAdapter<CellItemEntity> getAdapter() {
        return new BaseAdapter<CellItemEntity>(this, R.layout.activity_plan_cells_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, CellItemEntity item, int position) {
                holder.setVisible(R.id.img_choose, deleteMode);
                holder.setSelected(R.id.img_choose, item.isChoosed());
                holder.setImage(R.id.img_cover, item.getCellLogo());
                holder.setTextNotHide(R.id.tv_name, item.getCellName());
                holder.setTextNotHide(R.id.tv_lift, FormatUtils.getInstance().getCellLift(item.getLiftNum()));
                holder.setTextNotHide(R.id.tv_unit, FormatUtils.getInstance().getCellUnit(item.getUnitNum()));
                holder.setTextNotHide(R.id.tv_price, FormatUtils.getInstance().getCellPrice(item.getBusinessAdPrice()));
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, CellItemEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            if (deleteMode && tvDelete.isEnabled()) {
                item.setChoosed(!item.isChoosed());
                mAdapter.notifyItemChanged(position);
                updateEditUI();
            } else {
                Bundle bundle = new Bundle();
                bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjctId());
                startActivity(CellDetailsActivity.class, bundle);
            }
        }
    }

    @Override
    protected void loadData(int page) {
        if (datas != null) {
            mItems.addAll(datas);
            datas = null;
        }
        loadingComplete(true, 1);
    }

    @OnClick({R.id.pre_v_right, R.id.tv_delete_all, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_right:
                deleteMode = !deleteMode;
                updateDeleteUi();
                break;
            case R.id.tv_delete_all:
                setAllPlanEdit(!tvDeleteAll.isSelected());
                break;
            case R.id.tv_delete:
                requestDelete();
                break;
        }
    }

    private void updateDeleteUi() {
        preVRight.setText(deleteMode ? "取消" : "删除");
        llDelete.setVisibility(deleteMode ? View.VISIBLE : View.GONE);
        mAdapter.notifyDataSetChanged();
    }

    private void requestDelete() {
        List<PlanCellDeleteEntity> deleteParams = getDeleteParams();
        if (deleteParams.size() < 1) {
            showToast("需要至少选择一个小区");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvDelete.setEnabled(false);
//        NetService.getInstance().plansRemoveCells(AppContent.getInstance().getGson().toJson(deleteParams))
//                .compose(this.<String>bindLifeCycle())
//                .subscribe(new CustomApiCallback<String>() {
//                    @Override
//                    protected void onResultError(ApiException ex) {
//                        showToast(ex.getDisplayMessage());
//                        hideProgress();
//                        tvDelete.setEnabled(true);
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        showToast("删除成功");
//                        Iterator<CellItemEntity> iterator = mItems.iterator();
//                        while (iterator.hasNext()) {
//                            CellItemEntity item = iterator.next();
//                            if (item.isChoosed()) {
//                                iterator.remove();
//                            }
//                        }
//                        mAdapter.notifyDataSetChanged();
//                        tvDelete.setEnabled(true);
//                        hideProgress();
//                        updateEditUI();
//                    }
//                });
    }

    private void setAllPlanEdit(boolean edit) {
        for (CellItemEntity item : mItems) {
            item.setChoosed(edit);
        }
        mAdapter.notifyDataSetChanged();
        updateEditUI();
    }

    private void updateEditUI() {
        boolean allEdit = true;
        int choosedNum = 0;
        for (CellItemEntity item : mItems) {
            if (item.isChoosed()) {
                choosedNum++;
            } else {
                allEdit = false;
            }
        }
        tvDeleteAll.setSelected(allEdit);
        tvDeleteNumber.setText(String.format("已选%s个小区", choosedNum));
    }

    private List<PlanCellDeleteEntity> getDeleteParams() {
        StringBuilder stringBuilder = new StringBuilder();
        for (CellItemEntity item : mItems) {
            if (item.isChoosed()) {
                stringBuilder.append(item.getObjctId()).append(",");
            }
        }

        List<PlanCellDeleteEntity> params = new ArrayList<>();
        String cellIds = stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : null;
        if (!TextUtils.isEmpty(cellIds)) {
            params.add(new PlanCellDeleteEntity(planId, cellIds));
        }
        return params;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(CommonUtil.KEY_VALUE_1, planId);
        intent.putExtra(CommonUtil.KEY_VALUE_2, mItems);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
