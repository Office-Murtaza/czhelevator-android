package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kingyon.elevator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/4.
 * Email：lc824767150@163.com
 */

public class OrderIdentityDialog extends Dialog {
    @BindView(R.id.tv_info)
    TextView tvInfo;

    private OnIdentityClickListener onIdentityClickListener;

    public OrderIdentityDialog(@NonNull Context context) {
        super(context, R.style.normal_dialog_corner);
        setContentView(R.layout.dialog_order_identity);
    }

    public void setOnIdentityClickListener(OnIdentityClickListener onIdentityClickListener) {
        this.onIdentityClickListener = onIdentityClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.92);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void show(String s) {
        show();
        tvInfo.setText(s);
    }

    @OnClick({R.id.img_close, R.id.tv_cancel, R.id.tv_ensure})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_ensure && onIdentityClickListener != null) {
            onIdentityClickListener.onIdentityClick();
        }
        dismiss();
    }

    public interface OnIdentityClickListener {
        void onIdentityClick();
    }
}
