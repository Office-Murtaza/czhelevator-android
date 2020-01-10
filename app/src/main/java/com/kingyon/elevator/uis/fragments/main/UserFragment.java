package com.kingyon.elevator.uis.fragments.main;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.customview.PlanSelectDateDialog;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.entities.ReceivedPushEntity;
import com.kingyon.elevator.entities.TabEntity;
import com.kingyon.elevator.entities.UnreadNumberEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.AgreementActivity;
import com.kingyon.elevator.uis.activities.user.MyAdActivity;
import com.kingyon.elevator.uis.activities.installer.InstallerActivity;
import com.kingyon.elevator.uis.activities.password.LoginActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationActivity;
import com.kingyon.elevator.uis.activities.property.PropertyActivity;
import com.kingyon.elevator.uis.activities.salesman.SalesmanActivity;
import com.kingyon.elevator.uis.activities.user.IdentityInfoActivity;
import com.kingyon.elevator.uis.activities.user.IdentitySuccessActivity;
import com.kingyon.elevator.uis.activities.user.InviteActivity;
import com.kingyon.elevator.uis.activities.user.MyMateriaActivity;
import com.kingyon.elevator.uis.activities.user.MyInvoiceActivity;
import com.kingyon.elevator.uis.activities.user.MessageCenterActivity;
import com.kingyon.elevator.uis.activities.user.MyCollectActivity;
import com.kingyon.elevator.uis.activities.user.MyCouponsActivty;
import com.kingyon.elevator.uis.activities.user.MyPlanActivity;
import com.kingyon.elevator.uis.activities.user.MyWalletActivity;
import com.kingyon.elevator.uis.activities.user.SettingActivity;
import com.kingyon.elevator.uis.activities.user.UserProfileActivity;
import com.kingyon.elevator.uis.widgets.ProportionFrameLayout;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.DownloadUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RoleUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshFragment;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ScreenUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/12/24.
 * Email：lc824767150@163.com
 */

public class UserFragment extends BaseStateRefreshFragment {

    @BindView(R.id.pfl_head)
    ProportionFrameLayout pflHead;
    @BindView(R.id.ll_cooperation)
    LinearLayout llCooperation;
    @BindView(R.id.ll_property)
    LinearLayout llProperty;
    @BindView(R.id.ll_saldesman)
    LinearLayout llSalesman;
    @BindView(R.id.ll_installer)
    LinearLayout llInstaller;
    @BindView(R.id.nsv)
    NestedScrollView nsv;

