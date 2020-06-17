package com.kingyon.elevator.uis.activities.devices;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.EditDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.RoleUtils;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/17.
 * Email：lc824767150@163.com
 */

public class BuildChooseActivity extends BaseStateRefreshingLoadingActivity<NormalElemEntity> implements EditDialog.OnChangeClickListener<NormalElemEntity> {

    @BindView(R.id.tv_superior)
    TextView tvSuperior;
    @BindView(R.id.tv_dz)
    TextView tvDz;
    protected long parentId;
    protected EditDialog<NormalElemEntity> editDialog;
    protected boolean beInstaller;
    protected String superior;
    protected String regionName;



    @Override
    protected String getTitleText() {
        String myUserRole = AppContent.getInstance().getMyUserRole();
//        beInstaller = TextUtils.isEmpty(myUserRole) || TextUtils.equals(Constants.RoleType.INSTALLER, myUserRole);
        beInstaller = TextUtils.isEmpty(myUserRole) || RoleUtils.getInstance().roleBeTarget(Constants.RoleType.INSTALLER, myUserRole);
        return "楼栋管理";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_build_choose;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        parentId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        superior = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        regionName = getIntent().getStringExtra(CommonUtil.KEY_VALUE_3);
        super.init(savedInstanceState);
        if (!TextUtils.isEmpty(superior)) {
            tvSuperior.setText(superior);
            tvDz.setText(regionName + "");
        } else {
            tvSuperior.setVisibility(View.GONE);
            tvDz.setVisibility(View.GONE);
        }
    }

    @Override
    protected MultiItemTypeAdapter<NormalElemEntity> getAdapter() {
        return new BaseAdapter<NormalElemEntity>(this, R.layout.activity_build_choose_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, NormalElemEntity item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getName());
                holder.setOnClickListener(R.id.tv_edit, new OnClickWithObjects(item) {
                    @Override
                    public void onClick(View view, Object[] objects) {
                        onEditClick(((NormalElemEntity) objects[0]));
                    }
                });
                holder.setVisible(R.id.tv_edit, !beInstaller && item.isCanEdit());
            }
        };
    }

    protected void onEditClick(NormalElemEntity entity) {
        if (entity != null) {
            onEditImplement(entity);
        }
    }

    protected void onEditImplement(NormalElemEntity entity) {
        showEditDialog(entity);
    }

    protected void onCreateImplement() {
        showEditDialog(null);
    }

    protected void showEditDialog(NormalElemEntity entity) {
        LogUtils.e(entity.getName(),entity.getObjectId());
        if (editDialog == null) {
            editDialog = new EditDialog<>(this, this);
        }
        editDialog.show(entity, this, "楼栋", entity != null ? entity.getName() : "", "请输入楼栋名称", false, false);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, NormalElemEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Intent intent = new Intent();
            intent.putExtra(CommonUtil.KEY_VALUE_1, item);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, NormalElemEntity item, int position) {
        if (item != null && !beInstaller && item.isCanEdit()) {
            showDeleteDialog(item);
        }
        return true;
    }

    private void showDeleteDialog(final NormalElemEntity item) {
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

    protected void onDelete(NormalElemEntity item) {
        NetService.getInstance().removeBuilding(item.getObjectId())
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
        NetService.getInstance().getBuildByCell(parentId)
                .compose(this.<List<NormalElemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<NormalElemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(true, 1);
                    }

                    @Override
                    public void onNext(List<NormalElemEntity> normalElemEntities) {
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (normalElemEntities != null) {
                            mItems.addAll(normalElemEntities);
                        }
                        loadingComplete(true, 1);
                    }
                });
    }

    @OnClick(R.id.tv_create)
    public void onViewClicked() {
        onCreateImplement();
        LogUtils.e("1111111111");

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

    @Override
    public boolean onChangeClick(NormalElemEntity entity, String result) {
        return onEditResult(entity, result);
    }

    protected boolean onEditResult(NormalElemEntity entity, String result) {
        NetService.getInstance().addBuilding(entity != null ? entity.getObjectId() : null, parentId, result)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("保存成功");
                        if (editDialog != null && editDialog.isShowing()) {
                            editDialog.dismiss();
                        }
                        autoRefresh();
                    }
                });
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
