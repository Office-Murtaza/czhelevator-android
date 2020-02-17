package com.kingyon.elevator.uis.fragments.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.NoticeOrHelperPresenter;
import com.kingyon.elevator.uis.adapters.DianZanAdapter;
import com.kingyon.elevator.uis.adapters.MessageDetailsAdapter;
import com.kingyon.elevator.uis.widgets.MessageItemDecornation;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 点赞列表界面
 */
public class DianZanListFragment extends MvpBaseFragment<NoticeOrHelperPresenter> {

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.message_list_container)
    RecyclerView message_list_container;
    private List<Object> messageList;
    private LinearLayoutManager layoutManager;
    MessageItemDecornation messageItemDecornation;
    private DianZanAdapter dianZanAdapter;


    @Override
    public NoticeOrHelperPresenter initPresenter() {
        return new NoticeOrHelperPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        messageList = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            messageList.add(""+i);
        }
        messageItemDecornation = new MessageItemDecornation();
        dianZanAdapter = new DianZanAdapter(getActivity(),messageList);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        message_list_container.setLayoutManager(layoutManager);
        //message_list_container.addItemDecoration(messageItemDecornation);
        message_list_container.setAdapter(dianZanAdapter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_notice_or_helper_layout;
    }

    public static DianZanListFragment newInstance() {

        Bundle args = new Bundle();
        DianZanListFragment fragment = new DianZanListFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