    @BindView(R.id.img_help)
    ImageView imgHelp;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    @BindView(R.id.v_message)
    TextView vMessage;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_auth_info)
    TextView tvAuthInfo;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;

    private float limitedY;

    public static UserFragment newInstance() {
        Bundle args = new Bundle();
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_user;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        EventBus.getDefault().post(new ReceivedPushEntity());
        StatusBarUtil.setHeadViewPadding(getActivity(), llTitle);
        initNestedScrollListener();
    }

    private void initNestedScrollListener() {
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (limitedY == 0) {
                    limitedY = ScreenUtil.getScreenWidth(getContext()) / pflHead.getProporty() - StatusBarUtil.getStatusBarHeight(getContext());
                }
                if (scrollY == 0) {
                    llTitle.setBackgroundColor(0x00FFFFFF);
                    preTvTitle.setTextColor(0xFFFFFFFF);
                    vLine.setBackgroundColor(0x00EEEEEE);

                    imgHelp.setImageDrawable(getBackDrawable(0xFFFFFFFF, R.drawable.ic_user_help));
                    imgSetting.setImageDrawable(getBackDrawable(0xFFFFFFFF, R.drawable.ic_user_setting));
                    imgMessage.setImageDrawable(getBackDrawable(0xFFFFFFFF, R.drawable.ic_user_message));
                } else if (scrollY < limitedY) {
                    float percent = (limitedY - scrollY) / limitedY;
                    int alpha = (int) (255 - percent * 255);
                    llTitle.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                    int blackValue = (int) (0x33 + (0xFF - 0x33) * percent);
                    preTvTitle.setTextColor(Color.argb(0xFF, blackValue, blackValue, blackValue));
                    vLine.setBackgroundColor(Color.argb(alpha, 238, 238, 238));

                    int drawableColor = Color.argb(0xFF, blackValue, blackValue, blackValue);
                    imgHelp.setImageDrawable(getBackDrawable(drawableColor, R.drawable.ic_user_help));
                    imgSetting.setImageDrawable(getBackDrawable(drawableColor, R.drawable.ic_user_setting));
                    imgMessage.setImageDrawable(getBackDrawable(drawableColor, R.drawable.ic_user_message));
                } else {
                    llTitle.setBackgroundColor(0xFFFFFFFF);
                    preTvTitle.setTextColor(0xFF333333);
                    vLine.setBackgroundColor(0xFFEEEEEE);

                    imgHelp.setImageDrawable(getBackDrawable(0xFF333333, R.drawable.ic_user_help));
                    imgSetting.setImageDrawable(getBackDrawable(0xFF333333, R.drawable.ic_user_setting));
                    imgMessage.setImageDrawable(getBackDrawable(0xFF333333, R.drawable.ic_user_message));
                }
            }
        });
        rlTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private Drawable getBackDrawable(int color, int resId) {
        Drawable up = ContextCompat.getDrawable(getContext(), resId);
        Drawable drawableUp = null;
        if (up != null) {
            drawableUp = DrawableCompat.wrap(up);
            DrawableCompat.setTint(drawableUp, color);
        }
        Drawable up1 = ContextCompat.getDrawable(getContext(), resId);
        if (up1 != null) {
            Drawable drawableUp1 = DrawableCompat.unwrap(up1);
            DrawableCompat.setTintList(drawableUp1, null);
        }
        return drawableUp;
    }

    @Override
    public void onRefresh() {
        loadingComplete(STATE_CONTENT);
        if (!TextUtils.isEmpty(Net.getInstance().getToken())) {
            NetService.getInstance().userProfile()
                    .compose(this.<UserEntity>bindLifeCycle())
                    .subscribe(new CustomApiCallback<UserEntity>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            updateHeadUi(new UserEntity());
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
            GlideUtils.loadAvatarImageTransparent(getContext(), user.getAvatar(), imgHead);
            tvName.setText(user.getNikeName() != null ? user.getNikeName() : CommonUtil.getHideMobile(user.getPhone()));
            tvAuthInfo.setVisibility(View.VISIBLE);
            String authStatus = user.getAuthStatus() != null ? user.getAuthStatus() : "";
            switch (authStatus) {
                case Constants.IDENTITY_STATUS.AUTHING:
                    tvAuthInfo.setText("认证中");
                    tvAuthInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    break;
                case Constants.IDENTITY_STATUS.FAILD:
                    tvAuthInfo.setText("认证失败");
                    tvAuthInfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_user_auth_none, 0, 0, 0);
                    break;
                case Constants.IDENTITY_STATUS.AUTHED:
                    tvAuthInfo.setText("已认证");
                    tvAuthInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    break;
                case Constants.IDENTITY_STATUS.NO_AUTH:
                    tvAuthInfo.setText("未认证");
                    tvAuthInfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_user_auth_none, 0, 0, 0);
                    break;
                default:
                    tvAuthInfo.setText("");
                    tvAuthInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    break;
            }

//            llSalesman.setVisibility(TextUtils.equals(Constants.RoleType.SALESMAN, user.getRole()) ? View.VISIBLE : View.GONE);
//            llInstaller.setVisibility(TextUtils.equals(Constants.RoleType.INSTALLER, user.getRole()) ? View.VISIBLE : View.GONE);

            llSalesman.setVisibility(RoleUtils.getInstance().roleBeTarget(Constants.RoleType.SALESMAN, user.getRole()) ? View.VISIBLE : View.GONE);
            llInstaller.setVisibility(RoleUtils.getInstance().roleBeTarget(Constants.RoleType.INSTALLER, user.getRole()) ? View.VISIBLE : View.GONE);
        } else {
            GlideUtils.loadLocalImage(getContext(), R.drawable.bg_transparent_avatar, imgHead);
            tvName.setText("登录/注册");
            tvAuthInfo.setVisibility(View.GONE);

            llSalesman.setVisibility(View.GONE);
            llInstaller.setVisibility(View.GONE);
        }

