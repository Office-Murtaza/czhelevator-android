package com.kingyon.elevator.uis.activities.password;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.LoginResultEntity;
import com.kingyon.elevator.entities.RegisterIdEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.AgreementActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.JumpUtils;
import com.kingyon.library.social.AuthorizeUser;
import com.kingyon.library.social.AuthorizeUtils;
import com.leo.afbaselibrary.nets.callbacks.AbsAPICallback;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by GongLi on 2018/4/13.
 * Email：lc824767150@163.com
 */

public class LoginActivity extends BaseSwipeBackActivity implements AuthorizeUtils.AuthorizeListener {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.img_wx)
    ImageView imgWx;
    @BindView(R.id.img_qq)
    ImageView imgQq;
    @BindView(R.id.img_sina)
    ImageView imgSina;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    private AuthorizeUtils authorizeUtils;
    private String oldUserRole;

    @Override
    protected String getTitleText() {
        oldUserRole = AppContent.getInstance().getMyUserRole();
        if (TextUtils.isEmpty(oldUserRole)) {
            oldUserRole = Constants.RoleType.NORMAL;
        }
        return "登录";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DataSharedPreferences.clearLoginInfo();
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
    }

    @OnClick({R.id.tv_reset, R.id.tv_login, R.id.tv_register, R.id.img_wx, R.id.img_qq, R.id.img_sina, R.id.ll_agreement, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reset:
                startActivity(ResetPasswordActivity.class);
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_register:
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_2, Constants.LoginType.NORMAL);
                startActivity(RegisterActivity.class, bundle);
                break;
            case R.id.img_wx:
//                showToast("暂未开通");
                if (authorizeUtils == null) {
                    authorizeUtils = new AuthorizeUtils(this, null);
                    authorizeUtils.setAuthorizeListener(this);
                }
                setThirdLoginEnabled(false);
                showProgressDialog(getString(R.string.wait));
                authorizeUtils.authWechat();
                break;
            case R.id.img_qq:
                if (authorizeUtils == null) {
                    authorizeUtils = new AuthorizeUtils(this, null);
                    authorizeUtils.setAuthorizeListener(this);
                }
                setThirdLoginEnabled(false);
                showProgressDialog(getString(R.string.wait));
                authorizeUtils.authQQ();
                break;
            case R.id.img_sina:
                showToast("暂未开通");
//                if (authorizeUtils == null) {
//                    authorizeUtils = new AuthorizeUtils(this, null);
//                    authorizeUtils.setAuthorizeListener(this);
//                }
//                setThirdLoginEnabled(false);
//                showProgressDialog(getString(R.string.wait));
//                authorizeUtils.authSina();
                break;
            case R.id.tv_agreement:
            case R.id.ll_agreement:
                AgreementActivity.start(this, "屏多多用户协议", Constants.AgreementType.USER_RULE.getValue());
                break;
        }
    }

    private void login() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            showToast("请输入手机号码");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etPassword))) {
            showToast("请输入密码");
            return;
        }
        requestLogin(Constants.LoginType.NORMAL, null, null, null);
    }

    private void requestLogin(final String way, final String openId, final String head, final String nickName) {
        showProgressDialog(getString(R.string.wait));
        tvLogin.setEnabled(false);
        NetService.getInstance().login(way, openId, CommonUtil.getEditText(etName), CommonUtil.getEditText(etPassword))
                .compose(this.<LoginResultEntity>bindLifeCycle())
                .subscribe(new AbsAPICallback<LoginResultEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        showToast(ex.getDisplayMessage());
                        tvLogin.setEnabled(true);
                    }

                    @Override
                    public void onNext(LoginResultEntity loginResultEntity) {
                        tvLogin.setEnabled(true);
                        if (loginResultEntity == null) {
                            throw new ResultException(9000, "空指针");
                        }
                        if (loginResultEntity.isNeedFill()) {
                            hideProgress();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
                            bundle.putString(CommonUtil.KEY_VALUE_2, way);
                            bundle.putString(CommonUtil.KEY_VALUE_3, openId);
                            bundle.putString(CommonUtil.KEY_VALUE_4, head);
                            bundle.putString(CommonUtil.KEY_VALUE_5, nickName);
                            startActivity(RegisterActivity.class, bundle);
                        } else {
                            if (loginResultEntity.getUser() == null) {
                                throw new ResultException(9000, "空指针");
                            }
                            UserEntity userEntity = loginResultEntity.getUser();
                            DataSharedPreferences.saveLoginName(CommonUtil.getEditText(etName));
                            DataSharedPreferences.saveUserBean(userEntity);
                            DataSharedPreferences.saveToken(loginResultEntity.getToken());
                            Net.getInstance().setToken(DataSharedPreferences.getToken());
                            hideProgress();

                            boolean needFinish = !TextUtils.equals(oldUserRole, userEntity.getRole());
                            if (needFinish || !getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false)) {
                                ActivityUtil.finishAllNotLogin();
                                JumpUtils.getInstance().jumpToRoleMain(LoginActivity.this, userEntity);
                            }
                            EventBus.getDefault().post(new RegisterIdEntity());
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            String loginName = DataSharedPreferences.getLoginName();
            if (!TextUtils.isEmpty(loginName)) {
                etName.setText(loginName);
                etName.setSelection(loginName.length());
            }
        }
    }

    @Override
    public void onComplete(final AuthorizeUser user) {
        if (user != null && user.getUsername() != null) {
            Observable.just(user)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomApiCallback<AuthorizeUser>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            setThirdLoginEnabled(true);
                        }

                        @Override
                        public void onNext(AuthorizeUser authorizeUser) {
                            switch (authorizeUser.getPlat_form()) {
                                case "TENCENT_WEI_XIN":
                                    requestLogin(Constants.LoginType.WX, user.getUsername(), user.getHeadimgurl(), user.getNickname());
                                    break;
                                case "TENCENT_QQ":
                                    requestLogin(Constants.LoginType.QQ, user.getUsername(), user.getHeadimgurl(), user.getNickname());
                                    break;
                                case "XINLAN_WEIBO":
                                    requestLogin(Constants.LoginType.SINA, user.getUsername(), user.getHeadimgurl(), user.getNickname());
                                    break;
                            }
                            setThirdLoginEnabled(true);
                        }
                    });
        } else {
            onError();
        }
    }

    @Override
    public void onError() {
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<Integer>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast("授权失败");
                        hideProgress();
                        setThirdLoginEnabled(true);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        showToast("授权失败");
                        hideProgress();
                        setThirdLoginEnabled(true);
                    }
                });
    }

    private void setThirdLoginEnabled(boolean enabled) {
        imgWx.setEnabled(enabled);
        imgQq.setEnabled(enabled);
        imgSina.setEnabled(enabled);
    }
}