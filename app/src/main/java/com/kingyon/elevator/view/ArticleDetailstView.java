package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.mvpbase.BaseView;

import java.util.List;

/**
 * @Created By Admin  on 2020/8/19
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public interface ArticleDetailstView extends BaseView {
    void showListData(List<CommentListEntity> incomeDetailsEntities);

    /**
     * 已经全部加载完成了
     */
    void  loadMoreIsComplete();


    void commentAddSuccess();

    void addLikeCommentSuccess(int position);
}
