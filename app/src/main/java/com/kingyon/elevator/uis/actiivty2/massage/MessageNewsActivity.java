package com.kingyon.elevator.uis.actiivty2.massage;

import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.presenter2.MessageNewsPresenter;

/**
 * @Created By Admin  on 2020/4/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MessageNewsActivity extends MvpBaseActivity<MessageNewsPresenter> {
    @Override
    public MessageNewsPresenter initPresenter() {
        return new MessageNewsPresenter(this);
    }
}
