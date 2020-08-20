package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.NewsDetailsView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By SongPeng  on 2020/2/25
 * Email : 1531603384@qq.com
 */
public class NewsDetailsPresenter extends BasePresenter<NewsDetailsView> {

    private int startPosition = 0;
    private int size = 20;
    private List<CommentListEntity> commentEntities;

    public NewsDetailsPresenter(Context mContext) {
        super(mContext);
        commentEntities = new ArrayList<>();
    }

    public List<CommentListEntity> getCommentEntities() {
        return commentEntities;
    }

    public void loadCommentList( int newsId, int page) {
        if (page == 1) {
            commentEntities.clear();
        }
        NetService.getInstance().setQueryListComment(page,newsId)
                .subscribe(new CustomApiCallback<ConentEntity<CommentListEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
//                            getView().showShortToast(ex.getDisplayMessage());
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
//                            if (page == 1) {
//                                commentEntities = incomeDetailsEntities.getContent();
//                            } else {
                            if (incomeDetailsEntities.getContent().size() == 0) {
                                getView().loadMoreIsComplete();
                            }
                            for (int i = 0; i < incomeDetailsEntities.getContent().size(); i++) {
                                CommentListEntity commentListEntity = new CommentListEntity();
                                commentListEntity = incomeDetailsEntities.getContent().get(i);
                                commentEntities.add(commentListEntity);
                            }
//                                commentEntities.addAll(incomeDetailsEntities.getContent());

//                            }
//                            commentEntities.addAll(incomeDetailsEntities.getContent());

                            startPosition =incomeDetailsEntities.getTotalPages();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.getContent().size());
                            getView().showListData(commentEntities);
                        }
                    }
                });
    }

    /**
     * 给新闻新增一条评论
     *
     * @param newsId
     * @param content
     */
    public void addNewsComment(int newsId, String content) {
        NetService.getInstance().setComment(newsId, 0,  content)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("添加评论失败：" + GsonUtils.toJson(ex));
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                        }
                    }

                    @Override
                    public void onNext(String content) {
                        if (isViewAttached()) {
                            getView().showShortToast("评论成功");
                            getView().commentAddSuccess();
                        }
                    }
                });

    }

    /**
     * 点赞评论
     */
    public void addLikeComment(int commentId, int position) {
        NetService.getInstance().addLikeComment((long) commentId)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showShort(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String content) {
                        if (isViewAttached()) {
                            getView().addLikeCommentSuccess(position);
                        }
                    }
                });
    }
}
