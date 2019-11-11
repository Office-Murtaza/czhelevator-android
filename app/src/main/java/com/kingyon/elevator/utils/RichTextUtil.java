package com.kingyon.elevator.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kingyon.elevator.nets.CustomApiCallback;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by GongLi on 2017/11/3.
 * Email：lc824767150@163.com
 */

public class RichTextUtil {

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public static void setRichText(Context context, final NestedScrollView scrollView, final WebView wv, String content, OnWebImageClickListener onWebImageClickListener) {
        if (content == null) {
            content = "";
        }
        final WebSettings settings = wv.getSettings();
        settings.setAllowFileAccess(false);
        settings.setDomStorageEnabled(false);
        settings.setGeolocationEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultFontSize(14);
        wv.removeJavascriptInterface("imagelistner");
        wv.addJavascriptInterface(new JavascriptInterface(onWebImageClickListener), "imagelistner");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        wv.setBackgroundColor(0x00000000); // 设置背景色
        wv.setWebViewClient(new ImageClickWebView());
//        if (wv.getBackground() != null) {
//            wv.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
//        }
//        String s = HtmlFormUtil.formatHtml(content);

        ViewGroup.LayoutParams params = wv.getLayoutParams();
        params.height = 0;
        wv.setLayoutParams(params);

        String s = HtmlFormUtil.dealHtml(content);
        wv.loadDataWithBaseURL("about:blank", s, "text/html", "utf-8", null);

        ViewGroup.LayoutParams params2 = wv.getLayoutParams();
        params2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wv.setLayoutParams(params2);

        scrollView.requestLayout();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public static void setRichText(Context context, final WebView wv, String content, OnWebImageClickListener onWebImageClickListener) {
        if (content == null) {
            content = "";
        }
        final WebSettings settings = wv.getSettings();
        settings.setAllowFileAccess(false);
        settings.setDomStorageEnabled(false);
        settings.setGeolocationEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultFontSize(14);
        wv.removeJavascriptInterface("imagelistner");
        wv.addJavascriptInterface(new JavascriptInterface(onWebImageClickListener), "imagelistner");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        wv.setBackgroundColor(0x00000000); // 设置背景色
        wv.setWebViewClient(new ImageClickWebView());
//        if (wv.getBackground() != null) {
//            wv.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
//        }
//        String s = HtmlFormUtil.formatHtml(content);

        ViewGroup.LayoutParams params = wv.getLayoutParams();
        params.height = 0;
        wv.setLayoutParams(params);

        String s = HtmlFormUtil.dealHtml(content);
        wv.loadDataWithBaseURL("about:blank", s, "text/html", "utf-8", null);

        ViewGroup.LayoutParams params2 = wv.getLayoutParams();
        params2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wv.setLayoutParams(params2);
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    public static void setRichText(Context context, final WebView wv, String content, String color, OnWebImageClickListener onWebImageClickListener) {
        if (content == null) {
            content = "";
        }
        final WebSettings settings = wv.getSettings();
        settings.setAllowFileAccess(false);
        settings.setDomStorageEnabled(false);
        settings.setGeolocationEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultFontSize(14);
        wv.removeJavascriptInterface("imagelistner");
        wv.addJavascriptInterface(new JavascriptInterface(onWebImageClickListener), "imagelistner");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        wv.setBackgroundColor(0x00000000); // 设置背景色
        wv.setWebViewClient(new ImageClickWebView());
//        if (wv.getBackground() != null) {
//            wv.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
//        }
//        String s = HtmlFormUtil.formatHtml(content);

        ViewGroup.LayoutParams params = wv.getLayoutParams();
        params.height = 0;
        wv.setLayoutParams(params);

        String s = HtmlFormUtil.dealHtml(content, color);
        wv.loadDataWithBaseURL("about:blank", s, "text/html", "utf-8", null);

        ViewGroup.LayoutParams params2 = wv.getLayoutParams();
        params2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wv.setLayoutParams(params2);
    }

    public static void setRichText(WebView wv, String url) {
        final WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setAllowFileAccess(false);
        settings.setDomStorageEnabled(false);
        settings.setGeolocationEnabled(false);
        settings.setDefaultFontSize(14);
        wv.setBackgroundColor(0x00000000); // 设置背景色
//        if (wv.getBackground() != null) {
//            wv.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
//        }
        wv.loadUrl(url);
    }

    private static class ImageClickWebView extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        window.imagelistner.openImage(this.src);  " +
                    "    }  " +
                    "}" +
                    "})()");
        }
    }

    // js通信接口
    private static class JavascriptInterface {

        private OnWebImageClickListener onWebImageClickListener;

        public JavascriptInterface(OnWebImageClickListener onWebImageClickListener) {
            this.onWebImageClickListener = onWebImageClickListener;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            if (onWebImageClickListener != null) {
                Observable.just(img).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (ex != null) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        if (onWebImageClickListener != null) {
                            onWebImageClickListener.onWebImageClick(s);
                        }
                    }
                });
            }
        }
    }

    public interface OnWebImageClickListener {
        void onWebImageClick(String url);
    }
}
