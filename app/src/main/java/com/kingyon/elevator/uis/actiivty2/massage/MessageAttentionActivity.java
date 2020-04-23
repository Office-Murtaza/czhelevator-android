package com.kingyon.elevator.uis.actiivty2.massage;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.presenter2.MessageAttentionPresenter;

/**
 * @Created By Admin  on 2020/4/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:消息关注
 */
public class MessageAttentionActivity extends MvpBaseActivity<MessageAttentionPresenter> {
    @Override
    public MessageAttentionPresenter initPresenter() {
        return new MessageAttentionPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_attention);
    }
}
