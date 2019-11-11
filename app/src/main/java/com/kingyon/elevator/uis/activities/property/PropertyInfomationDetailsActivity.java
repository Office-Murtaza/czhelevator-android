package com.kingyon.elevator.uis.activities.property;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import butterknife.BindView;

/**
 * Created by GongLi on 2019/1/15.
 * Email：lc824767150@163.com
 */

public class PropertyInfomationDetailsActivity extends BaseSwipeBackActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private OrderDetailsEntity entity;

    @Override
    protected String getTitleText() {
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        if (entity == null) {
            entity = new OrderDetailsEntity();
        }
        return "信息详情";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_property_infomation_details;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvContent.setText(entity.getAdvertising() != null ? entity.getAdvertising().getTitle() : (TextUtils.isEmpty(entity.getInformationAdContent()) ? "" : entity.getInformationAdContent()));
        tvTime.setText(String.format("发布时间：%s", TimeUtil.getYmdCh(entity.getCreateTime())));
    }
}
