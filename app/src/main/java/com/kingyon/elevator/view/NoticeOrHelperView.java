package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.DianZanEntity;
import com.kingyon.elevator.entities.MsgCommentEntity;
import com.kingyon.elevator.entities.MsgNoticeEntity;
import com.kingyon.elevator.mvpbase.BaseView;

import java.util.List;

public interface NoticeOrHelperView extends BaseView {

    void showDianZanListData(List<DianZanEntity> dianZanEntities);

    /**
     * 已经全部加载完成了
     */
    void  loadMoreIsComplete();


    void showNoticeListData(List<MsgNoticeEntity> msgNoticeEntityList);


    void showXiaoZhuShouListData(List<MsgNoticeEntity> msgNoticeEntityList);

    void showCommentListData(List<MsgCommentEntity> msgNoticeEntityList);

    void setReadSuccess(int position);

    void deleteMsgSuccess(int position);
}
