package com.kingyon.elevator.uis.actiivty2.massage;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.MessagePushAdapter;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MESSAGE_PUSH;

/**
 * @Created By Admin  on 2020/6/1
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 推送消息
 */
@Route(path = ACTIVITY_MESSAGE_PUSH)
public class MessagePushActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv_robot)
    RecyclerView rvRobot;

    MessagePushAdapter messagePushAdapter;
    @Override
    public int getContentViewId() {
        return R.layout.activity_message_push;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("消息");
        rvRobot.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        messagePushAdapter = new MessagePushAdapter(this,20);
        rvRobot.setAdapter(messagePushAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }
}
