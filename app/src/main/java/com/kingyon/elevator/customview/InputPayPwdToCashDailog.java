package com.kingyon.elevator.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.interfaces.InputPayPwdListener;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.czh.myversiontwo.utils.CodeType.KEYBOARD_SETTING;

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
    @BindView(R.id.tv_forget_fingerprint)
    TextView tvForgetFingerprint;
    private Context context;
    String type;

    private InputPayPwdListener inputPayPwdListener;

    public InputPayPwdToCashDailog(@NonNull Context context,String type, InputPayPwdListener inputPayPwdListener) {
        super(context);
        this.context = context;
        this.inputPayPwdListener = inputPayPwdListener;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_input_pay_pwd_dialog_layout);
        ButterKnife.bind(this);
        if (type.equals(KEYBOARD_SETTING)){
            tvForgetFingerprint.setVisibility(View.GONE);
        }
        pay_password_input_view.setPayPasswordListener(pwd -> {
                    KeyboardUtils.hideSoftInput(pay_password_input_view.getEt_input_password());
                    inputPayPwdListener.userInputPassWord(pwd);
                }
        );
        tv_forget_password.setOnClickListener(v -> {
            //忘记支付密码
            MyActivityUtils.goFragmentContainerActivity(context, FragmentConstants.CheckPayVerCodeFragment);
            KeyboardUtils.hideSoftInput(pay_password_input_view.getEt_input_password());
            DialogUtils.getInstance().hideInputPayPwdToCashDailog();
        });
        tvForgetFingerprint.setOnClickListener(v -> {
            inputPayPwdListener.userInputPassWord("susser");
        });

        pay_password_input_view.postDelayed(() -> KeyboardUtils.showSoftInput(pay_password_input_view.getEt_input_password()), 200);
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        context = null;
    }
}
