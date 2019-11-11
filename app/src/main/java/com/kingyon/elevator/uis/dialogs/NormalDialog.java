package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;

import butterknife.ButterKnife;

/**
 * Created by arvin on 2016/3/8 10:19
 */
public abstract class NormalDialog extends Dialog implements View.OnClickListener {

    protected View backGroundView;
    protected TextView tvCancel;
    protected TextView tvEnsure;

    public NormalDialog(Context context) {
        super(context, R.style.normal_dialog_corner);
        setContentView(getlayout());
        addContent();
        ButterKnife.bind(this);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.92);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    protected int getlayout() {
        return R.layout.dialog_normal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backGroundView = findViewById(R.id.root);
        tvCancel = findViewById(R.id.tv_cancel);
        tvEnsure = findViewById(R.id.tv_ensure);
        tvCancel.setOnClickListener(this);
        tvEnsure.setOnClickListener(this);
        initViews();
    }

    protected void initViews() {
    }

    protected void addContent() {
        FrameLayout layouContent = findViewById(R.id.layout_content);
        layouContent.addView(LayoutInflater.from(getContext()).inflate(getContentView(), layouContent, false));
    }

    protected abstract int getContentView();

    public void setEnsureText(String ensureText) {
        if (!TextUtils.isEmpty(ensureText)) {
            tvEnsure.setText(ensureText);
        }
    }

    public void setCancelText(String cancelText) {
        if (!TextUtils.isEmpty(cancelText)) {
            tvCancel.setText(cancelText);
        }
    }

    protected abstract void onEnsure();

    protected abstract void onCancel();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                onCancel();
                break;
            case R.id.tv_ensure:
                onEnsure();
                break;
        }
    }
}
