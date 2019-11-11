package com.kingyon.elevator.uis.activities.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.NormalMessageEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.HtmlActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.JumpUtils;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.listeners.OnLongClickWithObjects;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class NormalMessageListActivity extends BaseStateRefreshingLoadingActivity<NormalMessageEntity> {

    private String messageType;
    private boolean officialMessages;

    @Override
    protected String getTitleText() {
        messageType = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        officialMessages = TextUtils.equals(Constants.MessageType.OFFICIAL.getValue(), messageType);
        return getMessageTypeName(messageType);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_normal_message_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        NormalMessageEntity messageEntity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_2);
        JumpUtils.getInstance().jumpToMessagePage(this, messageEntity);
    }

    @Override
    protected MultiItemTypeAdapter<NormalMessageEntity> getAdapter() {
        return new BaseAdapter<NormalMessageEntity>(this, R.layout.activity_normal_message_list_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, NormalMessageEntity item, int position) {
                holder.setImage(R.id.img_cover, item.getImage());
                holder.setVisible(R.id.pfl_cover, officialMessages);
                holder.setTextNotHide(R.id.tv_name, item.getTitle());
                holder.setSelected(R.id.tv_name, item.isUnRead());
                holder.setTextNotHide(R.id.tv_details, item.getContent());
                holder.setVisible(R.id.tv_details, !officialMessages);
                holder.setVisible(R.id.v_line, !officialMessages);
                holder.setVisible(R.id.tv_view, !officialMessages);
                holder.setTextNotHide(R.id.tv_time, TimeUtil.getRecentlyTime(item.getTime()));
                holder.setOnClickListener(R.id.ll_message, new OnClickWithObjects(item) {
                    @Override
                    public void onClick(View view, Object[] objects) {
                        onMessageClick((NormalMessageEntity) objects[0]);
                    }
                });
                holder.setOnLongClickListener(R.id.ll_message, new OnLongClickWithObjects(item) {
                    @Override
                    public boolean onLongClick(View view, Object[] objects) {
                        onMessageDeleteClick((NormalMessageEntity) objects[0]);
                        return true;
                    }
                });
            }
        };
    }

    private void onMessageDeleteClick(NormalMessageEntity entity) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要删除本条消息吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestDeleteMessage(entity);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void requestDeleteMessage(NormalMessageEntity entity) {
        NetService.getInstance().deleteMessage(entity.getObjectId())
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("删除成功");
                        if (mItems.contains(entity)) {
                            mItems.remove(entity);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void onMessageClick(NormalMessageEntity entity) {
        if (officialMessages) {
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
//            startActivity(MessageDetailsActivity.class, bundle);
            HtmlActivity.start(this, entity.getTitle(), entity.getLink());
        } else {
            JumpUtils.getInstance().jumpToMessagePage(this, entity);
        }
        readMessage(entity);
    }

    private void readMessage(final NormalMessageEntity entity) {
        if (entity.isUnRead()) {
            NetService.getInstance().messageRead(entity.getObjectId())
                    .compose(this.<String>bindLifeCycle())
                    .subscribe(new CustomApiCallback<String>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                        }

                        @Override
                        public void onNext(String s) {
                            entity.setUnRead(false);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().getMessageList(messageType, page)
                .compose(this.<PageListEntity<NormalMessageEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<NormalMessageEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<NormalMessageEntity> normalMessageEntityPageListEntity) {
                        if (normalMessageEntityPageListEntity == null || normalMessageEntityPageListEntity.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(normalMessageEntityPageListEntity.getContent());
                        loadingComplete(true, normalMessageEntityPageListEntity.getTotalPages());
                    }
                });
    }

    private String getMessageTypeName(String messageType) {
        String result;
        if (TextUtils.equals(Constants.MessageType.SYSTEM.getValue(), messageType)) {
            result = Constants.MessageType.SYSTEM.getName();
        } else if (TextUtils.equals(Constants.MessageType.REVIEW.getValue(), messageType)) {
            result = Constants.MessageType.REVIEW.getName();
        } else if (TextUtils.equals(Constants.MessageType.OFFICIAL.getValue(), messageType)) {
            result = Constants.MessageType.OFFICIAL.getName();
        } else {
            result = "";
        }
        return result;
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }
}
