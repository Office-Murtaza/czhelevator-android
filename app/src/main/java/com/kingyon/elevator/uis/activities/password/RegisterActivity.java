package com.kingyon.elevator.uis.activities.password;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.LoginResultEntity;
import com.kingyon.elevator.entities.RegisterIdEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.AgreementActivity;
import com.kingyon.elevator.uis.activities.MainActivity;
import com.kingyon.elevator.uis.activities.salesman.SalesmanActivity;
import com.kingyon.elevator.utils.CheckCodePresenter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.JumpUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/4/13.
 * Email：lc824767150@163.com
 */

public class RegisterActivity extends BaseSwipeBackActivity {
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
    @BindView(R.id.img_agreement)
    ImageView imgAgreement;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private String way;
    private String openId;
    private String head;
    private String nickName;
    private CheckCodePresenter checkCodePresenter;

    @Override
    protected String getTitleText() {
        boolean perfection = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        way = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        openId = getIntent().getStringExtra(CommonUtil.KEY_VALUE_3);
        head = getIntent().getStringExtra(CommonUtil.KEY_VALUE_4);
        nickName = getIntent().getStringExtra(CommonUtil.KEY_VALUE_5);
        return perfection ? "绑定手机" : "注册";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_register;
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
        String agreementTip = getString(R.string.agreementTip);
        SpannableString agreementSpan = new SpannableString(agreementTip);
        int index1 = agreementTip.indexOf("《");
        int index2 = agreementTip.indexOf("》") + 1;
        int index3 = agreementTip.lastIndexOf("《");
        int index4 = agreementTip.lastIndexOf("》") + 1;
        agreementSpan.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(0xFF808080);       //设置文件颜色
                ds.setUnderlineText(false);      //设置下划线
            }

            @Override
            public void onClick(View widget) {
                AgreementActivity.start(RegisterActivity.this, "屏多多用户协议", Constants.AgreementType.USER_RULE.getValue());
            }
        }, index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreementSpan.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(0xFF808080);       //设置文件颜色
                ds.setUnderlineText(false);      //设置下划线
            }

            @Override
            public void onClick(View widget) {
                AgreementActivity.start(RegisterActivity.this, "屏多多使用须知", Constants.AgreementType.AD_TERMS.getValue());
            }
        }, index3, index4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreementSpan.setSpan(new AbsoluteSizeSpan(12, true), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreementSpan.setSpan(new AbsoluteSizeSpan(12, true), index3, index4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreementSpan.setSpan(new StyleSpan(Typeface.BOLD), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreementSpan.setSpan(new StyleSpan(Typeface.BOLD), index3, index4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(agreementSpan);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());  //很重要，点击无效就是由于没有设置这个引起
    }

    @OnClick({R.id.tv_code, R.id.ll_agreement, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                checkCodePresenter.getCode(CommonUtil.getEditText(etMobile), CheckCodePresenter.VerifyCodeType.REGISTER);
                break;
            case R.id.ll_agreement:
                imgAgreement.setSelected(!imgAgreement.isSelected());
                break;
            case R.id.tv_login:
                registerUser();
                break;
        }
    }

    private void registerUser() {
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
            showToast("密码长度不足6位");
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
        if (!imgAgreement.isSelected()) {
            showToast("请阅读并同意所有协议");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvLogin.setEnabled(false);
        NetService.getInstance().register(way, CommonUtil.getEditText(etMobile), CommonUtil.getEditText(etCode)
                , CommonUtil.getEditText(etPassword), openId, head, nickName)
                .compose(this.<LoginResultEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<LoginResultEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        tvLogin.setEnabled(true);
                        hideProgress();
                    }

                    @Override
                    public void onNext(LoginResultEntity loginResultEntity) {
                        tvLogin.setEnabled(true);
                        if (loginResultEntity == null || loginResultEntity.getUser() == null) {
                            throw new ResultException(9000, "空指针");
                        }
                        hideProgress();
                        DataSharedPreferences.saveLoginName(CommonUtil.getEditText(etMobile));
                        DataSharedPreferences.saveUserBean(loginResultEntity.getUser());
                        DataSharedPreferences.saveToken(loginResultEntity.getToken());
                        Net.getInstance().setToken(DataSharedPreferences.getToken());

                        ActivityUtil.finishAllNotPassword();
                        JumpUtils.getInstance().jumpToRoleMain(RegisterActivity.this, loginResultEntity.getUser());
                        EventBus.getDefault().post(new RegisterIdEntity());
                        ActivityUtil.finishAllLogin();
                        finish();
                    }
                });
    }
}