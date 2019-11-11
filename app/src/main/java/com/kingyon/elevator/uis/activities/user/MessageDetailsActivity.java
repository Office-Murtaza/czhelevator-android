package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NormalMessageEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.TimeUtil;

import butterknife.BindView;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class MessageDetailsActivity extends BaseSwipeBackActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_details)
    TextView tvDetails;

    private NormalMessageEntity messageEntity;

    @Override
    protected String getTitleText() {
        messageEntity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        return "消息详情";
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        tvDetails.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_details;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvName.setText(messageEntity.getTitle());
        tvDetails.setText(messageEntity.getContent());
        tvTime.setText(TimeUtil.getAllTimeNoSecond(messageEntity.getTime()));
    }
}
