package com.kingyon.elevator.uis.fragments.message;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.DianZanEntity;
import com.kingyon.elevator.entities.MsgCommentEntity;
import com.kingyon.elevator.entities.MsgNoticeEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.NoticeOrHelperPresenter;
import com.kingyon.elevator.uis.activities.HtmlActivity;
import com.kingyon.elevator.uis.adapters.adapterone.MessageDetailsAdapter;
import com.kingyon.elevator.uis.widgets.MessageItemDecornation;
import com.kingyon.elevator.utils.JumpUtils;
import com.kingyon.elevator.utils.QuickClickUtils;
import com.kingyon.elevator.view.NoticeOrHelperView;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeOrHelperFragment extends MvpBaseFragment<NoticeOrHelperPresenter> implements NoticeOrHelperView {

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.message_list_container)
    RecyclerView message_list_container;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    private LinearLayoutManager layoutManager;
    MessageItemDecornation messageItemDecornation;
    private MessageDetailsAdapter messageAdapter;
    private int msgType = -1;


    @Override
    public NoticeOrHelperPresenter initPresenter() {
        return new NoticeOrHelperPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        if (getArguments() != null) {
            msgType = getArguments().getInt("msgType");
            messageItemDecornation = new MessageItemDecornation();
            messageAdapter = new MessageDetailsAdapter(getActivity(), presenter.getMsgNoticeEntityList());
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            message_list_container.setLayoutManager(layoutManager);
            //message_list_container.addItemDecoration(messageItemDecornation);
            message_list_container.setAdapter(messageAdapter);
            messageAdapter.setBaseOnItemClick(new BaseOnItemClick<MsgNoticeEntity>() {
                @Override
                public void onItemClick(MsgNoticeEntity data, int position) {
                        presenter.setMsgRead(data.getId(), position);
                        if (data.getType().equals(Constants.MessageType.OFFICIAL.getValue())) {
                            HtmlActivity.start(getActivity(), data.getTitle(), data.getLink());
                        } else {
                            JumpUtils.getInstance().jumpToMessagePage(getActivity(), data);
                        }

                }
            });
            messageAdapter.setBaseOnLongItemClick(new BaseOnItemClick<MsgNoticeEntity>() {
                @Override
                public void onItemClick(MsgNoticeEntity data, int position) {
                    deleteMsg(data, position);
                }
            });
            smart_refresh_layout.setEnableAutoLoadMore(false);
            smart_refresh_layout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    smart_refresh_layout.setEnableLoadMore(true);
                    if (msgType == 1) {
                        presenter.loadNoticeList(ReflashConstants.Refalshing);
                    } else if (msgType == 2) {
                        presenter.loadXiaoZhuShouList(ReflashConstants.Refalshing);
                    } else {
                        hideProgressDailog();
                    }
                }
            });
            smart_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    if (msgType == 1) {
                        presenter.loadNoticeList(ReflashConstants.LoadMore);
                    } else if (msgType == 2) {
                        presenter.loadXiaoZhuShouList(ReflashConstants.LoadMore);
                    } else {
                        hideProgressDailog();
                    }
                }
            });
            smart_refresh_layout.autoRefresh();
        } else {
            getActivity().finish();
        }
    }

    private void deleteMsg(MsgNoticeEntity data, int position) {
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

    public static NoticeOrHelperFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("msgType", type);
        NoticeOrHelperFragment fragment = new NoticeOrHelperFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void hideProgressDailog() {
        super.hideProgressDailog();
        smart_refresh_layout.finishLoadMore();
        smart_refresh_layout.finishRefresh();
    }

    @Override
    public void showDianZanListData(List<DianZanEntity> dianZanEntities) {

    }

    @Override
    public void loadMoreIsComplete() {
        smart_refresh_layout.setEnableLoadMore(false);
    }

    @Override
    public void showNoticeListData(List<MsgNoticeEntity> msgNoticeEntityList) {
        if (messageAdapter != null) {
            messageAdapter.reflashData(msgNoticeEntityList);
        }
        if (msgNoticeEntityList.size()>0) {
            stateLayout.showContentView();
        }else {
            stateLayout.setEmptyViewTip("  ");
            stateLayout.showEmptyView("暂无数据");
        }
    }

    @Override
    public void showXiaoZhuShouListData(List<MsgNoticeEntity> msgNoticeEntityList) {
        if (messageAdapter != null) {
            messageAdapter.reflashData(msgNoticeEntityList);
        }
        if (msgNoticeEntityList.size()>0) {
            stateLayout.showContentView();
        }else {
            stateLayout.setEmptyViewTip("  ");
            stateLayout.showEmptyView("暂无数据");
        }
    }

    @Override
    public void showCommentListData(List<MsgCommentEntity> msgNoticeEntityList) {

    }

    @Override
    public void setReadSuccess(int position) {
        if (position < presenter.getMsgNoticeEntityList().size()) {
            presenter.getMsgNoticeEntityList().get(position).setIsRead(true);
        }
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteMsgSuccess(int position) {
        if (position < presenter.getMsgNoticeEntityList().size()) {
            presenter.getMsgNoticeEntityList().remove(position);
        }
        messageAdapter.notifyDataSetChanged();
    }
}
