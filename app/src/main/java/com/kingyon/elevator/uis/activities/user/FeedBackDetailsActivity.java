package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.FeedBackCache;
import com.kingyon.elevator.entities.FeedBackEntity;
import com.kingyon.elevator.entities.FeedBackMessageEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adapterone.FeedBackDetailsAdaper;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.KeyboardChangeListener;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class FeedBackDetailsActivity extends BaseStateRefreshingLoadingActivity<Object> {
    @BindView(R.id.et_edit)
    EditText etEdit;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.tv_publish)
    TextView tvPublish;

    private long feedBackId;

    private KeyboardChangeListener keyboardChangeListener;

    @Override
    protected String getTitleText() {
        feedBackId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        return "反馈详情";
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        keyboardChangeListener = new KeyboardChangeListener(this);
        keyboardChangeListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    etEdit.requestFocus();
                    changeEditTip();
                } else {
                    llEdit.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_feed_back_details;
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        return new FeedBackDetailsAdaper(this, mItems);
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().getFeedBackInfo(feedBackId, page)
                .compose(this.<FeedBackCache>bindLifeCycle())
                .subscribe(new CustomApiCallback<FeedBackCache>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(FeedBackCache feedBackCache) {
                        FeedBackEntity feedBack = feedBackCache.getFeedBack();
                        PageListEntity<FeedBackMessageEntity> messagePage = feedBackCache.getMessagePage();
                        if (messagePage == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                            if (feedBack == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            mItems.add(feedBack);
                        }
                        if (messagePage.getContent() != null) {
                            mItems.addAll(messagePage.getContent());
                        }
                        loadingComplete(true, messagePage.getTotalPages());
                    }
                });
    }

    private void showEdit() {
        changeEditTip();
        if (llEdit.getVisibility() == View.GONE) {
            llEdit.setVisibility(View.VISIBLE);
            KeyBoardUtils.openKeybord(etEdit, this);
        }
    }

    private void changeEditTip() {
        etEdit.setHint("请输入您的回复");
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }

    @Override
    protected void onDestroy() {
        if (keyboardChangeListener != null) {
            keyboardChangeListener.destroy();
        }
        super.onDestroy();
    }

    @OnClick({R.id.tv_comment, R.id.tv_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_comment:
                showEdit();
                break;
            case R.id.tv_publish:
                onReplyClick();
                break;
        }
    }

    private void onReplyClick() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etEdit))) {
            showToast("请输入回复内容");
            return;
        }
        tvPublish.setEnabled(false);
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().commentFeedBack(feedBackId, etEdit.getText().toString())
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        tvPublish.setEnabled(true);
                        hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("回复成功");
                        autoRefresh();
                        tvPublish.setEnabled(true);
                        etEdit.setText("");
                        etEdit.setSelection(etEdit.getText().length());
                        KeyBoardUtils.closeKeybord(FeedBackDetailsActivity.this);
                        hideProgress();
                    }
                });
    }
}
