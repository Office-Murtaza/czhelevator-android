package com.kingyon.elevator.uis.fragments.cooperation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.IdentityInfoEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.PartnerIndexInfoEntity;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.WebViewActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationDeviceActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationWithdrawRecordsActivity;
import com.kingyon.elevator.uis.activities.cooperation.WithdrawalWayActivity;
import com.kingyon.elevator.uis.dialogs.TipDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_EARNINGS_YESTERDAY;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_HAVE_WITHDRAWAL;

/**
 *
 * 合伙人首页
 * */

public class CooperationInfoFragment extends BaseFragment implements OnParamsChangeInterface {


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
        if (getArguments() != null) {
            cooperationInfoNewEntity = getArguments().getParcelable(CommonUtil.KEY_VALUE_1);
        } else {
            cooperationInfoNewEntity = new CooperationInfoNewEntity();
        }
        RuntimeUtils.cooperationInfoNewEntity = cooperationInfoNewEntity;
        StatusBarUtil.setHeadViewPadding(getActivity(), containerView);
//        preVBack.setImageDrawable(getBackDrawable(0xFFFFFFFF));
        httpData();
        loadCashTips();
    }

    private void httpData() {
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().setPartnerIndexInfo()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<PartnerIndexInfoEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        hideProgress();
                    }

                    @Override
                    public void onNext(ConentEntity<PartnerIndexInfoEntity> list) {
                        hideProgress();
                        PartnerIndexInfoEntity partnerIndexInfoEntity = list.getContent().get(0);

                        updateUI(partnerIndexInfoEntity);
                        LogUtils.e(partnerIndexInfoEntity.toString());
                    }
                });

        NetService.getInstance().getIdentityInformation()
                .compose(this.<IdentityInfoEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<IdentityInfoEntity>() {
                    @Override
                    public void onNext(IdentityInfoEntity identityInfoEntity) {
                        DataSharedPreferences.saveUesrName(identityInfoEntity.getCompanyName().isEmpty()?identityInfoEntity.getPersonName():identityInfoEntity.getCompanyName());
                    }

                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                    }
                });

    }

    @Override
    public void onParamsChange(Object... objects) {
        cooperationInfoNewEntity = (CooperationInfoNewEntity) objects[0];

    }

    private void updateUI(PartnerIndexInfoEntity entity) {
        tvTime.setText("总收益：("+ DateUtils.getCurrentTime1()+")");
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
    private void loadCashTips() {
        NetService.getInstance().getTipsList("PARTNER", 2)
                .subscribe(new CustomApiCallback<List<AdNoticeWindowEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("弹窗提现提示加载失败：" + GsonUtils.toJson(ex));
                    }

                    @Override
                    public void onNext(List<AdNoticeWindowEntity> adNoticeWindowEntities) {
                        if (adNoticeWindowEntities != null && adNoticeWindowEntities.size() > 0) {
                            AdNoticeWindowEntity adNoticeWindowEntity = adNoticeWindowEntities.get(0);
                            LogUtils.d("弹窗提现提示数据：" + GsonUtils.toJson(adNoticeWindowEntity));
                            if (adNoticeWindowEntity.getShowType() == 1) {
                                //展示弹窗提示
                                DialogUtils.getInstance().showCashTipsDialog(getContext(), adNoticeWindowEntity.getShowContent(), adNoticeWindowEntity.isLink(), new OnItemClick() {
                                    @Override
                                    public void onItemClick(int position) {
                                        if (adNoticeWindowEntity.isLink()) {
                                            MyActivityUtils.goActivity(getContext(), WebViewActivity.class, adNoticeWindowEntity.getLinkUrl());
                                        }
                                    }
                                });
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
//                    if (entity.isCashing()) {
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
}
