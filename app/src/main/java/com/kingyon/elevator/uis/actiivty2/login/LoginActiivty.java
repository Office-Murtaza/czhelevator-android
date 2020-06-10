package com.kingyon.elevator.uis.actiivty2.login;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.uis.activities.AgreementActivity;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.library.social.AuthorizeUser;
import com.kingyon.library.social.AuthorizeUtils;
import com.kingyon.paylibrary.ALiLoginUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.czh.myversiontwo.activity.ActivityUtils.setActivity;
import static com.czh.myversiontwo.utils.CodeType.REGISTER;
import static com.czh.myversiontwo.utils.CodeType.WX;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_LOGIN;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_USER_LOGIN;
import static com.kingyon.elevator.constants.Constants.LoginType.ALI;
import static com.kingyon.elevator.constants.Constants.LoginType.QQ;

/**
 * Created By Admin  on 2020/4/9
 * Email : 163235610@qq.com
 *
 * @Author:Mrczh Instructions:登录
 */
@Route(path = ACTIVITY_MAIN2_LOGIN)
public class LoginActiivty extends BaseActivity implements AuthorizeUtils.AuthorizeListener {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_dl)
    TextView tvDl;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.ll_sjh)
    LinearLayout llSjh;
    @BindView(R.id.img_yesyhxy)
    ImageView imgYesyhxy;
    @BindView(R.id.tv_djyhxy)
    TextView tvDjyhxy;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.tv_login_next)
    TextView tvLoginNext;
    @BindView(R.id.tv_login_user)
    TextView tvLoginUser;
    @BindView(R.id.tv_login_third)
    TextView tvLoginThird;
    @BindView(R.id.img_wx)
    ImageView imgWx;
    @BindView(R.id.img_qq)
    ImageView imgQq;
    @BindView(R.id.img_wb)
    ImageView imgWb;
    @BindView(R.id.ll_third)
    LinearLayout llThird;
    @BindView(R.id.ll_sf)
    LinearLayout llSf;
    @BindView(R.id.tv_yszc)
    TextView tvYszc;
    private AuthorizeUtils authorizeUtils;
    boolean istyxi = false;
    private String unique, avatar, nickName, loginType;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login2;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvDjyhxy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                tvYszc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                tvLoginNext.setClickable(false);
                etPhone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() < 11) {
                            tvLoginNext.setBackgroundResource(R.mipmap.btn_common_dislabled);
//                            tvLoginNext.clickable
                            tvLoginNext.setClickable(false);
                        } else {
                            tvLoginNext.setBackgroundResource(R.mipmap.btn_common_normal);
                            tvLoginNext.setClickable(true);
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        OrdinaryActivity.loginActiivty = this;
    }

    @OnClick({R.id.img_top_back, R.id.tv_dl, R.id.img_yesyhxy, R.id.tv_login_next,
            R.id.img_wx, R.id.img_qq, R.id.img_wb, R.id.tv_djyhxy, R.id.tv_login_user,R.id.tv_yszc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_dl:

                break;
            case R.id.img_yesyhxy:
                if (istyxi) {
                    istyxi = false;
                    imgYesyhxy.setImageResource(R.mipmap.ic_read_normal);
                } else {
                    istyxi = true;
                    imgYesyhxy.setImageResource(R.mipmap.ic_read_selected);
                }
                break;
            case R.id.tv_login_next:
                httpNext();
                break;
            case R.id.img_wx:
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
            case R.id.img_wb:
                ALiLoginUtils aLiLogin = new ALiLoginUtils(this) {
                    @Override
                    protected void getOpentid(String unique1) {
                        LogUtils.e(unique1);
                        unique = unique1;
                        avatar = "";
                        nickName = "";
                        loginType = ALI;
                        OrdinaryActivity.httpLogin(LoginActiivty.this,
                                "", "", ALI, unique1, "", ""
                                , llSf, tvLoginUser);
                    }
                };
                aLiLogin.authV2();
                break;
            case R.id.tv_djyhxy:
                AgreementActivity.start(this, "屏多多用户协议", Constants.AgreementType.USER_RULE.getValue());
                break;
            case R.id.tv_login_user:
                setActivity(ACTIVITY_MAIN2_USER_LOGIN);
                break;
            case R.id.tv_yszc:
                AgreementActivity.start(this, "隐私协议", Constants.AgreementType.SERVICE_TERMS.getValue());
                break;
            default:
        }
    }

    private void httpNext() {
        if (etPhone.getText().toString().isEmpty()) {
            ToastUtils.showToast(this, "请输入手机号", 1000);
        } else if (!istyxi) {
            ToastUtils.showToast(this, "请同意用户协议", 1000);
        } else {
            if (unique == null) {
                OrdinaryActivity.CodeActivity(this, REGISTER, etPhone.getText().toString(), unique, avatar, nickName, "1", loginType);
            } else {
                OrdinaryActivity.CodeActivity(this, REGISTER, etPhone.getText().toString(), unique, avatar, nickName, "2", loginType);
            }
        }
    }

    private void setThirdLoginEnabled(boolean enabled) {
        imgWx.setEnabled(enabled);
        imgQq.setEnabled(enabled);
        imgWb.setEnabled(enabled);
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
                                    hideProgress();
                                    LogUtils.e(Constants.LoginType.WX, user.getUsername(), user.getHeadimgurl(), user.getNickname());
                                    OrdinaryActivity.httpLogin(LoginActiivty.this,
                                            "", "", WX, user.getUsername(), user.getHeadimgurl(), user.getNickname()
                                            , llSf, tvLoginUser);
                                    unique = user.getUsername();
                                    avatar = user.getHeadimgurl();
                                    nickName = user.getNickname();
                                    loginType = WX;
                                    break;
                                case "TENCENT_QQ":
                                    hideProgress();
                                    LogUtils.e(QQ, user.getUsername(), user.getHeadimgurl(), user.getNickname());
                                    OrdinaryActivity.httpLogin(LoginActiivty.this,
                                            "", "", QQ, user.getUsername(), user.getHeadimgurl(), user.getNickname()
                                            , llSf, tvLoginUser);
                                    unique = user.getUsername();
                                    avatar = user.getHeadimgurl();
                                    nickName = user.getNickname();
                                    loginType = QQ;
                                    break;
                                case "XINLAN_WEIBO":
                                    hideProgress();
                                    LogUtils.e(Constants.LoginType.SINA, user.getUsername(), user.getHeadimgurl(), user.getNickname());
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

}
