package com.kingyon.elevator.uis.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.customview.MyActionBar;
import com.kingyon.elevator.entities.CooperationIdentityEntity;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.FragmentContainerPresenter;
import com.kingyon.elevator.uis.fragments.homepage.CommentDetailsFragment;
import com.kingyon.elevator.uis.fragments.homepage.NewsRecommendationFragment;
import com.kingyon.elevator.uis.fragments.main.OrderFragment;
import com.kingyon.elevator.uis.fragments.message.CommentListFragment;
import com.kingyon.elevator.uis.fragments.message.DianZanListFragment;
import com.kingyon.elevator.uis.fragments.message.NoticeOrHelperFragment;
import com.kingyon.elevator.uis.fragments.user.CashMethodSettingFragment;
import com.kingyon.elevator.uis.fragments.user.CheckPayVerCodeFragment;
import com.kingyon.elevator.uis.fragments.user.EditLoginPasswordFragment;
import com.kingyon.elevator.uis.fragments.user.EditPassWordFragment;
import com.kingyon.elevator.uis.fragments.user.EditPayPasswordFragment;
import com.kingyon.elevator.uis.fragments.user.FingerSettingFragment;
import com.kingyon.elevator.uis.fragments.user.IncomeOrPayDetailsFragment;
import com.kingyon.elevator.uis.fragments.user.IncomeRecordFragment;
import com.kingyon.elevator.uis.fragments.user.PartnerFragment;
import com.kingyon.elevator.uis.fragments.user.RemeberPasswordFragment;
import com.kingyon.elevator.uis.fragments.user.ResetLoginPasswordFragment;
import com.kingyon.elevator.uis.fragments.user.SecuritySettingFragment;
import com.kingyon.elevator.uis.fragments.user.SetPasswordFragment;
import com.kingyon.elevator.uis.fragments.user.YesterDayIncomeFragment;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.FragmentContainerView;
import com.leo.afbaselibrary.widgets.StateLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * fragment容器界面
 */
public class FragmentContainerActivity extends MvpBaseActivity<FragmentContainerPresenter> implements FragmentContainerView {


