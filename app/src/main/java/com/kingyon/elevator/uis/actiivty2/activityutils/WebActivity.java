package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.WEB_ACTIVITY;
import static com.czh.myversiontwo.utils.StringContent.getHtmlData;

/**
 * @Created By Admin  on 2020/7/8
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
@Route(path = WEB_ACTIVITY)
public class WebActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.webview)
    WebView webview;
    @Autowired
    String title;
    @Autowired
    String content;

    @Override
    public int getContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        tvTopTitle.setText(title);
        webview.loadDataWithBaseURL(null, getHtmlData(content), "text/html", "utf-8", null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }
}
