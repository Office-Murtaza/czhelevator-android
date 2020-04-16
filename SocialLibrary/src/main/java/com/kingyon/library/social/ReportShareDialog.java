package com.kingyon.library.social;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class ReportShareDialog extends Dialog implements View.OnClickListener{
    protected Context mContext;
    View share_btn_cancel,tv_delete;
    protected ProgressDialog promotWaitBar;
    public ReportShareDialog(@NonNull Context context) {
        super(context, R.style.ShareDialog);
        this.mContext = context;
        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }
        promotWaitBar = new ProgressDialog(mContext);
        promotWaitBar.setMessage(getContext().getResources().getString(
                R.string.hold_on));
    }
    protected int getLayoutId() {
        return R.layout.dialog_reportshare;
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
        tv_delete = findViewById(R.id.tv_1);
        share_btn_cancel = findViewById(R.id.share_btn_cancel);

        tv_delete.setOnClickListener(this);
        share_btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_1) {
            ReportContentShareDialog reportContentShareDialog = new ReportContentShareDialog(mContext);
            reportContentShareDialog.show();
        } else if (id == R.id.share_btn_cancel) {
            dismiss();
        }


    }

}