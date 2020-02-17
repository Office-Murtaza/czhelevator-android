package com.kingyon.elevator.uis.fragments.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.customview.MessageTabView;
import com.kingyon.elevator.customview.PlanSelectDateNewDialog;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.SelectDateEntity;
import com.kingyon.elevator.entities.TabPagerEntity;
import com.kingyon.elevator.interfaces.PlanSelectDateLinsener;
import com.kingyon.elevator.uis.adapters.MessageAdapter;
import com.kingyon.elevator.uis.widgets.MessageItemDecornation;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.QuickClickUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.uis.adapters.UnLazyAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseRefreshFragment;
import com.leo.afbaselibrary.uis.fragments.BaseRefreshLoadingFragment;
import com.leo.afbaselibrary.uis.fragments.BaseStateLoadingFragment;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshFragment;
import com.leo.afbaselibrary.uis.fragments.BaseTabFragment;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.widgets.PagerSlidingTabStrip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/2/19.
 * Email：lc824767150@163.com
 */

public class MessageFragment extends BaseStateRefreshFragment {

    @BindView(R.id.fl_title)
    FrameLayout fl_title;
    @BindView(R.id.message_list_container)
    RecyclerView message_list_container;
    @BindView(R.id.tab_notice)
    MessageTabView tab_notice;
    @BindView(R.id.tab_helper)
    MessageTabView tab_helper;
    @BindView(R.id.tab_dianzan)
    MessageTabView tab_dianzan;
    @BindView(R.id.tab_comment)
    MessageTabView tab_comment;

    MessageAdapter messageAdapter;
    private List<Object> messageList;
    private LinearLayoutManager layoutManager;
    MessageItemDecornation messageItemDecornation;


    @Override
    public void onRefresh() {
        loadingComplete(STATE_CONTENT);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_message_center;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        StatusBarUtil.setHeadViewPadding(getActivity(), fl_title);
        loadingComplete(STATE_CONTENT);
        messageList = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            messageList.add(""+i);
        }
        messageItemDecornation = new MessageItemDecornation();
        messageAdapter = new MessageAdapter(getActivity(),messageList);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        message_list_container.setLayoutManager(layoutManager);
        message_list_container.addItemDecoration(messageItemDecornation);
        message_list_container.setAdapter(messageAdapter);

    }

    public static MessageFragment newInstance() {

        Bundle args = new Bundle();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void dealLeackCanary() {

    }

    @OnClick({R.id.tab_comment,R.id.tab_notice,R.id.tab_helper,R.id.tab_dianzan})
    public void OnClick(View view){
        switch (view.getId()) {
            case R.id.tab_comment:
                MyActivityUtils.goFragmentContainerActivity(getActivity(), FragmentConstants.CommentListFragment);
                break;
            case R.id.tab_notice:
                MyActivityUtils.goFragmentContainerActivity(getActivity(), FragmentConstants.NoticeListFragment);
                break;
            case R.id.tab_helper:
                MyActivityUtils.goFragmentContainerActivity(getActivity(), FragmentConstants.HelperListFragment);
                break;
            case R.id.tab_dianzan:
                MyActivityUtils.goFragmentContainerActivity(getActivity(), FragmentConstants.DianZanListFragment);
                break;
        }
    }

}
