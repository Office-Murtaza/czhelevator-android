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
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.FragmentContainerPresenter;
import com.kingyon.elevator.uis.fragments.user.CashMethodSettingFragment;
import com.kingyon.elevator.uis.fragments.user.IncomeOrPayDetailsFragment;
import com.kingyon.elevator.uis.fragments.user.IncomeRecordFragment;
import com.kingyon.elevator.uis.fragments.user.PartnerFragment;
import com.kingyon.elevator.uis.fragments.user.SetPasswordFragment;
import com.kingyon.elevator.uis.fragments.user.YesterDayIncomeFragment;
import com.kingyon.elevator.utils.CommonUtil;
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
            case FragmentConstants.IncomeWithMonth:
                my_action_bar.setTitle("9月收入");
                fragmentTransaction.replace(R.id.fragment_container, IncomeOrPayDetailsFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.CashMethodSettingFragment:
                my_action_bar.setTitle("提现方式");
                entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
                //LogUtils.e("接收到的参数：", GsonUtils.toJson(entity));
                fragmentTransaction.replace(R.id.fragment_container, CashMethodSettingFragment.newInstance(entity));
                fragmentTransaction.commit();
                break;
            case FragmentConstants.SetPasswordFragment:
                my_action_bar.setTitle("密码设置");
                fragmentTransaction.replace(R.id.fragment_container, SetPasswordFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.IncomeWithDay:
                my_action_bar.setTitle("9月1日收入");
                fragmentTransaction.replace(R.id.fragment_container, IncomeOrPayDetailsFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.PayWithDay:
                my_action_bar.setTitle("9月1日支出");
                fragmentTransaction.replace(R.id.fragment_container, IncomeOrPayDetailsFragment.newInstance());
                fragmentTransaction.commit();
                break;
            case FragmentConstants.PayWithMonth:
                my_action_bar.setTitle("9月支出");
                fragmentTransaction.replace(R.id.fragment_container, IncomeOrPayDetailsFragment.newInstance());
                fragmentTransaction.commit();
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
}
