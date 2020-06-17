package com.kingyon.elevator.uis.activities.homepage;

import android.os.Bundle;
import android.webkit.WebView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.RichTextUtil;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;

import butterknife.BindView;

/**
 * Created by GongLi on 2018/12/26.
 * Email：lc824767150@163.com
 */

public class WikiDetailsActivity extends BaseStateLoadingActivity {
    @BindView(R.id.pre_web_view)
    WebView preWebView;

    private long wikiId;

    @Override
    protected String getTitleText() {
        wikiId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        return getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_wiki_details;
    }

    @Override
    protected void loadData() {
        NetService.getInstance().knowledgeDetils(wikiId)
                .compose(this.<DataEntity<String>>bindLifeCycle())
                .subscribe(new CustomApiCallback<DataEntity<String>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(DataEntity<String> dataEntity) {
                        RichTextUtil.setRichText(WikiDetailsActivity.this, preWebView, dataEntity.getData(), null);
                        loadingComplete(STATE_CONTENT);
                    }
                });
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }
}
