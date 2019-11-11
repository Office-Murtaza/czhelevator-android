package com.kingyon.library.social;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareDialog extends Dialog implements OnClickListener,
        PlatformActionListener {

    protected Context mContext;
    protected ShareParamsProvider paramsProvider;
    protected View shareQQ, shareMore, shareQzone, shareWechat, shareWechatMoments,
            shareSina, shareCancel;

    protected ProgressDialog promotWaitBar;
    private final static int COMPLETE = 0, CANCEL = 1, ERROR = 2;

    public ShareDialog(Context context, ShareParamsProvider paramsProvider) {
        super(context, R.style.ShareDialog);
        this.mContext = context;
        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }
        this.paramsProvider = paramsProvider;
        promotWaitBar = new ProgressDialog(mContext);
        promotWaitBar.setMessage(getContext().getResources().getString(
                R.string.hold_on));
    }

    protected int getLayoutId() {
        return R.layout.dialog_share;
    }

    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        shareQQ = findViewById(R.id.share_qq);
        shareQzone = findViewById(R.id.share_qzone);
        shareWechat = findViewById(R.id.share_wechat);
        shareWechatMoments = findViewById(R.id.share_wechatmoments);
        shareSina = findViewById(R.id.share_sinaweibo);
        shareCancel = findViewById(R.id.share_btn_cancel);
        shareMore = findViewById(R.id.share_more);

        shareQQ.setOnClickListener(this);
        shareQzone.setOnClickListener(this);
        shareWechat.setOnClickListener(this);
        shareWechatMoments.setOnClickListener(this);
        shareSina.setOnClickListener(this);
        shareCancel.setOnClickListener(this);
        shareMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (paramsProvider == null || v == shareCancel) {
//			Toast.makeText(mContext, "params provide is null",
//					Toast.LENGTH_SHORT).show();t, "params provide is null",
//					Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }
        jumpShare(v);
        dismiss();
//		promotWaitBar.show();
    }

    protected void jumpShare(View v) {
        Platform plat;
        if (v == shareQQ) {
            plat = ShareSDK.getPlatform(QQ.NAME);
            plat.setPlatformActionListener(this);
            plat.share(paramsProvider.getParamsQQ());
        }
//        else if (v == shareQzone) {
//            plat = ShareSDK.getPlatform(QZone.NAME);
//            plat.setPlatformActionListener(this);
//            plat.share(paramsProvider.getParamsQQZone());
//        }
        else if (v == shareWechat) {
            plat = ShareSDK.getPlatform(Wechat.NAME);
            plat.setPlatformActionListener(this);
            plat.share(paramsProvider.getParamsWeChat());
        } else if (v == shareWechatMoments) {
            plat = ShareSDK.getPlatform(WechatMoments.NAME);
            plat.setPlatformActionListener(this);
            plat.share(paramsProvider.getParamsWeChatMoments());
        } else if (v == shareSina) {
            plat = ShareSDK.getPlatform(SinaWeibo.NAME);
            plat.setPlatformActionListener(this);
            plat.share(paramsProvider.getParamsSina());
        } else if (v == shareMore) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, paramsProvider.getParamsMore());
            sendIntent.setType("text/plain");
            mContext.startActivity(Intent.createChooser(sendIntent, "分享"));
        }
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        shareHandler.sendEmptyMessage(CANCEL);
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        shareHandler.sendEmptyMessage(COMPLETE);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        shareHandler.sendEmptyMessage(ERROR);
        Log.e("Dream", arg2.toString());
    }

    @SuppressLint("HandlerLeak")
    protected Handler shareHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            promotWaitBar.dismiss();
            switch (msg.what) {
                case COMPLETE:
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.share_complete), Toast.LENGTH_SHORT).show();
                    break;
                case CANCEL:
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.share_cancel), Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.share_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public interface ShareParamsProvider {

        ShareEntity getShareEntity();

        ShareParams getParamsQQ();

//        ShareParams getParamsQQZone();

        ShareParams getParamsWeChat();

        ShareParams getParamsWeChatMoments();

        ShareParams getParamsWeChatImages();

        ShareParams getParamsSina();

        String getParamsMore();
    }

    public void setParamsProvider(ShareParamsProvider paramsProvider) {
        this.paramsProvider = paramsProvider;
    }

}
