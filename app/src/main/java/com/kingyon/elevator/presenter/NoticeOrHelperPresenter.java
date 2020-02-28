package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.DianZanEntity;
import com.kingyon.elevator.entities.MsgCommentEntity;
import com.kingyon.elevator.entities.MsgNoticeEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.NoticeOrHelperView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.ArrayList;
import java.util.List;

public class NoticeOrHelperPresenter extends BasePresenter<NoticeOrHelperView> {

    private int startPosition = 0;
    private int size = 20;
    private List<DianZanEntity> dianZanEntityList;
    private List<MsgNoticeEntity> msgNoticeEntityList;
    private List<MsgCommentEntity> msgCommentEntityList;

    public NoticeOrHelperPresenter(Context mContext) {
        super(mContext);
        dianZanEntityList = new ArrayList<>();
        msgNoticeEntityList = new ArrayList<>();
        msgCommentEntityList = new ArrayList<>();
    }


    public List<DianZanEntity> getDianZanEntityList() {
        return dianZanEntityList;
    }

    public List<MsgNoticeEntity> getMsgNoticeEntityList() {
        return msgNoticeEntityList;
    }

    public List<MsgCommentEntity> getMsgCommentEntityList() {
        return msgCommentEntityList;
    }

    public void loadDianZanList(int reflashType) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getUserLikeList(startPosition, size)
                .subscribe(new CustomApiCallback<List<DianZanEntity>>() {
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
                    public void onNext(List<DianZanEntity> dianZanEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                dianZanEntityList = dianZanEntities;
                            } else {
                                dianZanEntityList.addAll(dianZanEntities);
                                if (dianZanEntities.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = dianZanEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + dianZanEntities.size());
                            getView().showDianZanListData(dianZanEntityList);
                        }
                    }
                });
    }


    public void loadNoticeList(int reflashType) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getNoticeList(startPosition, size)
                .subscribe(new CustomApiCallback<List<MsgNoticeEntity>>() {
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
                    public void onNext(List<MsgNoticeEntity> msgNoticeEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                msgNoticeEntityList = msgNoticeEntities;
                            } else {
                                msgNoticeEntityList.addAll(msgNoticeEntities);
                                if (msgNoticeEntities.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = msgNoticeEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + msgNoticeEntities.size());
                            getView().showNoticeListData(msgNoticeEntityList);
                        }
                    }
                });
    }


    public void loadXiaoZhuShouList(int reflashType) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getReviewList(startPosition, size)
                .subscribe(new CustomApiCallback<List<MsgNoticeEntity>>() {
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
                    public void onNext(List<MsgNoticeEntity> msgNoticeEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                msgNoticeEntityList = msgNoticeEntities;
                            } else {
                                msgNoticeEntityList.addAll(msgNoticeEntities);
                                if (msgNoticeEntities.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = msgNoticeEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + msgNoticeEntities.size());
                            getView().showXiaoZhuShouListData(msgNoticeEntityList);
                        }
                    }
                });
    }


    /**
     * 获取评论列表数据
     *
     * @param reflashType
     */
    public void loadMsgCommentList(int reflashType) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getUserCommentList(startPosition, size)
                .subscribe(new CustomApiCallback<List<MsgCommentEntity>>() {
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
                    public void onNext(List<MsgCommentEntity> msgNoticeEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                msgCommentEntityList = msgNoticeEntities;
                            } else {
                                msgCommentEntityList.addAll(msgNoticeEntities);
                                if (msgNoticeEntities.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = msgCommentEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + msgNoticeEntities.size());
                            getView().showCommentListData(msgCommentEntityList);
                        }
                    }
                });
    }

    public void setMsgRead(int msgId, int position) {
        NetService.getInstance().setMsgRead(msgId)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                        }
                    }

                    @Override
                    public void onNext(String data) {
                        if (isViewAttached()) {
                            getView().setReadSuccess(position);
                        }
                    }
                });
    }

    public void deleteMsg(int msgId, int position) {
        if (isViewAttached()) {
            getView().showProgressDialog("处理中...",false);
        }
        NetService.getInstance().deleteMessage(msgId)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showShortToast(ex.getDisplayMessage());
                        }
                    }

                    @Override
                    public void onNext(String data) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().deleteMsgSuccess(position);
                        }
                    }
                });
    }
}
