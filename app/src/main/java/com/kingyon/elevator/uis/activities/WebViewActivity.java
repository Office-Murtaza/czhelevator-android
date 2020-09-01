package com.kingyon.elevator.uis.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.util.UIUtil;
import com.kingyon.elevator.utils.MyStatusBarUtils;
import com.leo.afbaselibrary.widgets.ProgressWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {


    @BindView(R.id.webview)
    ProgressWebView webview;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyStatusBarUtils.setStatusBar(this, "#ffffff");
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("value1");
        LogUtils.e(url,"********");
        if (url!=null) {
            webview.loadUrl(url);
        }
    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            //加载null内容
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清除历史记录
            webview.clearHistory();
            //销毁VebView
            webview.destroy();
            //WebView置为null
            webview = null;
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        }else {
            finish();
        }
    }
}
