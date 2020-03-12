package com.kingyon.elevator.uis.fragments.message;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.DianZanEntity;
import com.kingyon.elevator.entities.MsgCommentEntity;
import com.kingyon.elevator.entities.MsgNoticeEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.NoticeOrHelperPresenter;
import com.kingyon.elevator.uis.activities.NewsDetailsActivity;
import com.kingyon.elevator.uis.adapters.DianZanAdapter;
import com.kingyon.elevator.uis.adapters.MessageDetailsAdapter;
import com.kingyon.elevator.uis.widgets.MessageItemDecornation;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.QuickClickUtils;
import com.kingyon.elevator.view.NoticeOrHelperView;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 点赞列表界面
 */
public class DianZanListFragment extends MvpBaseFragment<NoticeOrHelperPresenter> implements NoticeOrHelperView {

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.message_list_container)
    RecyclerView message_list_container;
    private LinearLayoutManager layoutManager;
    MessageItemDecornation messageItemDecornation;
    private DianZanAdapter dianZanAdapter;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;

    @Override
    public NoticeOrHelperPresenter initPresenter() {
        return new NoticeOrHelperPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        messageItemDecornation = new MessageItemDecornation();
        dianZanAdapter = new DianZanAdapter(getActivity(), presenter.getDianZanEntityList());
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        message_list_container.setLayoutManager(layoutManager);
        //message_list_container.addItemDecoration(messageItemDecornation);
        message_list_container.setAdapter(dianZanAdapter);
        dianZanAdapter.setBaseOnItemClick(new BaseOnItemClick<DianZanEntity>() {
            @Override
            public void onItemClick(DianZanEntity data, int position) {
                if (QuickClickUtils.isFastClick()) {
                    return;
                }
                presenter.setMsgRead(data.getId(), position);
                MyActivityUtils.goNewsDetailsActivity(getActivity(), data.getNewsId());
            }
        });
        dianZanAdapter.setBaseOnLongItemClick(new BaseOnItemClick<DianZanEntity>() {
            @Override
            public void onItemClick(DianZanEntity data, int position) {
                deleteMsg(data, position);
            }
        });
        smart_refresh_layout.setEnableAutoLoadMore(false);
        smart_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.loadDianZanList(ReflashConstants.LoadMore);
            }
        });
        smart_refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smart_refresh_layout.setEnableLoadMore(true);
                presenter.loadDianZanList(ReflashConstants.Refalshing);
            }
        });
        smart_refresh_layout.autoRefresh();
    }

    private void deleteMsg(DianZanEntity data, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("确定要删除这条消息吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteMsg(data.getId(), position);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
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

    @Override
    public void hideProgressDailog() {
        super.hideProgressDailog();
        smart_refresh_layout.finishRefresh();
        smart_refresh_layout.finishLoadMore();
    }

    @Override
    public void showDianZanListData(List<DianZanEntity> dianZanEntities) {
        if (dianZanAdapter != null) {
            dianZanAdapter.reflashData(dianZanEntities);
        }
        if (dianZanEntities.size()>0) {
            stateLayout.showContentView();
        }else {
            stateLayout.setEmptyViewTip("  ");
            stateLayout.showEmptyView("暂无数据");
        }
    }

    @Override
    public void loadMoreIsComplete() {
        smart_refresh_layout.setEnableLoadMore(false);
    }

    @Override
    public void showNoticeListData(List<MsgNoticeEntity> msgNoticeEntityList) {

    }

    @Override
    public void showXiaoZhuShouListData(List<MsgNoticeEntity> msgNoticeEntityList) {

    }

    @Override
    public void showCommentListData(List<MsgCommentEntity> msgNoticeEntityList) {

    }

    @Override
    public void setReadSuccess(int position) {
        if (position < presenter.getDianZanEntityList().size()) {
            presenter.getDianZanEntityList().get(position).setRead(true);
        }
        dianZanAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteMsgSuccess(int position) {
        if (position < presenter.getDianZanEntityList().size()) {
            presenter.getDianZanEntityList().remove(position);
        }
        dianZanAdapter.notifyDataSetChanged();
    }
}
