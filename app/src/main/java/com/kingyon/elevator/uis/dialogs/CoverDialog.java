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

import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_COVER;

/**
 * @Created By Admin  on 2020/7/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CoverDialog extends Dialog {
    @BindView(R.id.tv_cover)
    TextView tvCover;
    @BindView(R.id.share_btn_cancel)
    TextView shareBtnCancel;

    public CoverDialog(@NonNull Context context) {
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

    protected int getLayoutId() {
        return R.layout.dialog_cover;
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

    @OnClick({R.id.tv_cover, R.id.share_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cover:
                ActivityUtils.setActivity(ACTIVITY_COVER);
                dismiss();
                break;
            case R.id.share_btn_cancel:
                dismiss();
                break;
        }
    }
}
