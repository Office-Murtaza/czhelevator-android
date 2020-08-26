package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.EditPayPasswordFragmentPresenter;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.view.EditPayPasswordFragmentView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 密码设置修改界面
 */
public class EditPassWordFragment extends MvpBaseFragment<EditPayPasswordFragmentPresenter> implements EditPayPasswordFragmentView {

    @Override
    public EditPayPasswordFragmentPresenter initPresenter() {
        return new EditPayPasswordFragmentPresenter(getActivity());
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_edit_pass_word;
    }


    public static EditPassWordFragment newInstance() {
        Bundle args = new Bundle();
        EditPassWordFragment fragment = new EditPassWordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.reset_login_pwd, R.id.reset_pay_password})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.reset_login_pwd:
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.EditLoginPasswordFragment);
                break;
            case R.id.reset_pay_password:
                presenter.checkIsInitPayPwd();
                break;
            default:
        }
    }


    @Override
    public void checkPayPwdIsInit(Boolean isInit) {
        if (isInit) {
            MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.EditPayPasswordFragment);
        } else {
            showShortToast("当前还未设置支付密码，请先设置支付密码");
            MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.SetPasswordFragment, "setting");
        }
    }

    @Override
    public void showCountDownTime(int downtime) {

    }

    @Override
    public void payPasswordEditSuccess(boolean isSuccess) {

    }

    @Override
    public void clearAllInputPwd() {

    }
}
