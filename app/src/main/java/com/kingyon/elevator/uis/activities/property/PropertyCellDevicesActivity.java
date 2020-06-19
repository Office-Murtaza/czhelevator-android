package com.kingyon.elevator.uis.activities.property;

import android.os.Bundle;

import com.kingyon.elevator.entities.PlanPointGroup;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.property.EquipmentDetailsRevenueActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationCellDevicesActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.List;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertyCellDevicesActivity extends CooperationCellDevicesActivity {

    protected void onDeviceClick(PointItemEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonUtil.KEY_VALUE_1, entity.getObjectId());
        bundle.putString(CommonUtil.KEY_VALUE_2, role);
        startActivity(EquipmentDetailsRevenueActivity.class, bundle);
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
}