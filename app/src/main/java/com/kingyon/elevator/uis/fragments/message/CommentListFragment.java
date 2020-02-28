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
import com.kingyon.elevator.uis.adapters.CommentsAdapter;
import com.kingyon.elevator.uis.widgets.MessageItemDecornation;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.view.NoticeOrHelperView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 评论详情界面
 */
public class CommentListFragment extends MvpBaseFragment<NoticeOrHelperPresenter> implements NoticeOrHelperView {

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.message_list_container)
    RecyclerView message_list_container;
    private List<MsgCommentEntity> messageList;
    private LinearLayoutManager layoutManager;
    MessageItemDecornation messageItemDecornation;
    private CommentsAdapter commentsAdapter;


    @Override
    public NoticeOrHelperPresenter initPresenter() {
        return new NoticeOrHelperPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        messageList = new ArrayList<>();
        messageItemDecornation = new MessageItemDecornation();
        commentsAdapter = new CommentsAdapter(getActivity(), presenter.getMsgCommentEntityList());
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        message_list_container.setLayoutManager(layoutManager);
        commentsAdapter.setBaseOnItemClick(new BaseOnItemClick<MsgCommentEntity>() {
            @Override
            public void onItemClick(MsgCommentEntity data, int position) {
                presenter.setMsgRead(data.getId(), position);
                MyActivityUtils.goNewsDetailsActivity(getActivity(), data.getNewsId());
            }
        });
        commentsAdapter.setBaseOnLongItemClick(new BaseOnItemClick<MsgCommentEntity>() {
            @Override
            public void onItemClick(MsgCommentEntity data, int position) {
                deleteMsg(data, position);
            }
        });
        message_list_container.setAdapter(commentsAdapter);
        smart_refresh_layout.setEnableAutoLoadMore(false);
        smart_refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smart_refresh_layout.setEnableLoadMore(true);
                presenter.loadMsgCommentList(ReflashConstants.Refalshing);
            }
        });
        smart_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.loadMsgCommentList(ReflashConstants.LoadMore);
            }
        });
        smart_refresh_layout.autoRefresh();
    }

    private void deleteMsg(MsgCommentEntity data, int position) {
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

    public static CommentListFragment newInstance() {

        Bundle args = new Bundle();
        CommentListFragment fragment = new CommentListFragment();
        fragment.setArguments(args);
        return fragment;
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

    }

    @Override
    public void showXiaoZhuShouListData(List<MsgNoticeEntity> msgNoticeEntityList) {

    }

    @Override
    public void hideProgressDailog() {
        super.hideProgressDailog();
        smart_refresh_layout.finishRefresh();
        smart_refresh_layout.finishLoadMore();
    }

    @Override
    public void showCommentListData(List<MsgCommentEntity> msgNoticeEntityList) {
        if (commentsAdapter != null) {
            commentsAdapter.reflashData(msgNoticeEntityList);
        }
    }

    @Override
    public void setReadSuccess(int position) {
        if (position < presenter.getMsgCommentEntityList().size()) {
            presenter.getMsgCommentEntityList().get(position).setRead(true);
        }
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteMsgSuccess(int position) {
        if (position < presenter.getMsgCommentEntityList().size()) {
            presenter.getMsgCommentEntityList().remove(position);
        }
        commentsAdapter.notifyDataSetChanged();
    }
}
