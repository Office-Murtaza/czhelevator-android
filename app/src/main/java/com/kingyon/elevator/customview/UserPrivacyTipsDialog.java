package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.interfaces.PrivacyTipsListener;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.HtmlFormUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/10/30
 * Time:15:24
 * author:songpeng
 * Email:1531603384@qq.com
 */
public class UserPrivacyTipsDialog extends Dialog {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.tv_i_konw)
    TextView tv_i_konw;
    @BindView(R.id.tv_no_agree)
    TextView tv_no_agree;
    String data;
    PrivacyTipsListener privacyTipsListener;


    public UserPrivacyTipsDialog(Context context, String data, PrivacyTipsListener privacyTipsListener) {
        super(context, R.style.MyDialog);
        this.data = data;
        this.privacyTipsListener = privacyTipsListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_privacy_tips_dialog);
        ButterKnife.bind(this);
        WebSettings webSettings = webview.getSettings();
        //允许webview对文件的操作
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        //用于js调用Android
        webSettings.setDefaultFontSize(14);
        webSettings.setJavaScriptEnabled(true);
        //设置编码方式
        webSettings.setDefaultTextEncodingName("utf-8");
        webview.setWebChromeClient(new WebChromeClient());
        String s = HtmlFormUtil.dealHtml(data);
        webview.loadDataWithBaseURL("about:blank", s, "text/html", "utf-8", null);
        tv_i_konw.setOnClickListener(v -> {
            privacyTipsListener.onAgree();
            DataSharedPreferences.saveBoolean(DataSharedPreferences.IS_SHOW_ALREADY_PRIVACY_DIALOG, true);
            DialogUtils.getInstance().hideUserPrivacyTipsDialog();
        });
        tv_no_agree.setOnClickListener(v -> {
            //用户未同意，会一直提示
            privacyTipsListener.onNoAgree();
            DataSharedPreferences.saveBoolean(DataSharedPreferences.IS_SHOW_ALREADY_PRIVACY_DIALOG, false);
            DialogUtils.getInstance().hideUserPrivacyTipsDialog();

        });
    }


    @Override
    public void dismiss() {
        super.dismiss();
        webview.destroy();
    }
}
