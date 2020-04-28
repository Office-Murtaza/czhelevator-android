package com.kingyon.elevator.uis.actiivty2.login;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.RESETPASSWORD;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_FORGET_PASSWORD;

/**
 * Created By Admin  on 2020/4/10
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions: 忘记密码
 */
@Route(path = ACTIVITY_MAIN2_FORGET_PASSWORD)
public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_dl)
    TextView tvDl;
    @BindView(R.id.tv_code_dao)
    TextView tvCodeDao;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.img_remove)
    ImageView imgRemove;
    @BindView(R.id.ll_sjh)
    LinearLayout llSjh;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_login_next)
    TextView tvLoginNext;

    @Override
    public int getContentViewId() {
        return R.layout.activity_forgetpassord;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_code_dao, R.id.img_remove, R.id.tv_get_code, R.id.tv_login_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.img_remove:
                etPhone.setText("");
                break;
            case R.id.tv_get_code:
                OrdinaryActivity.CodeTextviewActivity(this,RESETPASSWORD,etPhone.getText().toString(),tvGetCode);
                break;
            case R.id.tv_login_next:
                OrdinaryActivity.httpResetPassword(this,etPhone.getText().toString(),edCode.getText().toString(),etPassword.getText().toString());
                break;
        }
    }
}
