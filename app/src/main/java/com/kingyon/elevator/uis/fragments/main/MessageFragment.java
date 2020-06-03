package com.kingyon.elevator.uis.fragments.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.customview.MessageTabView;
import com.kingyon.elevator.entities.MsgNoticeEntity;
import com.kingyon.elevator.entities.MsgUnreadCountEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.HtmlActivity;
import com.kingyon.elevator.uis.activities.MainActivity;
import com.kingyon.elevator.uis.adapters.adapterone.MessageAdapter;
import com.kingyon.elevator.uis.widgets.MessageItemDecornation;
import com.kingyon.elevator.utils.JumpUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.QuickClickUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseStateLoadingFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/2/19.
 * Email：lc824767150@163.com
 */

public class MessageFragment extends BaseStateLoadingFragment {

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
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;


    MessageAdapter messageAdapter;
    private List<MsgNoticeEntity> messageList;
    private LinearLayoutManager layoutManager;
    MessageItemDecornation messageItemDecornation;
    MsgUnreadCountEntity msgUnreadCountEntity;
    private int startPosition = 0;
    private int size = 30;


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
        messageItemDecornation = new MessageItemDecornation();
        messageAdapter = new MessageAdapter(getActivity(), messageList);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        message_list_container.setLayoutManager(layoutManager);
        message_list_container.addItemDecoration(messageItemDecornation);
        message_list_container.setAdapter(messageAdapter);
        messageAdapter.setBaseOnItemClick(new BaseOnItemClick<MsgNoticeEntity>() {
            @Override
            public void onItemClick(MsgNoticeEntity data, int position) {
//                if (QuickClickUtils.isFastClick()) {
                    setMsgRead(data.getId(), position);
                    if (data.getType().equals(Constants.MessageType.OFFICIAL.getValue())) {
                        HtmlActivity.start(getActivity(), data.getTitle(), data.getLink());
                    } else {
                        JumpUtils.getInstance().jumpToMessagePage(getActivity(), data);
                    }
//                }

            }
        });
        messageAdapter.setBaseOnLongItemClick(new BaseOnItemClick<MsgNoticeEntity>() {
            @Override
            public void onItemClick(MsgNoticeEntity data, int position) {
                deleteMsgAction(data, position);
            }
        });
        smart_refresh_layout.setEnableAutoLoadMore(false);
        smart_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMsgList(ReflashConstants.LoadMore);
            }
        });
        smart_refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smart_refresh_layout.setEnableLoadMore(true);
                getUnreadCount();
                loadMsgList(ReflashConstants.Refalshing);
            }
        });
    }

    private void deleteMsgAction(MsgNoticeEntity data, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("确定要删除这条消息吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMsg(data.getId(), position);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void loadData() {
        getUnreadCount();
        smart_refresh_layout.autoRefresh();
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


    @OnClick({R.id.tab_comment, R.id.tab_notice, R.id.tab_helper, R.id.tab_dianzan, R.id.set_all_read})
    public void OnClick(View view) {
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
                case R.id.set_all_read:
                    setAllRead();
                    break;
            }

    }


    /**
     * 获取未读数
     */
    private void getUnreadCount() {
        NetService.getInstance().getUnreadCount()
                .subscribe(new CustomApiCallback<MsgUnreadCountEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("未读数获取失败：" + GsonUtils.toJson(ex));
                    }

                    @Override
                    public void onNext(MsgUnreadCountEntity data) {
                        msgUnreadCountEntity = data;
                        updateView();
                    }
                });
    }

    private void updateView() {
        if (msgUnreadCountEntity != null) {
            tab_notice.showUnReadCount(msgUnreadCountEntity.getNotice());
            tab_helper.showUnReadCount(msgUnreadCountEntity.getReview());
            tab_dianzan.showUnReadCount(msgUnreadCountEntity.getUserLike());
            tab_comment.showUnReadCount(msgUnreadCountEntity.getUserComment());
            int allCount=msgUnreadCountEntity.getNotice()+
                    msgUnreadCountEntity.getReview()+
                    msgUnreadCountEntity.getUserComment()+msgUnreadCountEntity.getUserLike();
            ((MainActivity)getActivity()).showMessageUnreadCount(allCount);
        }else {
            ((MainActivity)getActivity()).showMessageUnreadCount(0);
        }
    }

    public void loadMsgList(int reflashType) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getIndexList(startPosition, size)
                .subscribe(new CustomApiCallback<List<MsgNoticeEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgressDailog();
                    }

                    @Override
                    public void onNext(List<MsgNoticeEntity> dianZanEntities) {
                        hideProgressDailog();
                        if (reflashType == ReflashConstants.Refalshing) {
                            messageList = dianZanEntities;
                        } else {
                            messageList.addAll(dianZanEntities);
                            if (dianZanEntities.size() == 0) {
                                loadMoreIsComplete();
                            }
                        }
                        startPosition = messageList.size();
                        LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + dianZanEntities.size());
                        showMsgNoticeListData(messageList);
                    }
                });
    }


    public void hideProgressDailog() {
        smart_refresh_layout.finishLoadMore();
        smart_refresh_layout.finishRefresh();
    }

    public void showMsgNoticeListData(List<MsgNoticeEntity> dianZanEntities) {
        if (messageAdapter != null) {
            messageAdapter.reflashData(dianZanEntities);
        }
    }

    public void loadMoreIsComplete() {
        smart_refresh_layout.setEnableLoadMore(false);
    }


    public void setMsgRead(int msgId, int position) {
        NetService.getInstance().setMsgRead(msgId)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showShort(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String data) {
                        setReadSuccess(position);
                    }
                });
    }

    public void setReadSuccess(int position) {
        if (position < messageList.size()) {
            messageList.get(position).setIsRead(true);
        }
        messageAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        getUnreadCount();
    }

    public void deleteMsg(int msgId, int position) {
        showProgressDialog("处理中...");
        NetService.getInstance().deleteMessage(msgId)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        ToastUtils.showShort(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String data) {
                        hideProgress();
                        deleteMsgSuccess(position);
                    }
                });
    }


    public void setAllRead() {
        showProgressDialog("处理中...");
        NetService.getInstance().setAllIsRead()
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        ToastUtils.showShort(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String data) {
                        hideProgress();
                        setItemAllRead();
                        getUnreadCount();
                    }
                });
    }

    public void deleteMsgSuccess(int position) {
        if (position < messageList.size()) {
            messageList.remove(position);
        }
        messageAdapter.notifyDataSetChanged();
    }

    public void setItemAllRead() {
        if (messageAdapter != null && messageList.size() > 0) {
            for (MsgNoticeEntity msg : messageList) {
                msg.setIsRead(true);
            }
            messageAdapter.notifyDataSetChanged();
        }
    }
}
