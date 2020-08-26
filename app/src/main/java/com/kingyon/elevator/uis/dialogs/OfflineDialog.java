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
import com.kingyon.elevator.view.IsSrcSuccess;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/8/24
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OfflineDialog extends Dialog {

    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.confirm)
    TextView confirm;
    IsSuccess isSuccess;
    public OfflineDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.dialog_offline);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cancel, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                isSuccess.isSuccess(true);
                break;
        }
    }
    public void onSuccessful(IsSuccess isSuccess){
       this.isSuccess = isSuccess;
    }
}
