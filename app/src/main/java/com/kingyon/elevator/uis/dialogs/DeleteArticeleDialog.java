package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.util.List;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:自己删除
 */
public class DeleteArticeleDialog extends Dialog implements View.OnClickListener{
    protected BaseActivity mContext;
    View share_btn_cancel,tv_delete;
    protected ProgressDialog promotWaitBar;
    int objId;
    RecyclerView.Adapter attentionAdapter;
    String type;
    int position;
    List<QueryRecommendEntity> conentEntity;
    List<CommentListEntity> conentEntity1;
    /**
     * type 1 首页条目删除  2 内容详情删除  3 评论条目删除
     *
     * */
    IsSuccess isSuccess;
    public DeleteArticeleDialog(@NonNull BaseActivity context ) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.mContext = context;
        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }


    }


    protected int getLayoutId() {
        return com.kingyon.library.social.R.layout.dialog_deldtehare;
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
        tv_delete = findViewById(com.kingyon.library.social.R.id.tv_delete);
        share_btn_cancel = findViewById(com.kingyon.library.social.R.id.share_btn_cancel);

        tv_delete.setOnClickListener(this);
        share_btn_cancel.setOnClickListener(this);
    }

    public void IsSuccess(IsSuccess isSuccess){
        this.isSuccess = isSuccess;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.kingyon.library.social.R.id.tv_delete) {
//            删除

            ConentUtils.httpDelcommen(mContext,objId,position,attentionAdapter,conentEntity1);

            dismiss();
        } else if (id == com.kingyon.library.social.R.id.share_btn_cancel) {

        }
        dismiss();
    }



}