//        tvSalesman.setVisibility(View.VISIBLE);
//        tvInstaller.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedUnread(UnreadNumberEntity entity) {
        updateMessageNumber(entity.getTotalUnread());
    }

    private void updateMessageNumber(long totalUnread) {
        if (totalUnread < 1) {
            vMessage.setVisibility(View.GONE);
            vMessage.setText("");
        } else if (totalUnread < 100) {
            vMessage.setVisibility(View.VISIBLE);
            vMessage.setText(String.valueOf(totalUnread));
        } else {
            vMessage.setVisibility(View.VISIBLE);
            vMessage.setText(getString(R.string.number_max));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            autoRefresh();
        }
    }

    @OnClick({R.id.img_help, R.id.rl_message, R.id.img_setting, R.id.ll_info, R.id.tv_auth_info, R.id.tv_order_all, R.id.tv_order_deal, R.id.tv_order_completed, R.id.tv_ad, R.id.tv_wallet, R.id.tv_coupons, R.id.tv_invoice, R.id.tv_contract, R.id.tv_fodder, R.id.tv_collect, R.id.tv_plan, R.id.tv_gifts, R.id.tv_cooperation, R.id.tv_property, R.id.tv_saldesman, R.id.tv_installer, R.id.tv_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_help:
                AgreementActivity.start((BaseActivity) getActivity(), "帮助", Constants.AgreementType.HELP.getValue());
                break;
            case R.id.rl_message:
                startActivity(MessageCenterActivity.class);
                break;
            case R.id.img_setting:
               startActivity(SettingActivity.class);
                break;
            case R.id.ll_info:
                if (TextUtils.isEmpty(Net.getInstance().getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(UserProfileActivity.class);
                }
                break;
            case R.id.tv_auth_info:
                jumpToIdentity();
                break;
            case R.id.tv_order_all:
                EventBus.getDefault().post(new TabEntity(2, new NormalParamEntity("", "全部订单")));
                break;
            case R.id.tv_order_deal:
                EventBus.getDefault().post(new TabEntity(2, new NormalParamEntity(Constants.OrderStatus.RELEASEING, "发布中")));
                break;
            case R.id.tv_order_completed:
                EventBus.getDefault().post(new TabEntity(2, new NormalParamEntity(Constants.OrderStatus.COMPLETE, "已完成")));
                break;
            case R.id.tv_ad:
                startActivity(MyAdActivity.class);
                break;
            case R.id.tv_wallet:
                startActivity(MyWalletActivity.class);
                break;
            case R.id.tv_coupons:
                startActivity(MyCouponsActivty.class);
                break;
            case R.id.tv_invoice:
                startActivity(MyInvoiceActivity.class);
                break;
            case R.id.tv_contract:
                downloadContract();
                break;
            case R.id.tv_fodder:
                startActivity(MyMateriaActivity.class);
                break;
            case R.id.tv_collect:
                startActivity(MyCollectActivity.class);
                break;
            case R.id.tv_plan:
                startActivity(MyPlanActivity.class);
                break;
            case R.id.tv_gifts:
                startActivity(InviteActivity.class);
                break;
            case R.id.tv_cooperation:
                startActivity(CooperationActivity.class);
                //MyActivityUtils.goFragmentContainerActivity(getActivity(), FragmentConstants.SetPasswordFragment);
                break;
            case R.id.tv_property:
                startActivity(PropertyActivity.class);
                break;
            case R.id.tv_saldesman:
                startActivity(SalesmanActivity.class);
                break;
            case R.id.tv_installer:
                startActivity(InstallerActivity.class);
                break;
            case R.id.tv_service:
                AFUtil.toCall(getContext(), getString(R.string.service_phone));
                break;
        }
    }

    private void downloadContract() {
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().downloadContract()
                .compose(this.<DataEntity<String>>bindLifeCycle())
                .subscribe(new CustomApiCallback<DataEntity<String>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                    }

                    @Override
                    public void onNext(DataEntity<String> dataEntity) {
                        if (dataEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        hideProgress();
                        String contractUrl = dataEntity.getData();
                        if (TextUtils.isEmpty(contractUrl)) {
                            showToast("没有可供下载的合同");
                        } else {
                            DownloadUtils.getInstance().openReportFile(getContext(), contractUrl, contractUrl.substring(contractUrl.lastIndexOf("/") + 1));
                        }
                    }
                });
    }

    private void jumpToIdentity() {
        UserEntity user = DataSharedPreferences.getUserBean();
        String authStatus = (user != null && user.getAuthStatus() != null) ? user.getAuthStatus() : "";
        switch (authStatus) {
            case Constants.IDENTITY_STATUS.NO_AUTH:
            case Constants.IDENTITY_STATUS.FAILD:
                startActivity(IdentityInfoActivity.class);
                break;
            case Constants.IDENTITY_STATUS.AUTHED:
                startActivity(IdentitySuccessActivity.class);
                break;
            default:
                showToast(tvAuthInfo.getText().toString());
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }
}
