package com.kingyon.elevator.uis.activities.salesman;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.uis.activities.devices.UnitChooseActivity;
import com.kingyon.elevator.utils.CommonUtil;

/**
 * Created by GongLi on 2019/1/18.
 * Email：lc824767150@163.com
 */

public class SalesBuildUnitActivity extends UnitChooseActivity {
    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, NormalElemEntity item, int position) {
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjectId());
//            bundle.putString(CommonUtil.KEY_VALUE_2, item.getName());
            bundle.putString(CommonUtil.KEY_VALUE_2, String.format("%s-%s", superior, item.getName()));
            startActivity(SalesUnitLiftActivity.class, bundle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onfresh();
    }
}