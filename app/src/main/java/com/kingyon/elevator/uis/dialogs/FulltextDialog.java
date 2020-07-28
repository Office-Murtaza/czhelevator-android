package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.kingyon.elevator.R;

/**
 * @Created By Admin  on 2020/7/28
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class FulltextDialog extends Dialog {

    public FulltextDialog(@NonNull Context context) {
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
        return R.layout.dialog_fulltext;
    }

    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }
}
