package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/7/4.
 * Email：lc824767150@163.com
 */

public class TipDialog<T> extends Dialog {
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    protected T entity;
    protected OnOperatClickListener<T> onOperatClickListener;

    public TipDialog(Context context) {
        super(context, R.style.normal_dialog_small);
        setContentView(getLayoutRes());
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.92f);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public TipDialog(Context context, OnOperatClickListener<T> onOperatClickListener) {
        this(context);
        this.onOperatClickListener = onOperatClickListener;
    }

    public void setOnOperatClickListener(OnOperatClickListener<T> onOperatClickListener) {
        this.onOperatClickListener = onOperatClickListener;
    }

    protected int getLayoutRes() {
        return R.layout.dialog_tip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_ensure, R.id.img_close})
    public void onViewClicked(View view) {
        if (onOperatClickListener != null) {
            switch (view.getId()) {
                case R.id.tv_cancel:
                    onOperatClickListener.onCancelClick(entity);
                    break;
                case R.id.tv_ensure:
                    onOperatClickListener.onEnsureClick(entity);
                    break;
            }
        }
        dismiss();
    }

//    public void showDistanceSame(CharSequence tip) {
//        show(tip, null);
//        tvContent.setPadding(ScreenUtil.dp2px(16), ScreenUtil.dp2px(30), ScreenUtil.dp2px(16), ScreenUtil.dp2px(30));
//    }

    public void show(CharSequence tip) {
        show(tip, null);
    }

    public void show(CharSequence tip, String ensure, String cancel, T params) {
        show(tip, params);
        tvEnsure.setText(ensure);
        tvCancel.setText(cancel);
    }

    public void showEnsure(CharSequence tip) {
        show(tip, null);
        tvCancel.setVisibility(View.GONE);
        tvEnsure.setText("确定");
    }

    public void showEnsure(CharSequence tip, String ensure, T params) {
        show(tip, null);
        tvCancel.setVisibility(View.GONE);
        tvEnsure.setText(ensure);
    }

    public void showEnsureNoClose(CharSequence tip, String ensure, T params) {
        show(tip, null);
        tvCancel.setVisibility(View.GONE);
        tvEnsure.setText(ensure);
        imgClose.setVisibility(View.GONE);
        llContent.setPadding(0, ScreenUtil.dp2px(14), 0, 0);
    }


    public void showWechat(CharSequence tip, T wechatId) {
        show(tip, wechatId);
        tvCancel.setText("确定");
        tvCancel.setVisibility(View.VISIBLE);
        tvEnsure.setText("微信提醒");
    }

    public void show(CharSequence tip, T params) {
        entity = params;
        super.show();
        tvContent.setText(tip);
    }


    public interface OnOperatClickListener<K> {
        void onEnsureClick(K param);

        void onCancelClick(K param);
    }
}
