package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


import com.kingyon.elevator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/11/15.
 * Email：lc824767150@163.com
 */

public class WechatDialog extends Dialog {

    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;

    private OnWeChatOpenClickListener onWeChatOpenClickListener;

    public WechatDialog(@NonNull Context context) {
        super(context, R.style.normal_dialog_corner);
        setContentView(R.layout.dialog_wechat);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.8);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setOnWeChatOpenClickListener(OnWeChatOpenClickListener onWeChatOpenClickListener) {
        this.onWeChatOpenClickListener = onWeChatOpenClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public void show(String wechat) {
        super.show();
        String tip = String.format("微信公众号%s已复制到剪贴板，请去微信搜索并关注，然后通过公众号免费制作。", wechat);
        SpannableString spannableString = new SpannableString(tip);
        spannableString.setSpan(new ForegroundColorSpan(0xFFF73E31), tip.indexOf(wechat), wechat.length() + wechat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new BackgroundColorSpan(0xFFF3F3F3), tip.indexOf(wechat), wechat.length() + wechat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInfo.setText(spannableString);
    }

    @OnClick({R.id.img_close, R.id.tv_wechat})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_wechat) {
            if (onWeChatOpenClickListener != null) {
                onWeChatOpenClickListener.onWeChatClick();
            }
        }
        dismiss();
    }

    public interface OnWeChatOpenClickListener {
        void onWeChatClick();
    }
}
