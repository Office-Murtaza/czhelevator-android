package com.kingyon.elevator.uis.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.MyStatusBarUtils;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {


    @BindView(R.id.webview)
    WebView webview;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyStatusBarUtils.setStatusBar(this, "#ffffff");
        ActivityUtil.addActivity(this);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("value1");
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        if (url!=null) {
            webview.loadUrl(url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.clearCache(true);
        webview.destroy();
        webview=null;
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
