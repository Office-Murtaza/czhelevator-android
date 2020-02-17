package com.kingyon.elevator.presenter;

import android.content.Context;

import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.view.MessageCenterView;

public class MessageCenterPresenter extends BasePresenter<MessageCenterView> {

    public MessageCenterPresenter(Context mContext) {
        super(mContext);
    }

}
