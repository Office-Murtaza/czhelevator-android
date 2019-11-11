package com.kingyon.elevator.uis.activities.devices;

import android.os.Bundle;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.LiftElemEntity;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.List;

/**
 * Created by GongLi on 2019/1/17.
 * Email：lc824767150@163.com
 */

public class LiftChooseActivity extends BuildChooseActivity {
    @Override
    protected String getTitleText() {
        super.getTitleText();
        return "电梯管理";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_lift_choose;
    }

    @Override
    protected MultiItemTypeAdapter<NormalElemEntity> getAdapter() {
        return new BaseAdapter<NormalElemEntity>(this, R.layout.activity_lift_choose_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, NormalElemEntity item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getName());
                if (item instanceof LiftElemEntity) {
                    holder.setTextNotHide(R.id.tv_lift_no, String.format("注册代码:%s", ((LiftElemEntity) item).getLiftNo()));
                    holder.setVisible(R.id.tv_lift_no, true);
                } else {
                    holder.setVisible(R.id.tv_lift_no, false);
                }
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

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().getLiftByUnit(parentId)
                .compose(this.<List<LiftElemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<LiftElemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(true, 1);
                    }

                    @Override
                    public void onNext(List<LiftElemEntity> liftElemEntities) {
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (liftElemEntities != null) {
                            mItems.addAll(liftElemEntities);
                        }
                        loadingComplete(true, 1);
                    }
                });
    }

    @Override
    protected void onEditImplement(NormalElemEntity entity) {
        LiftElemEntity item = (LiftElemEntity) entity;
        Bundle bundle = new Bundle();
        bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
        bundle.putParcelable(CommonUtil.KEY_VALUE_2, item);
        bundle.putLong(CommonUtil.KEY_VALUE_3, parentId);
        startActivityForResult(LiftEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
    }

    @Override
    protected void onCreateImplement() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CommonUtil.KEY_VALUE_1, false);
        bundle.putLong(CommonUtil.KEY_VALUE_3, parentId);
        startActivityForResult(LiftEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
    }

    @Override
    protected void onDelete(NormalElemEntity item) {
        NetService.getInstance().removeLift(item.getObjectId())
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
}
