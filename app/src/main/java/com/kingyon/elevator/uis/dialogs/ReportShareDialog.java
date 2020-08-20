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

import com.leo.afbaselibrary.uis.activities.BaseActivity;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:他人举报
 */
public class ReportShareDialog extends Dialog implements View.OnClickListener{
    protected Context mContext;
    protected int objId;
    protected String reportType;
    protected String otherUserAccount;
    View share_btn_cancel,tv_delete;
    protected ProgressDialog promotWaitBar;
    public ReportShareDialog(@NonNull Context context, int objId, String reportType, String otherUserAccount) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.mContext = context;
        this.objId = objId;
        this.reportType = reportType;
        this.otherUserAccount = otherUserAccount;
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
        return com.kingyon.library.social.R.layout.dialog_reportshare;
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
        share_btn_cancel = findViewById(com.kingyon.library.social.R.id.share_btn_cancel);

        tv_delete.setOnClickListener(this);
        share_btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.kingyon.library.social.R.id.tv_1) {
            if (reportType.equals("USER")){
                UsersReportDialog usersReportDialog = new UsersReportDialog(mContext,otherUserAccount);
                usersReportDialog.show();
            }else {
                ReportContentShareDialog reportContentShareDialog = new ReportContentShareDialog(mContext, objId, reportType);
                reportContentShareDialog.show();
            }
            dismiss();
        } else if (id == com.kingyon.library.social.R.id.share_btn_cancel) {
            dismiss();
        }


    }

}