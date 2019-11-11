package com.kingyon.elevator.uis.activities.property;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.entities.CellDeviceNumberEntity;
import com.kingyon.elevator.entities.DeviceNumberEntity;
import com.kingyon.elevator.entities.DeviceNumberInfo;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.cooperation.CooperationDeviceActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;

import java.util.List;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertyDeviceActivity extends CooperationDeviceActivity {

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        if (item != null && item instanceof CellDeviceNumberEntity) {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, ((CellDeviceNumberEntity) item).getCellId());
            bundle.putString(CommonUtil.KEY_VALUE_2, role);
            startActivity(PropertyCellDevicesActivity.class, bundle);
        }
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().deviceNumber()
                .compose(this.<DeviceNumberInfo>bindLifeCycle())
                .subscribe(new CustomApiCallback<DeviceNumberInfo>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 1);
                    }

                    @Override
                    public void onNext(DeviceNumberInfo deviceNumberInfo) {
                        DeviceNumberEntity deviceNumber = deviceNumberInfo.getDeviceNumber();
                        List<CellDeviceNumberEntity> cellDeviceNumbers = deviceNumberInfo.getCellDeviceNumbers();
                        if (deviceNumber == null || cellDeviceNumbers == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.add(deviceNumber);
                        if (cellDeviceNumbers.size() > 0) {
                            mItems.add("tip");
                        }
                        mItems.addAll(cellDeviceNumbers);
                        loadingComplete(true, 1);
                    }
                });
    }
}
