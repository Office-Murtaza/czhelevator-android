package com.kingyon.elevator.presenter.presenter2;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.ArticleDetailstView;
import com.kingyon.elevator.view.NewsDetailsView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created By Admin  on 2020/8/19
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ArticleDetailstPresenter extends BasePresenter<ArticleDetailstView> {

    private int startPosition = 0;
    private int size = 20;
    private List<CommentListEntity> commentEntities;


    public ArticleDetailstPresenter(Context mContext) {
        super(mContext);
        commentEntities = new ArrayList<>();
    }

    public List<CommentListEntity> getCommentEntities() {
        return commentEntities;
    }

    public void loadCommentList(int reflashType, int id) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().setQueryListComment(startPosition, id)
                .subscribe(new CustomApiCallback<ConentEntity<CommentListEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (startPosition == 0) {
                                getView().showErrorView();
                            }
                        }
                    }
                    @Override
                    public void onNext(ConentEntity<CommentListEntity> incomeDetailsEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                commentEntities = incomeDetailsEntities.getContent();
                            } else {
                                commentEntities.addAll(incomeDetailsEntities.getContent());
                                if (incomeDetailsEntities.getContent().size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = commentEntities.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.getContent().size());
                            getView().showListData(commentEntities);
                        }
                    }
                });


    }
}
