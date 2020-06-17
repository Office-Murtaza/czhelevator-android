package com.kingyon.elevator.uis.fragments.homepage;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.MessageCenterPresenter;
import com.kingyon.elevator.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息界面
 */
public class MessageCenterFragment extends MvpBaseFragment<MessageCenterPresenter> {

    @BindView(R.id.fl_title)
    FrameLayout fl_title;
    @BindView(R.id.message_list_container)
    RecyclerView message_list_container;


    @Override
    public MessageCenterPresenter initPresenter() {
        return new MessageCenterPresenter(getActivity());
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        StatusBarUtil.setHeadViewPadding(getActivity(), fl_title);


    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_message_center;
    }

    public static MessageCenterFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MessageCenterFragment fragment = new MessageCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
