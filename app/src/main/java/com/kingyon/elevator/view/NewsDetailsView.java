package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.mvpbase.BaseView;

import java.util.List;

/**
 * Created By SongPeng  on 2020/2/25
 * Email : 1531603384@qq.com
 */
public interface NewsDetailsView extends BaseView {

    void showListData(List<CommentListEntity> incomeDetailsEntities);

    /**
     * 已经全部加载完成了
     */
    void  loadMoreIsComplete();


    void commentAddSuccess();

    void addLikeCommentSuccess(int position);
}
