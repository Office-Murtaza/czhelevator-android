package com.kingyon.elevator.uis.fragments.main2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.cooperation.CooperationActivity;
import com.kingyon.elevator.uis.activities.homepage.WikiListActivity;
import com.kingyon.elevator.uis.activities.installer.InstallerActivity;
import com.kingyon.elevator.uis.activities.property.PropertyActivity;
import com.kingyon.elevator.uis.activities.salesman.SalesmanActivity;
import com.kingyon.elevator.uis.activities.user.IdentitySuccessActivity;
import com.kingyon.elevator.uis.activities.user.InviteActivity;
import com.kingyon.elevator.uis.activities.user.MyAdActivity;
import com.kingyon.elevator.uis.activities.user.MyCouponsActivty;
import com.kingyon.elevator.uis.activities.user.MyInvoiceActivity;
import com.kingyon.elevator.uis.activities.user.MyWalletActivity;
import com.kingyon.elevator.uis.activities.user.SettingActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.RoleUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ATTENTION_FANS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_CERTIFICATION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_CUSTOMER_SERVICE_CENTER;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MYDYNAMIC;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MY_COLLECT;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ORDER;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_RE_CODE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_CENTER;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:个人
 */
public class PersonalFragment extends BaseFragment {
    @BindView(R.id.ll_persona)
    RelativeLayout llPersona;
    Unbinder unbinder;
    @BindView(R.id.img_code)
    ImageView imgCode;
    @BindView(R.id.img_placeholder)
    ImageView imgPlaceholder;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.img_certification)
    ImageView imgCertification;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.ll_portrait)
    LinearLayout llPortrait;
    @BindView(R.id.tv_t_currency)
    TextView tvTCurrency;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.ll_amount)
    LinearLayout llAmount;
    @BindView(R.id.tv_dynamic_number)
    TextView tvDynamicNumber;
    @BindView(R.id.ll_dynamic)
    LinearLayout llDynamic;
    @BindView(R.id.tv_attention_number)
    TextView tvAttentionNumber;
    @BindView(R.id.ll_attention)
    LinearLayout llAttention;
    @BindView(R.id.tv_fan_number)
    TextView tvFanNumber;
    @BindView(R.id.ll_fan)
    LinearLayout llFan;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;
    @BindView(R.id.ll_card)
    LinearLayout llCard;
    @BindView(R.id.ll_statistical)
    LinearLayout llStatistical;
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.ll_advertisi)
    LinearLayout llAdvertisi;
    @BindView(R.id.ll_activity)
    LinearLayout llActivity;
    @BindView(R.id.ll_function)
    LinearLayout llFunction;
    @BindView(R.id.image_banner)
    ImageView imageBanner;
    @BindView(R.id.ll_wallet)
    LinearLayout llWallet;
    @BindView(R.id.ll_invoice)
    LinearLayout llInvoice;
    @BindView(R.id.ll_task)
    LinearLayout llTask;
    @BindView(R.id.ll_partner)
    LinearLayout llPartner;
    @BindView(R.id.ll_installation)
    LinearLayout llInstallation;
    @BindView(R.id.ll_business)
    LinearLayout llBusiness;
    @BindView(R.id.ll_property)
    LinearLayout llProperty;
    @BindView(R.id.ll_invite)
    LinearLayout llInvite;
    @BindView(R.id.ll_customer)
    LinearLayout llCustomer;
    @BindView(R.id.ll_system)
    LinearLayout llSystem;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_hx1)
    TextView tvHx1;
    @BindView(R.id.tv_hx2)
    TextView tvHx2;
    String otherUserAccount;
    String authStatus;
    @Override
    public int getContentViewId() {
        return R.layout.fragment_persona;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        httpPersonal();


    }

    @Override
    public void onResume() {
        super.onResume();
        httpPersonal();
        otherUserAccount = DataSharedPreferences.getCreatateAccount();
    }

    private void httpPersonal() {
        if (!TextUtils.isEmpty(Net.getInstance().getToken())) {
            NetService.getInstance().userProfile()
                    .compose(this.<UserEntity>bindLifeCycle())
                    .subscribe(new CustomApiCallback<UserEntity>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            updateHeadUi(null);
                        }

                        @Override
                        public void onNext(UserEntity userEntity) {
                            if (userEntity != null) {
                                updateHeadUi(userEntity);
                                DataSharedPreferences.saveUserBean(userEntity);
                            } else {
                                updateHeadUi(new UserEntity());
                            }
                        }
                    });
        } else {
            updateHeadUi(null);
        }


    }

    private void updateHeadUi(UserEntity user) {
        if (user != null) {
            GlideUtils.loadAvatarImageTransparent(getContext(), user.getAvatar(), imgPlaceholder);
            tvName.setText(user.getNikeName() != null ? user.getNikeName() : CommonUtil.getHideMobile(user.getPhone()));
            tvAttentionNumber.setText(user.getUserCenterAttr().attentionNum + "");
            tvFanNumber.setText(user.getUserCenterAttr().fansNum + "");
            tvDynamicNumber.setText(user.getUserCenterAttr().dynamicNum + "");
            tvCardNumber.setText(user.getUserCenterAttr().couponNum + "");
            tvTCurrency.setText("T币：￥" + user.getUserCenterAttr().tlwMoney + "");
            tvIntegral.setText("积分：" + user.getUserCenterAttr().integral + "");
            LogUtils.e(user.getRole());
             authStatus = user.getAuthStatus() != null ? user.getAuthStatus() : "";
            switch (authStatus) {
                case Constants.IDENTITY_STATUS.AUTHING:
                    /*认证中*/
                    imgCertification.setImageResource(R.mipmap.ic_personal_certification_off);
                    break;
                case Constants.IDENTITY_STATUS.FAILD:
                    /*认证失败*/
                    imgCertification.setImageResource(R.mipmap.ic_personal_certification_off);
                    break;
                case Constants.IDENTITY_STATUS.AUTHED:
                    /*已认证*/
                    imgCertification.setImageResource(R.mipmap.ic_personal_certification_on);
                    break;
                case Constants.IDENTITY_STATUS.NO_AUTH:
                    /*未认证*/
                    imgCertification.setImageResource(R.mipmap.ic_personal_certification_off);
                    break;
                default:
                    imgCertification.setImageResource(R.mipmap.ic_personal_certification_off);
                    break;
            }
            /*安装*/
            llInstallation.setVisibility(RoleUtils.getInstance().roleBeTarget(Constants.RoleType.INSTALLER, user.getRole()) ? View.VISIBLE : View.GONE);
            /*业务*/
            llBusiness.setVisibility(RoleUtils.getInstance().roleBeTarget(Constants.RoleType.SALESMAN, user.getRole()) ? View.VISIBLE : View.GONE);
           /*物业*/
            llProperty.setVisibility(RoleUtils.getInstance().roleBeTarget(Constants.RoleType.PROPERTY, user.getRole()) ? View.VISIBLE : View.GONE);
            if (!RoleUtils.getInstance().roleBeTarget(Constants.RoleType.PROPERTY, user.getRole())&&
                !RoleUtils.getInstance().roleBeTarget(Constants.RoleType.SALESMAN, user.getRole())&&
                !RoleUtils.getInstance().roleBeTarget(Constants.RoleType.INSTALLER, user.getRole())){
                    tvHx1.setVisibility(View.GONE);
                    tvHx2.setVisibility(View.GONE);
            }else {
                tvHx1.setVisibility(View.VISIBLE);
                tvHx2.setVisibility(View.VISIBLE);
            }
        } else {
            GlideUtils.loadLocalImage(getContext(), R.mipmap.im_head_placeholder, imgPlaceholder);
            tvName.setText("登录/注册");
            llBusiness.setVisibility(View.GONE);
            llProperty.setVisibility(View.GONE);
            imgCertification.setImageResource(R.mipmap.ic_personal_certification_off);
            tvAttentionNumber.setText("0");
            tvFanNumber.setText("0");
            tvDynamicNumber.setText("0");
            tvCardNumber.setText("0");
            tvTCurrency.setText("T币：￥0.00");
            tvIntegral.setText("积分：0.00");
            llProperty.setVisibility(View.GONE);
            llBusiness.setVisibility(View.GONE);
            llInstallation.setVisibility(View.GONE);
            tvHx1.setVisibility(View.GONE);
            tvHx2.setVisibility(View.GONE);

        }
    }

    @Override
    protected void dealLeackCanary() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        otherUserAccount = DataSharedPreferences.getCreatateAccount();

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                httpPersonal();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_code, R.id.img_certification,
            R.id.ll_portrait, R.id.tv_t_currency, R.id.tv_integral, R.id.ll_amount, R.id.tv_dynamic_number,
            R.id.ll_dynamic, R.id.tv_attention_number, R.id.ll_attention, R.id.tv_fan_number, R.id.ll_fan,
            R.id.tv_card_number, R.id.ll_card, R.id.ll_statistical, R.id.ll_order, R.id.ll_collect, R.id.ll_advertisi,
            R.id.ll_activity, R.id.ll_function, R.id.image_banner, R.id.ll_wallet, R.id.ll_invoice, R.id.ll_task,
            R.id.ll_partner, R.id.ll_installation, R.id.ll_business, R.id.ll_property, R.id.ll_invite, R.id.ll_customer,
            R.id.ll_system, R.id.ll_persona})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_code:
                /*二维码*/
                if (isToken(getActivity())) {
                    ActivityUtils.setActivity(ACTIVITY_RE_CODE);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.img_certification:
                /*认证*/
                if (isToken(getActivity())) {
                    if (!authStatus.isEmpty()) {
                        switch (authStatus) {
                            case Constants.IDENTITY_STATUS.AUTHING:
                                /*认证中*/
                                Bundle bundle = new Bundle();
                                bundle.putString("type", Constants.IDENTITY_STATUS.AUTHING);
                                startActivity(IdentitySuccessActivity.class, bundle);
                                break;
                            case Constants.IDENTITY_STATUS.FAILD:
                                /*认证失败*/
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("type", Constants.IDENTITY_STATUS.FAILD);
                                startActivity(IdentitySuccessActivity.class, bundle1);
                                break;
                            case Constants.IDENTITY_STATUS.AUTHED:
                                /*已认证*/
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("type", Constants.IDENTITY_STATUS.AUTHED);
                                startActivity(IdentitySuccessActivity.class, bundle2);
                                break;
                            case Constants.IDENTITY_STATUS.NO_AUTH:
                                /*未认证*/
                                ActivityUtils.setActivity(ACTIVITY_CERTIFICATION);
                                break;
                            default:
                                ActivityUtils.setActivity(ACTIVITY_CERTIFICATION);
                                break;
                        }
                    }else {
                        showToast("数据错误 请重试");
                        httpPersonal();
                    }
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.ll_portrait:
                /*个人资料*/
                if (isToken(getActivity())) {
                    LogUtils.e(otherUserAccount);
                    ActivityUtils.setActivity(ACTIVITY_USER_CENTER, "type", "1","otherUserAccount",otherUserAccount);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.tv_t_currency:
                /*T币*/
//                startActivity(MyWalletActivity.class);
                break;
            case R.id.tv_integral:
                /*积分*/
                break;
            case R.id.ll_amount:
                /*余额积分*/

                break;
            case R.id.tv_dynamic_number:
            case R.id.ll_dynamic:
                /*我的动态*/
//                if (isToken(getActivity())) {
//                    ActivityUtils.setActivity(ACTIVITY_MYDYNAMIC);
//                } else {
//                    ActivityUtils.setLoginActivity();
//                }
                if (isToken(getActivity())) {
                    LogUtils.e(otherUserAccount);
                    ActivityUtils.setActivity(ACTIVITY_USER_CENTER, "type", "1","otherUserAccount",otherUserAccount);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.tv_attention_number:
            case R.id.ll_attention:
                /*关注*/
                if (isToken(getActivity())) {
                    ActivityUtils.setActivity(ACTIVITY_ATTENTION_FANS, "page", 0);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.tv_fan_number:
            case R.id.ll_fan:
                /*粉丝*/
                if (isToken(getActivity())) {
                    ActivityUtils.setActivity(ACTIVITY_ATTENTION_FANS, "page", 1);
                } else {

                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.tv_card_number:
            case R.id.ll_card:
                /*卡卷*/
                if (isToken(getActivity())) {
                    startActivity(MyCouponsActivty.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.ll_statistical:
                break;
            case R.id.ll_order:
                /*订单*/
//                MyActivityUtils.goOrderCOntainerActivity(getActivity(), new NormalParamEntity("", "全部订单"));
                if (isToken(getActivity())) {
                    ActivityUtils.setActivity(ACTIVITY_ORDER);
                } else {
                    ActivityUtils.setLoginActivity();
                }

                break;
            case R.id.ll_collect:
                /*收藏*/
                if (isToken(getActivity())) {
                    ActivityUtils.setActivity(ACTIVITY_MY_COLLECT);
                } else {
                    ActivityUtils.setLoginActivity();
//                    startActivity(MyCollectActivity.class);
                }

                break;
            case R.id.ll_advertisi:
                /*广告*/
                if (isToken(getActivity())) {
                    startActivity(MyAdActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.ll_activity:
                /*活动*/
                break;
            case R.id.ll_function:
                break;
            case R.id.image_banner:
                /*图片*/
                break;
            case R.id.ll_wallet:
                /*我的钱包*/
                if (isToken(getActivity())) {
                    startActivity(MyWalletActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.ll_invoice:
                /*我的发票*/
                if (isToken(getActivity())) {
                    startActivity(MyInvoiceActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }

                break;
            case R.id.ll_task:
                /*我的任务*/

                break;
            case R.id.ll_partner:
                /*合伙人*/
                if (isToken(getActivity())) {
                    startActivity(CooperationActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }

                break;
            case R.id.ll_installation:
                /*安装管理*/
                if (isToken(getActivity())) {
                    startActivity(InstallerActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }

                break;
            case R.id.ll_business:
                /*业务管理*/
                if (isToken(getActivity())) {
                    startActivity(SalesmanActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }

                break;
            case R.id.ll_property:
                /*物业管理*/
                if (isToken(getActivity())) {
                    startActivity(PropertyActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.ll_invite:
                /*邀请有礼*/
                startActivity(InviteActivity.class);
                break;
            case R.id.ll_customer:
                /*客服电话*/
                ActivityUtils.setActivity(ACTIVITY_CUSTOMER_SERVICE_CENTER);
                break;
            case R.id.ll_system:
                /*系统设置*/
                startActivity(SettingActivity.class);
                break;
            case R.id.ll_persona:
                break;
        }
    }

    private boolean islogin() {
        if (tvName.getText().toString().equals("登录/注册")) {
            return true;
        } else {
            return false;
        }
    }
}
