package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kingyon.elevator.R;

/**
 * @Created By Admin  on 2020/6/16
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class NotAttentionDialog  extends Dialog {
    Context context;
    String beFollowerAccount;
    TextView tv_not_attention,share_btn_cancel;
    OnClick onClick;

    public NotAttentionDialog(@NonNull Context context,OnClick onClick) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.context = context;
        this.onClick = onClick;
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
        return R.layout.dialog_not_attention;
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
        tv_not_attention = findViewById(R.id.tv_not_attention);
        share_btn_cancel = findViewById(R.id.share_btn_cancel);
        tv_not_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onclick();
                dismiss();
            }
        });
        share_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public interface OnClick{
        public void onclick();
    }
}
