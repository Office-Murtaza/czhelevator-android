package com.kingyon.elevator.uis.activities.property;

import android.os.Bundle;

import com.kingyon.elevator.uis.activities.cooperation.CooperationDevicesDetailsActivity;
import com.kingyon.elevator.utils.CommonUtil;

/**
 * Created by GongLi on 2019/1/15.
 * Email：lc824767150@163.com
 */

public class PropertyDevicesDetailsActivity extends CooperationDevicesDetailsActivity {
    protected void onReportClick() {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonUtil.KEY_VALUE_1, deviceId);
        startActivityForResult(PropertyDeviceReportActivity.class, CommonUtil.REQ_CODE_1, bundle);
    }
}
