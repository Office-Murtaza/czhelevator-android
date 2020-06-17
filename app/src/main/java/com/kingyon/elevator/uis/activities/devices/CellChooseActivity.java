package com.kingyon.elevator.uis.activities.devices;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.RoleUtils;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/17.
 * Email：lc824767150@163.com
 */

public class CellChooseActivity extends BaseStateRefreshingLoadingActivity<CellItemEntity> {

    @BindView(R.id.tv_create)
    TextView tvCreate;

    private Double longitude;
    private Double latitude;
    //    private boolean beFromManager;
    private boolean beInstaller;

    @Override
    protected String getTitleText() {
        longitude = getIntent().getDoubleExtra(CommonUtil.KEY_VALUE_1, 0);
        latitude = getIntent().getDoubleExtra(CommonUtil.KEY_VALUE_2, 0);
//        beFromManager = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_3, false);
        if (longitude == 0) {
            longitude = null;
        }
        if (latitude == 0) {
            latitude = null;
        }
        return "小区管理";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cell_choose;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        String myUserRole = AppContent.getInstance().getMyUserRole();
//        beInstaller = TextUtils.isEmpty(myUserRole) || TextUtils.equals(Constants.RoleType.INSTALLER, myUserRole);
        beInstaller = TextUtils.isEmpty(myUserRole) || RoleUtils.getInstance().roleBeTarget(Constants.RoleType.INSTALLER, myUserRole);
        super.init(savedInstanceState);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        tvCreate.setVisibility(beInstaller ? View.GONE : View.VISIBLE);
    }

    @Override
    protected MultiItemTypeAdapter<CellItemEntity> getAdapter() {
        return new BaseAdapter<CellItemEntity>(this, R.layout.activity_cell_choose_item, mItems) {
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
                holder.setVisible(R.id.tv_edit, !beInstaller && item.isCanEdit());
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
//        if (!beFromManager && item != null) {
//            Intent intent = new Intent();
//            intent.putExtra(CommonUtil.KEY_VALUE_1, item);
//            setResult(RESULT_OK, intent);
//            finish();
//        }
        if (item != null) {
            Intent intent = new Intent();
            intent.putExtra(CommonUtil.KEY_VALUE_1, item);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, CellItemEntity item, int position) {
        if (item != null && !beInstaller && item.isCanEdit()) {
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
        NetService.getInstance().partnerCellList(longitude, latitude, page)
                .compose(this.<ConentEntity<CellItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<CellItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<CellItemEntity> cellItemEntityPageListEntity) {
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

    @OnClick(R.id.tv_create)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CommonUtil.KEY_VALUE_1, false);
        startActivityForResult(CellEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode) {
            autoRefresh();
        }
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }
}
