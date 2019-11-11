package com.kingyon.elevator.uis.activities.installer;

import android.os.Bundle;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.cooperation.CooperationDevicesDetailsActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class InstallerDeviceDetailsActivity extends CooperationDevicesDetailsActivity {

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setVisibility(View.GONE);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_installer_device_details;
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().deviceDetails(deviceId)
                .compose(this.<PointItemEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<PointItemEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 1);
                    }

                    @Override
                    public void onNext(PointItemEntity pointItemEntity) {
                        if (pointItemEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                            mItems.add(pointItemEntity);
                        }
                        loadingComplete(true, 1);
                    }
                });
    }
}
