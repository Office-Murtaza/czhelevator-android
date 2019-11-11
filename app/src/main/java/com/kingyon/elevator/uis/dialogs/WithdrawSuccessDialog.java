package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.kingyon.elevator.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class WithdrawSuccessDialog extends Dialog {

    public WithdrawSuccessDialog(@NonNull Context context) {
        super(context, R.style.normal_dialog_small);
        setContentView(R.layout.dialog_withdraw_success);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.92f);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @OnClick({R.id.img_close, R.id.tv_ensure})
    public void onViewClicked(View view) {
        dismiss();
    }

}
