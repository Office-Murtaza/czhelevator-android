package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.password.ModifyPhoneSecondActivity;
import com.kingyon.elevator.uis.dialogs.BindingDialog;
import com.kingyon.elevator.utils.CheckCodePresenter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.library.social.AuthorizeUser;
import com.kingyon.library.social.AuthorizeUtils;
import com.kingyon.paylibrary.ALiLoginUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.czh.myversiontwo.utils.Constance.BINDING_ACTIVITY;
import static com.kingyon.elevator.constants.Constants.LoginType.QQ;

/**
 * @Created By Admin  on 2020/7/10
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
@Route(path = BINDING_ACTIVITY)
public class BindingActivity extends BaseActivity implements AuthorizeUtils.AuthorizeListener{

    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.head_root)
    RelativeLayout headRoot;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private CheckCodePresenter checkCodePresenter;
    private AuthorizeUtils authorizeUtils;
    @Autowired
    String mobile;
    @Autowired
    String type;
    @Override
    public int getContentViewId() {
        return R.layout.activity_binding;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        checkCodePresenter = new CheckCodePresenter(BindingActivity.this, tvCode);
        tvMobile.setText(CommonUtil.getHideMobile(mobile));
        preTvTitle.setText(type+"绑定");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(BindingActivity.this);
    }

    @OnClick({R.id.pre_v_back, R.id.tv_code, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_back:
                finish();
                break;
            case R.id.tv_code:
                checkCodePresenter.getCode(mobile, CheckCodePresenter.VerifyCodeType.UNBIND_OLD);
                break;
            case R.id.tv_next:
                verify();
                break;
        }
    }

    private void verify() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etCode))) {
            showToast("请输入验证码");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvNext.setEnabled(false);
        NetService.getInstance().unbindPhone(mobile, CommonUtil.getEditText(etCode), "UNBIND_OLD")
                .compose(BindingActivity.this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        tvTips.setText(ex.getDisplayMessage());
                        showToast(ex.getDisplayMessage());
                        tvNext.setEnabled(true);
                    }

                    @Override
                    public void onNext(String s) {
                        tvNext.setEnabled(true);
                        hideProgress();
                       switch (type){
                           case "支付宝":
                               ALiLoginUtils aLiLogin = new ALiLoginUtils(BindingActivity.this) {
                                   @Override
                                   protected void getOpentid(String unique1) {
                                       ConentUtils.httpBin3Rd(unique1, Constants.LoginType.ALI, new ConentUtils.OnSuccess() {
                                           @Override
                                           public void onSuccess(boolean isSuccess) {
                                               if (isSuccess) {
                                                   finish();
                                               }else {
                                                   showToast("绑定失败");
                                               }
                                           }
                                       });
                                   }
                               };
                               aLiLogin.authV2();
                               break;
                           case "微信":
                               /*绑定*/
                               if (authorizeUtils == null) {
                                   authorizeUtils = new AuthorizeUtils(BindingActivity.this, null);
                                   authorizeUtils.setAuthorizeListener(BindingActivity.this);
                               }
                               showProgressDialog(getString(R.string.wait));
                               authorizeUtils.authWechat();
                               break;
                           case "QQ":

                               if (authorizeUtils == null) {
                                   authorizeUtils = new AuthorizeUtils(BindingActivity.this, null);
                                   authorizeUtils.setAuthorizeListener(BindingActivity.this);
                               }
                               showProgressDialog(getString(R.string.wait));
                               authorizeUtils.authQQ();
                               break;
                           
                       }
                    }
                });
    }

    @Override
    public void onComplete(AuthorizeUser user) {
        if (user != null && user.getUsername() != null) {
            Observable.just(user)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomApiCallback<AuthorizeUser>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                           
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
                                                finish();
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
                                                finish();
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
                       
                    }

                    @Override
                    public void onNext(Integer integer) {
                        showToast("授权失败");
                        hideProgress();
                       
                    }
                });
    }
}
