package com.kingyon.elevator.uis.activities.salesman;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.devices.CellEditActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import butterknife.OnClick;

/**
 * Created by GongLi on 2018/12/24.
 * Email：lc824767150@163.com
 */

public class SalesmanActivity extends BaseStateRefreshingLoadingActivity<CellItemEntity> {

    @Override
    protected String getTitleText() {
        return "小区管理";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_salesman;
    }

    @Override
    protected MultiItemTypeAdapter<CellItemEntity> getAdapter() {
        return new BaseAdapter<CellItemEntity>(this, R.layout.activity_salesman_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, CellItemEntity item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getCellName());
                holder.setTextNotHide(R.id.tv_region, item.getRegionName());
                holder.setTextNotHide(R.id.tv_lift_num, String.format("%s部电梯", item.getLiftNum()));
                holder.setOnClickListener(R.id.tv_edit, new OnClickWithObjects(item) {
                    @Override
                    public void onClick(View view, Object[] objects) {
                        onEditClick(((CellItemEntity) objects[0]));
                    }
                });
                holder.setVisible(R.id.tv_edit, item.isCanEdit());
            }
        };
    }

    private void onEditClick(CellItemEntity entity) {
        if (entity != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
            bundle.putLong(CommonUtil.KEY_VALUE_2, entity.getObjctId());
            startActivityForResult(CellEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, CellItemEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjctId());
            bundle.putString(CommonUtil.KEY_VALUE_2, item.getCellName());
            startActivity(SalesCellBuildActivity.class, bundle);
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, CellItemEntity item, int position) {
        if (item != null && item.isCanEdit()) {
            showDeleteDialog(item);
        }
        return true;
    }

    private void showDeleteDialog(final CellItemEntity item) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("删除以后无法找回，确定要删除它吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDelete(item);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void onDelete(CellItemEntity item) {
        NetService.getInstance().removeCell(item.getObjctId())
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("操作成功");
                        autoRefresh();
                    }
                });
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().partnerCellList(null, null, page)
                .compose(this.<PageListEntity<CellItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<CellItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<CellItemEntity> cellItemEntityPageListEntity) {
                        if (cellItemEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (cellItemEntityPageListEntity.getContent() != null) {
                            mItems.addAll(cellItemEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, cellItemEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onfresh();
    }

    @OnClick(R.id.tv_add)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CommonUtil.KEY_VALUE_1, false);
        startActivityForResult(CellEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
    }
}
