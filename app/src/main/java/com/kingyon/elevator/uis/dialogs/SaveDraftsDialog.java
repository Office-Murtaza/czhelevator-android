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
import com.kingyon.elevator.utils.utilstwo.IsSuccess;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/8/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class SaveDraftsDialog extends Dialog {
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_no_save)
    TextView tvNoSave;
    @BindView(R.id.share_btn_cancel)
    TextView shareBtnCancel;
    IsSuccess isSuccess;
    public SaveDraftsDialog(@NonNull Context context) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);

        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }
    }

    public void Clicked( IsSuccess isSuccess){
        this.isSuccess = isSuccess;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    protected int getLayoutId() {
        return R.layout.dialog_save_drafts;
    }

    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }

    @OnClick({R.id.tv_save, R.id.tv_no_save, R.id.share_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                isSuccess.isSuccess(true);
                break;
            case R.id.tv_no_save:
                isSuccess.isSuccess(false);
                break;
            case R.id.share_btn_cancel:
                dismiss();
                break;
        }
    }
}
