package com.kingyon.elevator.uis.actiivty2.user;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.login.LoginActiivty;
import com.kingyon.elevator.uis.activities.password.ModifyPhoneFirstActivity;
import com.kingyon.elevator.uis.dialogs.BindingDialog;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.library.social.AuthorizeUser;
import com.kingyon.library.social.AuthorizeUtils;
import com.kingyon.paylibrary.ALiLoginUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.czh.myversiontwo.utils.CodeType.WX;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ACCOUNT_BINDING;
import static com.kingyon.elevator.constants.Constants.LoginType.ALI;
import static com.kingyon.elevator.constants.Constants.LoginType.QQ;

/**
 * @Created By Admin  on 2020/6/3
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:绑定第三方账户
 */
@Route(path = ACTIVITY_ACCOUNT_BINDING)
public class AccountBindingActivity extends BaseActivity implements AuthorizeUtils.AuthorizeListener{

    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.ll_qq)
    LinearLayout llQq;
    UserEntity usernewEntity;
    private AuthorizeUtils authorizeUtils;

    @Override
    public int getContentViewId() {
        return R.layout.activity_account_binding;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("账户绑定");
        httpData();

    }

    private void httpData() {
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().getCheckBind3Rd()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                    }
                    @Override
                    public void onNext(UserEntity userEntity) {
                        hideProgress();
                        usernewEntity = userEntity;
                        tvPhone.setText(AccountNumUtils.hidePhoneNum(userEntity.getPhone()));
                        if (userEntity.isBindAli()){
                            tvPay.setText("已绑定");
                            tvPay.setTextColor(Color.parseColor("#ABABAB"));
                        }else {
                            tvPay.setText("未绑定");
                            tvPay.setTextColor(Color.parseColor("#FF1330"));
                        }
                        if (userEntity.isBindQq()){
                            tvQq.setText("已绑定");
                            tvQq.setTextColor(Color.parseColor("#ABABAB"));
                        }else {
                            tvQq.setText("未绑定");
                            tvQq.setTextColor(Color.parseColor("#FF1330"));

                        }
                        if (userEntity.isBindWx()){
                            tvWx.setText("已绑定");
                            tvWx.setTextColor(Color.parseColor("#ABABAB"));
                        }else {
                            tvWx.setText("未绑定");
                            tvWx.setTextColor(Color.parseColor("#FF1330"));

                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.ll_phone, R.id.ll_wx, R.id.ll_pay, R.id.ll_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.ll_phone:
                /*电话*/
                BindingDialog dialog1 = new BindingDialog(this);
                dialog1.show();
                dialog1.setDialogOnClick(new BindingDialog.DialogOnClick() {
                    @Override
                    public void onClick() {
                        dialog1.dismiss();
                        UserEntity userBean = DataSharedPreferences.getUserBean();
                        Bundle bundle = new Bundle();
                        if (userBean != null && !TextUtils.isEmpty(userBean.getPhone())) {
                            bundle.putString(CommonUtil.KEY_VALUE_1, userBean.getPhone());
                            startActivity(ModifyPhoneFirstActivity.class, bundle);
                        } else {
                            bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
                            startActivity(LoginActiivty.class, bundle);
                        }
                    }
                });

                break;
            case R.id.ll_wx:
                /*微信*/
                if (usernewEntity.isBindWx()){
                    BindingDialog dialog2 = new BindingDialog(this);
                    dialog2.show();
                    dialog2.setDialogOnClick(new BindingDialog.DialogOnClick() {
                        @Override
                        public void onClick() {
                            dialog2.dismiss();
                            if (authorizeUtils == null) {
                                authorizeUtils = new AuthorizeUtils(AccountBindingActivity.this, null);
                                authorizeUtils.setAuthorizeListener(AccountBindingActivity.this);
                            }
                            setThirdLoginEnabled(false);
                            showProgressDialog(getString(R.string.wait));
                            authorizeUtils.authWechat();
                        }
                    });
                }else {
                    /*绑定*/
                    if (authorizeUtils == null) {
                        authorizeUtils = new AuthorizeUtils(this, null);
                        authorizeUtils.setAuthorizeListener(this);
                    }
                    setThirdLoginEnabled(false);
                    showProgressDialog(getString(R.string.wait));
                    authorizeUtils.authWechat();
                }

                break;
            case R.id.ll_pay:
                /*支付宝*/
                if (usernewEntity.isBindAli()){
                    BindingDialog dialog3 = new BindingDialog(this);
                    dialog3.show();
                    dialog3.setDialogOnClick(new BindingDialog.DialogOnClick() {
                        @Override
                        public void onClick() {
                            dialog3.dismiss();
                            /*绑定*/
                            ALiLoginUtils aLiLogin = new ALiLoginUtils(AccountBindingActivity.this) {
                                @Override
                                protected void getOpentid(String unique1) {
                                    LogUtils.e(unique1);
                                }
                            };
                            aLiLogin.authV2();
                        }
                    });
                }else {
                    /*绑定*/
                    ALiLoginUtils aLiLogin = new ALiLoginUtils(this) {
                        @Override
                        protected void getOpentid(String unique1) {
                          LogUtils.e(unique1);
                            ConentUtils.httpBin3Rd(unique1, Constants.LoginType.ALI, new ConentUtils.OnSuccess() {
                                @Override
                                public void onSuccess(boolean isSuccess) {
                                    if (isSuccess) {
                                        httpData();
                                    }else {
                                        showToast("绑定失败");
                                    }
                                }
                            });
                        }
                    };
                    aLiLogin.authV2();
                }
                break;
            case R.id.ll_qq:
                /*qq*/
                if (usernewEntity.isBindQq()){
                    BindingDialog dialog4 = new BindingDialog(this);
                    dialog4.show();
                    dialog4.setDialogOnClick(new BindingDialog.DialogOnClick() {
                        @Override
                        public void onClick() {
                            dialog4.dismiss();
                            /*绑定*/
                            if (authorizeUtils == null) {
                                authorizeUtils = new AuthorizeUtils(AccountBindingActivity.this, null);
                                authorizeUtils.setAuthorizeListener(AccountBindingActivity.this);
                            }
                            setThirdLoginEnabled(false);
                            showProgressDialog(getString(R.string.wait));
                            authorizeUtils.authQQ();
                        }
                    });
                }else {
                    /*绑定*/
                    if (authorizeUtils == null) {
                        authorizeUtils = new AuthorizeUtils(this, null);
                        authorizeUtils.setAuthorizeListener(this);
                    }
                    setThirdLoginEnabled(false);
                    showProgressDialog(getString(R.string.wait));
                    authorizeUtils.authQQ();
                }
                break;
        }
    }

    private void setThirdLoginEnabled(boolean enabled) {
        llPay.setEnabled(enabled);
        llQq.setEnabled(enabled);
        llWx.setEnabled(enabled);
    }

    @Override
    public void onComplete(AuthorizeUser user) {
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
                                    ConentUtils.httpBin3Rd(user.getUsername(),Constants.LoginType.WX,new ConentUtils.OnSuccess() {
                                        @Override
                                        public void onSuccess(boolean isSuccess) {
                                            if (isSuccess) {
                                                httpData();
                                            }else {
                                                showToast("绑定失败");
                                            }
                                        }
                                    });
                                    break;
                                case "TENCENT_QQ":
                                    hideProgress();
                                    LogUtils.e(QQ, user.getUsername(), user.getHeadimgurl(), user.getNickname());
                                    ConentUtils.httpBin3Rd(user.getUsername(),Constants.LoginType.QQ,new ConentUtils.OnSuccess() {
                                        @Override
                                        public void onSuccess(boolean isSuccess) {
                                            if (isSuccess) {
                                                httpData();
                                            }else {
                                                showToast("绑定失败");
                                            }
                                        }
                                    });
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
