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
import com.kingyon.elevator.entities.ToPlanTab;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/2/1.
 * Email：lc824767150@163.com
 */

public class CellAdSuccessDialog extends Dialog {
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;

    private String type;

    public CellAdSuccessDialog(@NonNull Context context) {
        super(context, R.style.normal_dialog_small);
        setContentView(R.layout.dialog_cell_add_success);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.84f);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public void show(String type, CharSequence tip, String ensure, String cancel) {
        this.type = type;
        show(tip);
        tvEnsure.setText(ensure);
        tvCancel.setText(cancel);
    }

    public void show(CharSequence tip) {
        super.show();
        tvContent.setText(tip);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_ensure})
    public void onViewClicked(View view) {
        dismiss();
        if (view.getId() == R.id.tv_ensure) {
            EventBus.getDefault().post(new ToPlanTab(type));
        }
    }
}
