package com.kingyon.elevator.customview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.interfaces.InputPayPwdListener;
import com.kingyon.elevator.interfaces.PayPasswordListener;
import com.kingyon.elevator.uis.activities.cooperation.CooperationWithdrawActivity;
import com.kingyon.elevator.utils.KeyBoardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By SongPeng  on 2019/11/28
 * Email : 1531603384@qq.com
 * 输入密码对话框进行验证提现
 */
public class InputPayPwdToCashDailog extends MyBaseBottomDialog {

    @BindView(R.id.tv_forget_password)
    TextView tv_forget_password;
    @BindView(R.id.pay_password_input_view)
    PayPasswordEditView pay_password_input_view;
    private Context context;


    private InputPayPwdListener inputPayPwdListener;

    public InputPayPwdToCashDailog(@NonNull Context context, InputPayPwdListener inputPayPwdListener) {
        super(context);
        this.context = context;
        this.inputPayPwdListener = inputPayPwdListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_input_pay_pwd_dialog_layout);
        ButterKnife.bind(this);
        pay_password_input_view.setPayPasswordListener(pwd -> {
                   KeyboardUtils.hideSoftInput(pay_password_input_view.getEt_input_password());
//                    KeyBoardUtils.closeKeybord(pay_password_input_view.getEt_input_password(),(Activity) context);
                    inputPayPwdListener.userInputPassWord(pwd);
                }
        );
        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //忘记支付密码
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }
}