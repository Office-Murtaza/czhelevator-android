package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.content.VoideDetailsActivity;
import com.kingyon.elevator.uis.actiivty2.content.VoideVerticalDetailsActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentCommentsAdapter;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:自己删除
 */
public class CommentDialog extends Dialog{
    protected BaseActivity mContext;
    RecyclerView rvComment;
    SmartRefreshLayout smartRefreshLayout;
    protected ProgressDialog promotWaitBar;
    int objId;
    int page = 1;
    /**
     *
     * */
    public CommentDialog(@NonNull BaseActivity context, int objId) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.mContext = context;
        this.objId = objId;
        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }
        promotWaitBar = new ProgressDialog(mContext);
        promotWaitBar.setMessage(getContext().getResources().getString(
                com.kingyon.library.social.R.string.hold_on));
    }


    protected int getLayoutId() {
        return com.kingyon.library.social.R.layout.dialog_comment;
    }

    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        rvComment = findViewById(com.kingyon.library.social.R.id.rv_comment);
        smartRefreshLayout = findViewById(com.kingyon.library.social.R.id.smart_refresh_layout);
        httpComment(page,objId);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                httpComment(page,objId);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                listEntities.clear();
                httpComment(page,objId);
            }
        });



    }

    private void httpComment(int page, int objId) {
        NetService.getInstance().setQueryListComment(page,objId)
                .compose(mContext.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<CommentListEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                    }
                    @Override
                    public void onNext(ConentEntity<CommentListEntity> conentEntity) {
                        dataAdd(conentEntity);
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);

                    }
                });
    }

    List<CommentListEntity> listEntities = new ArrayList<>();
    ContentCommentsAdapter contentCommentsAdapter;

    private void dataAdd(ConentEntity<CommentListEntity> conentEntity) {

        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            CommentListEntity commentListEntity = new CommentListEntity();
            commentListEntity = conentEntity.getContent().get(i);
            listEntities.add(commentListEntity);
        }
        if (contentCommentsAdapter == null || page == 1) {
            contentCommentsAdapter = new ContentCommentsAdapter(mContext, "1",
                    new ContentCommentsAdapter.GetRefresh() {
                        @Override
                        public void onRefresh(boolean isSucced) {
                            listEntities.clear();
                            page=1;
                            httpComment(page, objId);
                        }
                    });
            contentCommentsAdapter.addData(listEntities);
            rvComment.setAdapter(contentCommentsAdapter);
            rvComment.setLayoutManager(new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false));
        } else {
            contentCommentsAdapter.addData(listEntities);
            contentCommentsAdapter.notifyDataSetChanged();
        }
    }

}
