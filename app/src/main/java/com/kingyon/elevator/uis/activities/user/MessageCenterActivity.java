package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.PushMessageEntity;
import com.kingyon.elevator.entities.UnreadNumberEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseRefreshActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class MessageCenterActivity extends BaseRefreshActivity {
    @BindView(R.id.tv_system_message)
    TextView tvSystemMessage;
    @BindView(R.id.tv_system_number)
    TextView tvSystemNumber;
    @BindView(R.id.tv_review_message)
    TextView tvReviewMessage;
    @BindView(R.id.tv_review_number)
    TextView tvReviewNumber;
    @BindView(R.id.tv_push_message)
    TextView tvPushMessage;
    @BindView(R.id.tv_push_number)
    TextView tvPushNumber;

    private boolean notFirstIn;

    @Override
    protected String getTitleText() {
        return "消息";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_center;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        dealOpenActivity(getIntent());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        autoRefresh();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dealOpenActivity(intent);
    }

    @Override
    public void onRefresh() {
        NetService.getInstance().unreadCount()
                .compose(this.<UnreadNumberEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<UnreadNumberEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        updateUnread(new UnreadNumberEntity());
                        refreshComplete();
                    }

                    @Override
                    public void onNext(UnreadNumberEntity unreadNumberEntity) {
                        if (unreadNumberEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        updateUnread(unreadNumberEntity);
                        refreshComplete();
                    }
                });
    }

    @Override
    protected void onResume() {
        if (notFirstIn) {
            onRefresh();
        } else {
            notFirstIn = true;
        }
        super.onResume();
    }

    private void updateUnread(UnreadNumberEntity unreadNumberEntity) {
        setUnreadNumber(tvSystemNumber, unreadNumberEntity.getSystemMessage());
        setUnreadNumber(tvReviewNumber, unreadNumberEntity.getReviewMessage());
        setUnreadNumber(tvPushNumber, unreadNumberEntity.getOfficialMessage());
    }

    @OnClick({R.id.ll_system_message, R.id.ll_review_message, R.id.ll_push_message})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_system_message:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.MessageType.SYSTEM.getValue());
                break;
            case R.id.ll_review_message:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.MessageType.REVIEW.getValue());
                break;
            case R.id.ll_push_message:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.MessageType.OFFICIAL.getValue());
                break;
        }
        startActivity(NormalMessageListActivity.class, bundle);
    }

    private void setUnreadNumber(TextView tvNumber, long number) {
        if (number < 1) {
            tvNumber.setVisibility(View.GONE);
            tvNumber.setText("");
        } else if (number < 100) {
            tvNumber.setVisibility(View.VISIBLE);
            tvNumber.setText(String.valueOf(number));
        } else {
            tvNumber.setVisibility(View.VISIBLE);
            tvNumber.setText(R.string.number_max);
        }
    }

    private void dealOpenActivity(Intent intent) {
        PushMessageEntity messageEntity = intent.getParcelableExtra(CommonUtil.KEY_VALUE_1);
        if (messageEntity == null || messageEntity.getMessage() == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommonUtil.KEY_VALUE_1, messageEntity.getType());
        bundle.putParcelable(CommonUtil.KEY_VALUE_2, messageEntity.getMessage());
        startActivity(NormalMessageListActivity.class, bundle);
    }
}
