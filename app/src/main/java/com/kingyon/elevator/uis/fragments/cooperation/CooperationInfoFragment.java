package com.kingyon.elevator.uis.fragments.cooperation;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.IdentityInfoEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.PartnerIndexInfoEntity;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.actiivty2.user.AboutActivity;
import com.kingyon.elevator.uis.actiivty2.user.IdentityCertificationActivity;
import com.kingyon.elevator.uis.activities.WebViewActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationDeviceActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationWithdrawRecordsActivity;
import com.kingyon.elevator.uis.activities.cooperation.WithdrawalWayActivity;
import com.kingyon.elevator.uis.dialogs.TipDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.PublicFuncation;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.SrcSuccess;
import com.kingyon.elevator.view.AlwaysMarqueeTextView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_CERTIFICATION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_EARNINGS_YESTERDAY;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_HAVE_WITHDRAWAL;
import static com.kingyon.elevator.utils.utilstwo.HtmlUtil.delHTMLTag;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * 合伙人首页BaseStateRefreshingActivity
 */

public class CooperationInfoFragment extends BaseFragment implements OnParamsChangeInterface, SwipeRefreshHelper.OnSwipeRefreshListener  {


    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_all_income)
    TextView tvAllIncome;
    @BindView(R.id.yesterday_income)
    TextView yesterdayIncome;
    @BindView(R.id.tv_can_crash)
    TextView tvCanCrash;
    @BindView(R.id.tv_already_crash)
    TextView tvAlreadyCrash;
    @BindView(R.id.already_crash_container)
    LinearLayout alreadyCrashContainer;
    @BindView(R.id.btn_apply_crash)
    LinearLayout btnApplyCrash;
    @BindView(R.id.tv_device_manager)
    LinearLayout tvDeviceManager;
    Unbinder unbinder;
    @BindView(R.id.container_view)
    RelativeLayout containerView;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_income_today)
    LinearLayout llIncomeToday;
    @BindView(R.id.tv_notice)
    AlwaysMarqueeTextView tvNotice;
    @BindView(R.id.pre_refresh)
    SwipeRefreshLayout preRefresh;
    protected SwipeRefreshHelper mSwipeRefreshHelper;
    private CooperationInfoNewEntity cooperationInfoNewEntity;
    private TipDialog<String> tipDialog;

    public static CooperationInfoFragment newInstance(CooperationInfoNewEntity entity) {
        Bundle args = new Bundle();
        args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        CooperationInfoFragment fragment = new CooperationInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_cooperation_info;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("合伙人");
        if (getArguments() != null) {
            cooperationInfoNewEntity = getArguments().getParcelable(CommonUtil.KEY_VALUE_1);
        } else {
            cooperationInfoNewEntity = new CooperationInfoNewEntity();
        }
        RuntimeUtils.cooperationInfoNewEntity = cooperationInfoNewEntity;
        StatusBarUtil.setHeadViewPadding(getActivity(), containerView);
//        preVBack.setImageDrawable(getBackDrawable(0xFFFFFFFF));
        httpData();
        loadWindowAd();
        ConentUtils.httpData(Constants.AgreementType.PARTNER_PROMPT.getValue(), new SrcSuccess() {
            @Override
            public void srcSuccess(String data) {
                tvNotice.setText(delHTMLTag(data) + "");
            }
        });
        mSwipeRefreshHelper = new SwipeRefreshHelper(preRefresh);
        mSwipeRefreshHelper.setOnSwipeRefreshListener(this);
    }

    private void httpData() {
        showProgressDialog(getString(R.string.wait));
//        DataSharedPreferences.getUserBean().getAuthStatus();
        NetService.getInstance().userProfile()
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (ex.getCode()==100200){
                            ActivityUtils.setLoginActivity();
                        }
                    }
                    @Override
                    public void onNext(UserEntity userEntity) {
                        DataSharedPreferences.saveUserBean(userEntity);
                        if (userEntity!=null) {
                            switch (userEntity.getAuthStatus()) {
                                case Constants.IDENTITY_STATUS.AUTHING:
                                    shwoDialog("您已提交个人认证申请，工作人员正在审核处理中，预计需要5个工作日。", false, false);
                                    break;
                                case Constants.IDENTITY_STATUS.FAILD:
                                    shwoDialog("个人认证失败，请重新提交", true, false);
                                    break;
                                case Constants.IDENTITY_STATUS.NO_AUTH:
                                    shwoDialog("为了更好的为您提供服务，请先完成资质认证。", true, false);
                                    break;
                                case Constants.IDENTITY_STATUS.AUTHED:
                                    httpQing();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    private void httpQing() {
        NetService.getInstance().setPartnerIndexInfo()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<PartnerIndexInfoEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        hideProgress();
                        loadingComplete();
                    }

                    @Override
                    public void onNext(ConentEntity<PartnerIndexInfoEntity> list) {
                        hideProgress();
                        PartnerIndexInfoEntity partnerIndexInfoEntity = list.getContent().get(0);
                        loadingComplete();
                        updateUI(partnerIndexInfoEntity);
                        LogUtils.e(partnerIndexInfoEntity.toString());
                    }
                });

        NetService.getInstance().getIdentityInformation()
                .compose(this.<IdentityInfoEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<IdentityInfoEntity>() {
                    @Override
                    public void onNext(IdentityInfoEntity identityInfoEntity) {
                        DataSharedPreferences.saveUesrName(identityInfoEntity.getCompanyName().isEmpty() ? identityInfoEntity.getPersonName() : identityInfoEntity.getCompanyName());
                    }

                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                    }
                });
    }

    private void loadingComplete() {
        /*cancel*/
        mSwipeRefreshHelper.refreshComplete();
    }

    @Override
    public void onParamsChange(Object... objects) {
        cooperationInfoNewEntity = (CooperationInfoNewEntity) objects[0];

    }

    private void updateUI(PartnerIndexInfoEntity entity) {
        tvTime.setText("总收益：(" + DateUtils.getCurrentTime1() + ")");
        tvAllIncome.setText(getSumSpan(CommonUtil.getTwoFloat(entity.total)));
        tvCanCrash.setText(CommonUtil.getTwoFloat(entity.canWithdraw));
        yesterdayIncome.setText("昨日新增收益:" + CommonUtil.getTwoFloat(entity.yesterdayMoney));
        cooperationInfoNewEntity = new CooperationInfoNewEntity();
        cooperationInfoNewEntity.setYesterdayIncome(entity.yesterdayMoney);
        cooperationInfoNewEntity.setRealizableIncome(entity.canWithdraw);

        tvAlreadyCrash.setText(CommonUtil.getTwoFloat(entity.withdrawal));

    }

    private CharSequence getSumSpan(String twoFloat) {
        SpannableString spannableString = new SpannableString(twoFloat);
        spannableString.setSpan(new AbsoluteSizeSpan(20, true), twoFloat.indexOf(".") + 1, twoFloat.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    /**
     * 加载提现的提示
     */
    /**
     * 加载弹窗通知
     */
    private void loadWindowAd() {
        NetService.getInstance().getTipsList("2")
                .subscribe(new CustomApiCallback<List<AdNoticeWindowEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e("弹窗广告加载失败：" + GsonUtils.toJson(ex));
                    }

                    @Override
                    public void onNext(List<AdNoticeWindowEntity> adNoticeWindowEntities) {
                        if (adNoticeWindowEntities != null && adNoticeWindowEntities.size() > 0) {
                            DataSharedPreferences.saveCooperationDialog(false);

                            LogUtils.e("弹窗广告数据：" + GsonUtils.toJson(adNoticeWindowEntities),adNoticeWindowEntities.toString());
                            AdNoticeWindowEntity adNoticeWindowEntity = PublicFuncation.getLastAdItem(adNoticeWindowEntities);
                            if (adNoticeWindowEntity != null) {
                                LogUtils.e(adNoticeWindowEntity.type,"********************");
                                if (adNoticeWindowEntity.type == 0) {
                                    //展示弹窗广告
                                    DialogUtils.getInstance().showMainWindowNoticeDialog(getActivity(), adNoticeWindowEntity);
                                }else if (adNoticeWindowEntity.type == 1) {
                                    DialogUtils.getInstance().showMainText(getActivity(), adNoticeWindowEntity);
                                }
                            }
                        }
                    }
                });
    }



    @OnClick({R.id.btn_apply_crash, R.id.tv_device_manager, R.id.tv_right, R.id.yesterday_income,
            R.id.tv_all_income, R.id.already_crash_container, R.id.img_top_back, R.id.ll_income_today})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.img_top_back:
                /*返回*/
                FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
                break;
            case R.id.tv_right:
                /*提现记录*/
                startActivity(CooperationWithdrawRecordsActivity.class);
                break;
            case R.id.yesterday_income:
                /*昨日收益*/
                ActivityUtils.setActivity(ACTIVITY_EARNINGS_YESTERDAY);
                break;
            case R.id.already_crash_container:
                /*已提现*/
                ActivityUtils.setActivity(ACTIVITY_HAVE_WITHDRAWAL);
                break;
            case R.id.btn_apply_crash:
                if (cooperationInfoNewEntity.isDisable()) {
                    showToast("您已被禁止提现，如有问题请联系客服！");
                } else {
//                    if (cooperationInfoNewEntity.isCashing()) {
                            LogUtils.e(cooperationInfoNewEntity.toString());
                            bundle.putParcelable(CommonUtil.KEY_VALUE_1, cooperationInfoNewEntity);
                            //startActivityForResult(CooperationWithdrawActivity.class, CommonUtil.REQ_CODE_1, bundle);
        //                        MyActivityUtils.goActivity(getActivity(), FragmentContainerActivity.class, FragmentConstants.CashMethodSettingFragment, bundle);
                            MyActivityUtils.goActivity(getActivity(), WithdrawalWayActivity.class, bundle);
//                    } else {
//                        showToast("尊敬的合伙人您好，现在不在提现时间范围内，谢谢!");
//                    }
                }
                break;
            case R.id.ll_income_today:
            case R.id.tv_all_income:
                /*总余额*/
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.IncomeRecordFragment);
                break;
            case R.id.tv_device_manager:
                /*设备管理*/
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.RoleType.PARTNER);
                startActivity(CooperationDeviceActivity.class, bundle);
                break;
        }
    }

    private void onTipClick() {
        if (tipDialog == null) {
            tipDialog = new TipDialog<>(getContext(), null);
        }
        tipDialog.showEnsureNoClose(String.format("每月提现时间为16-20号，若有疑问可致电：%s", getString(R.string.service_phone)), "知道了", "");
    }

    private Drawable getBackDrawable(int color) {
        Drawable up = ContextCompat.getDrawable(getContext(), R.drawable.ic_back_gray_tint);
        Drawable drawableUp = null;
        if (up != null) {
            drawableUp = DrawableCompat.wrap(up);
            DrawableCompat.setTint(drawableUp, color);
        }
        Drawable up1 = ContextCompat.getDrawable(getContext(), R.drawable.ic_back_gray_tint);
        if (up1 != null) {
            Drawable drawableUp1 = DrawableCompat.unwrap(up1);
            DrawableCompat.setTintList(drawableUp1, null);
        }
        return drawableUp;
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
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

    @Override
    public void onfresh() {
        httpData();
    }

    @Override
    public void onResume() {
        super.onResume();
        httpData();
    }


    private void shwoDialog(String src,boolean isattestation,boolean is) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        /* 添加对话框自定义布局 */
        alertDialog.setContentView(R.layout.layout_partner);
        /* 获取对话框窗口 */
        Window window = alertDialog.getWindow();
        /* 设置显示窗口的宽高 */
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        /* 设置窗口显示位置 */
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        TextView tv_content = window.findViewById(R.id.tv_content);
        TextView tv_next = window.findViewById(R.id.tv_next);
        TextView tv_cancel = window.findViewById(R.id.tv_cancel);
        tv_content.setText(src);
        if (isattestation){
            tv_cancel.setVisibility(View.VISIBLE);
            tv_next.setText("去认证");

        }
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isattestation){
                    alertDialog.dismiss();
//                    startActivity(IdentityInfoActivity.class);
                    ActivityUtils.setActivity(ACTIVITY_CERTIFICATION);
                    alertDialog.dismiss();
//                    DialogUtils.shwoCertificationDialog(getActivity());
                }else {
                    if (is){
                        alertDialog.dismiss();
                    }else {
                        getActivity().finish();
                    }
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
