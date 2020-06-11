package com.kingyon.elevator.uis.fragments.main2;

import android.os.Bundle;
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
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.cooperation.CooperationActivity;
import com.kingyon.elevator.uis.activities.installer.InstallerActivity;
import com.kingyon.elevator.uis.activities.property.PropertyActivity;
import com.kingyon.elevator.uis.activities.salesman.SalesmanActivity;
import com.kingyon.elevator.uis.activities.user.InviteActivity;
import com.kingyon.elevator.uis.activities.user.MyAdActivity;
import com.kingyon.elevator.uis.activities.user.MyCollectActivity;
import com.kingyon.elevator.uis.activities.user.MyInvoiceActivity;
import com.kingyon.elevator.uis.activities.user.MyWalletActivity;
import com.kingyon.elevator.uis.activities.user.SettingActivity;
import com.kingyon.elevator.uis.activities.user.UserProfileActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RoleUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_CERTIFICATION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_IDENTITY_INFO;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_LOGIN;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_ATTENTION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ORDER;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_RE_CODE;

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
            String authStatus = user.getAuthStatus() != null ? user.getAuthStatus() : "";
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
            llBusiness.setVisibility(RoleUtils.getInstance().roleBeTarget(Constants.RoleType.SALESMAN, user.getRole()) ? View.VISIBLE : View.GONE);
            llProperty.setVisibility(RoleUtils.getInstance().roleBeTarget(Constants.RoleType.INSTALLER, user.getRole()) ? View.VISIBLE : View.GONE);
        } else {
            GlideUtils.loadLocalImage(getContext(), R.mipmap.im_head_placeholder, imgPlaceholder);
            tvName.setText("登录/注册");
            llBusiness.setVisibility(View.GONE);
            llProperty.setVisibility(View.GONE);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_code, R.id.img_placeholder, R.id.tv_name, R.id.img_certification, R.id.tv_grade,
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
                if (islogin()){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_LOGIN);
                }else {
                   ActivityUtils.setActivity(ACTIVITY_RE_CODE);
                }
                break;
            case R.id.img_placeholder:
                /*头像*/
                if (islogin()){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_LOGIN);
                }else {
                    startActivity(UserProfileActivity.class);
                }
                break;
            case R.id.tv_name:
                /*昵称*/
                if (islogin()){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_LOGIN);
                }else {
                    startActivity(UserProfileActivity.class);
                }
                break;
            case R.id.img_certification:
                /*认证*/
                if (islogin()){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_LOGIN);
                }else {
//                    ActivityUtils.setActivity(ACTIVITY_IDENTITY_INFO);
                    ActivityUtils.setActivity(ACTIVITY_CERTIFICATION);
                }
                break;
            case R.id.tv_grade:
                /*等级*/
                if (islogin()){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_LOGIN);
                }else {
                    startActivity(UserProfileActivity.class);
                }
                break;
            case R.id.ll_portrait:
                /*个人资料*/
                if (islogin()){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_LOGIN);
                }else {
                    startActivity(UserProfileActivity.class);
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
                break;
            case R.id.ll_dynamic:
                /*动态*/

                break;
            case R.id.tv_attention_number:
                break;
            case R.id.ll_attention:
                /*关注*/
                ActivityUtils.setActivity(ACTIVITY_MASSAGE_ATTENTION);
                break;
            case R.id.tv_fan_number:
                break;
            case R.id.ll_fan:
                /*粉丝*/
                ActivityUtils.setActivity(ACTIVITY_MASSAGE_ATTENTION);
                break;
            case R.id.tv_card_number:
                break;
            case R.id.ll_card:
                /*卡卷*/
                break;
            case R.id.ll_statistical:
                break;
            case R.id.ll_order:
                /*订单*/
//                MyActivityUtils.goOrderCOntainerActivity(getActivity(), new NormalParamEntity("", "全部订单"));

                ActivityUtils.setActivity(ACTIVITY_ORDER);

                break;
            case R.id.ll_collect:
                /*收藏*/
                startActivity(MyCollectActivity.class);
                break;
            case R.id.ll_advertisi:
                /*广告*/
                startActivity(MyAdActivity.class);
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
                startActivity(MyWalletActivity.class);
                break;
            case R.id.ll_invoice:
                /*我的发票*/
                startActivity(MyInvoiceActivity.class);
                break;
            case R.id.ll_task:
                /*我的任务*/
                break;
            case R.id.ll_partner:
                /*合伙人*/
                startActivity(CooperationActivity.class);
                break;
            case R.id.ll_installation:
                /*安装管理*/
                startActivity(InstallerActivity.class);
                break;
            case R.id.ll_business:
                /*业务管理*/
                startActivity(SalesmanActivity.class);
                break;
            case R.id.ll_property:
                /*物业管理*/
                startActivity(PropertyActivity.class);
                break;
            case R.id.ll_invite:
                /*邀请有礼*/
                startActivity(InviteActivity.class);
                break;
            case R.id.ll_customer:
                /*客服电话*/
                try {
                    AFUtil.toCall(getContext(), getString(R.string.service_phone));
                }catch (Exception e){
                    ToastUtils.showToast(getActivity(),"当前手机不允许拨打电话，请换手机操作",2000);
                }
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
        if (tvName.getText().toString().equals("登录/注册")){
            return true;
        }else {
            return false;
        }
    }
}
