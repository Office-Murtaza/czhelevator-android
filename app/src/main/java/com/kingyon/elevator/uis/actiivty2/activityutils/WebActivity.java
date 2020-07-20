package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
    @Autowired
    String type;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        LogUtils.e(title, content, type);
        tvTopTitle.setText(title);
        initView();
        if (type.equals("url")) {
            webview.loadUrl(content);
        } else {
            webview.loadDataWithBaseURL(null, getHtmlData(content), "text/html", "utf-8", null);
        }

    }

    private void initView() {
        webview.getSettings().setJavaScriptEnabled(true);
        // 加载需要显示的网页
        webview.setWebViewClient(new WebViewClient() {
            // 复写shouldInterceptRequest
            //API21以下用shouldInterceptRequest(WebView view, String url)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                // 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
                // 此处网页里图片的url为:http://s.ip-cdn.com/img/logo.gif
                // 图片的资源文件名为:logo.gif
                if (url.contains("logo.gif")) {
                    InputStream is = null;
                    // 步骤2:创建一个输入流
                    try {
                        is = getApplicationContext().getAssets().open("images/error.png");
                        // 步骤3:打开需要替换的资源(存放在assets文件夹里)
                        // 在app/src/main下创建一个assets文件夹
                        // assets文件夹里再创建一个images文件夹,放一个error.png的图片
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 步骤4:替换资源
                    WebResourceResponse response = new WebResourceResponse("image/png",
                            "utf-8", is);
                    // 参数1:http请求里该图片的Content-Type,此处图片为image/png
                    // 参数2:编码类型
                    // 参数3:替换资源的输入流
                    System.out.println("旧API");
                    return response;
                }

                return super.shouldInterceptRequest(view, url);

            }

            // API21以上用shouldInterceptRequest(WebView view, WebResourceRequest request)
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                // 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
                // 此处图片的url为:http://s.ip-cdn.com/img/logo.gif
                // 图片的资源文件名为:logo.gif
                if (request.getUrl().toString().contains("logo.gif")) {
                    InputStream is = null;
                    // 步骤2:创建一个输入流
                    try {
                        is = getApplicationContext().getAssets().open("images/error.png");
                        // 步骤3:打开需要替换的资源(存放在assets文件夹里)
                        // 在app/src/main下创建一个assets文件夹
                        // assets文件夹里再创建一个images文件夹,放一个error.png的图片

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //步骤4:替换资源
                    WebResourceResponse response = new WebResourceResponse("image/png",
                            "utf-8", is);
                    // 参数1：http请求里该图片的Content-Type,此处图片为image/png
                    // 参数2：编码类型
                    // 参数3：存放着替换资源的输入流（上面创建的那个）
                    return response;
                }
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                showToast("加载失败");
//                finish();
            }
        });
        WebSettings seting=webview.getSettings();
        seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                if(newProgress==100){
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }

            }
        });
    }
    //设置返回键动作（防止按返回键直接退出程序)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if(webview.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                webview.goBack();
                return true;
            }
            else {//当webview处于第一页面时,直接退出程序
//                System.exit(0);
                finish();
            }


        }
        return super.onKeyDown(keyCode, event);
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
