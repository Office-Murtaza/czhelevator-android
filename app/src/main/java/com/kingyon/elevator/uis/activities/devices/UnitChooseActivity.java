package com.kingyon.elevator.uis.activities.devices;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.EditDialog;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.List;

/**
 * Created by GongLi on 2019/1/17.
 * Email：lc824767150@163.com
 * 单元管理
 */

public class UnitChooseActivity extends BuildChooseActivity {
    @Override
    protected String getTitleText() {
        super.getTitleText();
        return "单元管理";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_unit_choose;
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().getUnitByBuild(parentId)
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

    @Override
    protected void onEditImplement(NormalElemEntity entity) {
        showEditDialog(entity);
    }

    @Override
    protected void onCreateImplement() {
        showEditDialog(null);
    }

    protected void showEditDialog(NormalElemEntity entity) {
        if (editDialog == null) {
            editDialog = new EditDialog<>(this, this);
        }
        editDialog.show(entity, this, "单元", entity != null ? entity.getName() : "", "请输入单元名称", false, false);
    }

    @Override
    protected boolean onEditResult(NormalElemEntity entity, String result) {
        NetService.getInstance().addUnit(entity != null ? entity.getObjectId() : null, parentId, result)
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
    protected void onDelete(NormalElemEntity item) {
        NetService.getInstance().removeUnit(item.getObjectId())
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
    protected void onResume() {
        super.onResume();
        autoRefresh();
    }
}