    @BindView(R.id.my_action_bar)
    MyActionBar my_action_bar;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String type = "";
    private CooperationInfoNewEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("tag");
        LogUtils.e(type);
        fragmentManager = getSupportFragmentManager();
        setContent();
    }

    @Override
    public FragmentContainerPresenter initPresenter() {
        return new FragmentContainerPresenter(this);
    }

    private void setContent() {
        if (type == null) {
            finish();
            return;
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (type) {
            case FragmentConstants.CashMethodSettingFragment:
                my_action_bar.setTitle("提现方式");
                entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
                //LogUtils.e("接收到的参数：", GsonUtils.toJson(entity));
                fragmentTransaction.replace(R.id.fragment_container, CashMethodSettingFragment.newInstance(entity));
                fragmentTransaction.commit();
                break;
            case FragmentConstants.SetPasswordFragment:
                my_action_bar.setTitle("密码设置");
                String from = getIntent().getStringExtra("from");
                fragmentTransaction.replace(R.id.fragment_container, SetPasswordFragment.newInstance(from));
                fragmentTransaction.commit();
                break;
            case FragmentConstants.IncomeWithYearOrMonth:
                if (RuntimeUtils.chartSelectParameterEntity != null) {
                    if (RuntimeUtils.chartSelectParameterEntity.getSelectIncomeOrPay() == 0) {
                        //收入
                        if (RuntimeUtils.chartSelectParameterEntity.getSelectCatType() == 0) {
                            my_action_bar.setTitle(RuntimeUtils.chartSelectParameterEntity.getCurrentSelectDay() + "月收入");
                        } else {
                            my_action_bar.setTitle(RuntimeUtils.chartSelectParameterEntity.getCurrentSelectMonth() + "月"
                                    + RuntimeUtils.chartSelectParameterEntity.getCurrentSelectDay() + "日收入");
                        }
                    } else {
                        //支出
                        if (RuntimeUtils.chartSelectParameterEntity.getSelectCatType() == 0) {
                            my_action_bar.setTitle(RuntimeUtils.chartSelectParameterEntity.getCurrentSelectDay() + "月支出");
                        } else {
                            my_action_bar.setTitle(RuntimeUtils.chartSelectParameterEntity.getCurrentSelectMonth() + "月"
                                    + RuntimeUtils.chartSelectParameterEntity.getCurrentSelectDay() + "日支出");
                        }
                    }
                    fragmentTransaction.replace(R.id.fragment_container, IncomeOrPayDetailsFragment.newInstance());
                    fragmentTransaction.commit();
                } else {
                    showShortToast("数据错误，请重试");
                    finish();
                }
                break;
            case FragmentConstants.YesterDayIncomeFragment:
                my_action_bar.setTitle(getString(R.string.zuorishouyi));
                fragmentTransaction.replace(R.id.fragment_container, YesterDayIncomeFragment.newInstance(FragmentConstants.YesterDayIncomeFragment));
                fragmentTransaction.commit();
                break;
            case FragmentConstants.AlreadyCrashFragment:
                my_action_bar.setTitle(getString(R.string.yitixian));
                fragmentTransaction.replace(R.id.fragment_container, YesterDayIncomeFragment.newInstance(FragmentConstants.AlreadyCrashFragment));
                fragmentTransaction.commit();
                break;
            case FragmentConstants.IncomeRecordFragment:
                my_action_bar.setTitle(getString(R.string.shouyijilu));
                fragmentTransaction.replace(R.id.fragment_container, IncomeRecordFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.SecuritySettingFragment:
                my_action_bar.setTitle("安全设置");
                fragmentTransaction.replace(R.id.fragment_container, SecuritySettingFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.EditPassWordFragment:
                my_action_bar.setTitle("密码设置");
                fragmentTransaction.replace(R.id.fragment_container, EditPassWordFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.FingerSettingFragment:
                my_action_bar.setTitle("指纹识别");
                fragmentTransaction.replace(R.id.fragment_container, FingerSettingFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.RemeberPasswordFragment:
                my_action_bar.setTitle("修改支付密码");
                fragmentTransaction.replace(R.id.fragment_container, RemeberPasswordFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.EditPayPasswordFragment:
                my_action_bar.setTitle("密码设置");
                Boolean isRemember = getIntent().getBooleanExtra("isRememberPwd", false);
                fragmentTransaction.replace(R.id.fragment_container, EditPayPasswordFragment.newInstance(isRemember));
                fragmentTransaction.commit();
                break;
            case FragmentConstants.EditLoginPasswordFragment:
                my_action_bar.setTitle("修改登录密码");
                fragmentTransaction.replace(R.id.fragment_container, EditLoginPasswordFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.ResetLoginPasswordFragment:
                my_action_bar.setTitle("重置登录密码");
                fragmentTransaction.replace(R.id.fragment_container, ResetLoginPasswordFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.CheckPayVerCodeFragment:
                my_action_bar.setTitle("密码设置");
                fragmentTransaction.replace(R.id.fragment_container, CheckPayVerCodeFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.NoticeListFragment:
                my_action_bar.setTitle("通知");
                fragmentTransaction.replace(R.id.fragment_container, NoticeOrHelperFragment.newInstance(1));
                fragmentTransaction.commit();
                break;
            case FragmentConstants.HelperListFragment:
                my_action_bar.setTitle("小助手");
                fragmentTransaction.replace(R.id.fragment_container, NoticeOrHelperFragment.newInstance(2));
                fragmentTransaction.commit();
                break;
            case FragmentConstants.DianZanListFragment:
                my_action_bar.setTitle("点赞");
                fragmentTransaction.replace(R.id.fragment_container, DianZanListFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.CommentListFragment:
                my_action_bar.setTitle("评论");
                fragmentTransaction.replace(R.id.fragment_container, CommentListFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.CommentDetailsFragment:
                my_action_bar.setTitle("评论详情");
                fragmentTransaction.replace(R.id.fragment_container, CommentDetailsFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.NewsRecommendationFragment:
                my_action_bar.setTitle("更多推荐");
                fragmentTransaction.replace(R.id.fragment_container, NewsRecommendationFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.OrderFragment:
                my_action_bar.setTitle("我的订单");
                fragmentTransaction.replace(R.id.fragment_container, OrderFragment.newInstance(new NormalParamEntity("", "全部订单")));
                fragmentTransaction.commit();
                break;
            default:
                showShortToast(getString(R.string.no_pramater));
                finish();
        }
    }


    /**
     * 获取到容器的加载中布局
     *
     * @return
     */
    public StateLayout getStateLayout() {
        return stateLayout;
    }


    public void showContentView() {
        stateLayout.showContentView();
    }

    public void showProgressView() {
        stateLayout.showProgressView(getString(com.leo.afbaselibrary.R.string.loading));
    }

    public void showEmptyView() {
        stateLayout.showEmptyView(getString(com.leo.afbaselibrary.R.string.empty));
    }

    public void showErrorView() {
        stateLayout.showErrorView(getString(com.leo.afbaselibrary.R.string.error));
    }

    @Override
    public void loadingComplete(int stateCode) {

    }

    @Override
    public void goPartnerDetailsInfo(boolean authed, CooperationIdentityEntity identity, CooperationInfoNewEntity info) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        DialogUtils.getInstance().hideInputPayPwdToCashDailog();
        DialogUtils.getInstance().hideFingerCheckDailog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentManager = null;
        fragmentTransaction = null;
    }
}
