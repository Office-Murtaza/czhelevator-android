package com.kingyon.elevator.uis.actiivty2.massage;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.presenter2.MessageLikePresenter;

/**
 * @Created By Admin  on 2020/4/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:消息点赞
 */
public class MessageLikeActivity extends MvpBaseActivity<MessageLikePresenter> {
    @Override
    public MessageLikePresenter initPresenter() {
        return new MessageLikePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_like);
    }
}
