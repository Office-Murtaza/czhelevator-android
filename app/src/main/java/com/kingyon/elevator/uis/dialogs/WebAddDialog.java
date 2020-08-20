package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.utils.ToastUtils;

/**
 * @Created By Admin  on 2020/8/20
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class WebAddDialog extends Dialog {



    IsSrcSuccess isSrcSuccess;
    Context context;
    EditText etLj;
    EditText etText;
    TextView tvQx;
    TextView tvQr;
    public WebAddDialog(@NonNull Context context) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.context = context;
        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.CENTER);
        }
    }

    protected int getLayoutId() {
        return R.layout.dialog_web_edit;
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
        etLj = findViewById(R.id.et_lj);
        etText = findViewById(R.id.et_text);
        tvQx = findViewById(R.id.tv_cancel);
        tvQr = findViewById(R.id.tv_confirm);
        tvQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLj.getText().toString().isEmpty()) {
                    ToastUtils.showToast(context, "地址必填", 1000);
                } else {
                    isSrcSuccess.onSuccess(etLj.getText().toString(), etText.getText().toString());
                }
            }
        });
        tvQx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void OnGetConent(IsSrcSuccess isSrcSuccess) {
        this.isSrcSuccess = isSrcSuccess;
    }

    public interface IsSrcSuccess {
        void onSuccess(String e, String e1);
    }
}
