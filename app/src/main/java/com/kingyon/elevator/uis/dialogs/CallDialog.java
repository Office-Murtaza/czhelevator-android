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
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/7/10
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CallDialog extends Dialog {

    @BindView(R.id.tv_call)
    TextView tvCall;
    @BindView(R.id.share_btn_cancel)
    TextView shareBtnCancel;
    Context context;

    public CallDialog(@NonNull Context context) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.context = context;
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
        return R.layout.dialog_call;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }

    @OnClick({R.id.tv_call, R.id.share_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_call:
                try {
                    AFUtil.toCall(context, context.getString(R.string.service_phone));
                } catch (Exception e) {
                    ToastUtils.showToast(context, "当前手机不允许拨打电话，请换手机操作", 1000);
                }
                break;
            case R.id.share_btn_cancel:
                dismiss();
                break;
        }
    }

}
