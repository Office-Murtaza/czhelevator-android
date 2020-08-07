package com.kingyon.elevator.uis.activities.password;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.login.LoginActiivty;
import com.kingyon.elevator.utils.CheckCodePresenter;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/12/24.
 * Email：lc824767150@163.com
 */

public class ResetPasswordActivity extends BaseSwipeBackActivity {
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_repeat)
    EditText etPasswordRepeat;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private CheckCodePresenter checkCodePresenter;

    @Override
    protected String getTitleText() {
        return "重置密码";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        checkCodePresenter = new CheckCodePresenter(this, tvCode);
        etPassword.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = etPassword.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > etPassword.getWidth() - etPassword.getPaddingRight() - drawable.getIntrinsicWidth()) {
                    if (etPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPassword.setSelected(false);
                    } else {
                        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        etPassword.setSelected(true);
                    }
                    etPassword.setSelection(etPassword.getText().length());
                }
                return false;
            }
        });
        etPasswordRepeat.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = etPasswordRepeat.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > etPasswordRepeat.getWidth() - etPasswordRepeat.getPaddingRight() - drawable.getIntrinsicWidth()) {
                    if (etPasswordRepeat.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        etPasswordRepeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPasswordRepeat.setSelected(false);
                    } else {
                        etPasswordRepeat.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        etPasswordRepeat.setSelected(true);
                    }
                    etPasswordRepeat.setSelection(etPasswordRepeat.getText().length());
                }
                return false;
            }
        });
    }

    @OnClick({R.id.tv_code, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                checkCodePresenter.getCode(CommonUtil.getEditText(etMobile), CheckCodePresenter.VerifyCodeType.RESETPASSWORD);
                break;
            case R.id.tv_login:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etMobile))) {
            showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etCode))) {
            showToast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etPassword))) {
            showToast("请输入密码");
            return;
        }
        if (CommonUtil.getEditText(etPassword).length() < 6) {
            showToast("新密码长度不足6位");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etPasswordRepeat))) {
            showToast("请再次输入密码");
            return;
        }
        if (!TextUtils.equals(CommonUtil.getEditText(etPasswordRepeat), CommonUtil.getEditText(etPassword))) {
            showToast("两次输入的密码不一致");
            return;
        }
        showProgressDialog(getString(R.string.wait),true);
        tvLogin.setEnabled(false);
        NetService.getInstance().resetPassword(CommonUtil.getEditText(etMobile), CommonUtil.getEditText(etCode)
                , CommonUtil.getEditText(etPassword))
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        tvLogin.setEnabled(true);
                        hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
                        tvLogin.setEnabled(true);
                        showToast("重置密码成功");
                        ActivityUtil.finishAllNotThis("ResetPasswordActivity");
                        startActivity(LoginActiivty.class);
                        finish();
                    }
                });
    }
}
