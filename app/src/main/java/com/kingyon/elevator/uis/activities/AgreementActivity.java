package com.kingyon.elevator.uis.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.RichTextUtil;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/3/2.
 * Email：lc824767150@163.com
 */

public class AgreementActivity extends BaseStateLoadingActivity {

    @BindView(R.id.pre_web_view)
    WebView preWebView;
    @BindView(R.id.pre_v_right)
    TextView preVRight;

    private String type;

    public static void start(BaseActivity baseActivity, String title, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonUtil.KEY_VALUE_1, title);
        bundle.putString(CommonUtil.KEY_VALUE_2, type);
        baseActivity.startActivity(AgreementActivity.class, bundle);
    }

    @Override
    protected String getTitleText() {
        type = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        return getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        preVRight.setText("");
        preVRight.setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {
        NetService.getInstance().richText(type)
                .compose(this.<DataEntity<String>>bindLifeCycle())
                .subscribe(new CustomApiCallback<DataEntity<String>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(DataEntity<String> dataEntity) {
                        RichTextUtil.setRichText(AgreementActivity.this, preWebView, dataEntity.getData(), null);
                        loadingComplete(STATE_CONTENT);
                    }
                });
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
    }
}
