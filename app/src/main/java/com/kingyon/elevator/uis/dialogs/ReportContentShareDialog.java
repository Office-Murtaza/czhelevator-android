package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class ReportContentShareDialog extends Dialog implements View.OnClickListener{
    protected Context mContext;
    protected int objId;
    protected String reportType;
    View share_btn_cancel;
     TextView tv_delete;
     TextView tv_delete1;
     TextView tv_delete2;
     TextView tv_delete3;
     TextView tv_delete4;
    protected ProgressDialog promotWaitBar;
    public ReportContentShareDialog(@NonNull Context context, int objId, String reportType) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.mContext = context;
        this. objId =  objId;
        this. reportType =  reportType;
        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }
        promotWaitBar = new ProgressDialog(mContext);
        promotWaitBar.setMessage(getContext().getResources().getString(
                com.kingyon.library.social.R.string.hold_on));
    }
    protected int getLayoutId() {
        return com.kingyon.library.social.R.layout.dialog_content_reportshare;
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
        tv_delete = findViewById(com.kingyon.library.social.R.id.tv_1);
        tv_delete1 = findViewById(com.kingyon.library.social.R.id.tv_2);
        tv_delete2 = findViewById(com.kingyon.library.social.R.id.tv_3);
        tv_delete3 = findViewById(com.kingyon.library.social.R.id.tv_4);
        tv_delete4 = findViewById(com.kingyon.library.social.R.id.tv_5);
        share_btn_cancel = findViewById(com.kingyon.library.social.R.id.share_btn_cancel);

        tv_delete.setOnClickListener(this);
        tv_delete1.setOnClickListener(this);
        tv_delete2.setOnClickListener(this);
        tv_delete3.setOnClickListener(this);
        tv_delete4.setOnClickListener(this);
        share_btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.kingyon.library.social.R.id.tv_1) {
            ConentUtils.httpReport(mContext,objId,reportType,tv_delete.getText().toString());
            dismiss();
        } else if (id == com.kingyon.library.social.R.id.tv_2) {
            ConentUtils.httpReport(mContext,objId,reportType,tv_delete1.getText().toString());
            dismiss();
        }else if (id == com.kingyon.library.social.R.id.tv_3) {
            ConentUtils.httpReport(mContext,objId,reportType,tv_delete2.getText().toString());
            dismiss();
        }else if (id == com.kingyon.library.social.R.id.tv_4) {
            ConentUtils.httpReport(mContext,objId,reportType,tv_delete3.getText().toString());
            dismiss();
        }else if (id == com.kingyon.library.social.R.id.tv_5) {
            ConentUtils.httpReport(mContext,objId,reportType,tv_delete4.getText().toString());
            dismiss();
        }else if (id == com.kingyon.library.social.R.id.share_btn_cancel) {
            dismiss();
        }


    }

}